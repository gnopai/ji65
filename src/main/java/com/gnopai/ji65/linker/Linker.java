package com.gnopai.ji65.linker;

import com.gnopai.ji65.Address;
import com.gnopai.ji65.Program;
import com.gnopai.ji65.assembler.*;
import com.gnopai.ji65.parser.expression.ExpressionEvaluator;
import com.google.common.annotations.VisibleForTesting;

import java.util.List;

public class Linker implements SegmentDataVisitor {
    private static final int PROGRAM_SIZE = 256 * 128; // TODO eventually get this from the config
    private static final int PROGRAM_START_INDEX = 0x8000; // TODO eventually get this from the config
    private final LabelResolver labelResolver;
    private final ExpressionEvaluator expressionEvaluator;
    private ProgramBuilder programBuilder;
    private Environment environment;

    public Linker(LabelResolver labelResolver, ExpressionEvaluator expressionEvaluator) {
        this.labelResolver = labelResolver;
        this.expressionEvaluator = expressionEvaluator;
    }

    public Program link(AssembledSegments segments) {
        return link(segments, PROGRAM_SIZE, PROGRAM_START_INDEX);
    }

    // 1. determine actual layout (segment addresses -- TODO)
    // 2. define all labels (LabelResolver)
    // 3. resolve everything in the environment from expressions to bytes (ExpressionEvaluator)
    // 4. resolve all segment data bytes (visiting here into ProgramBuilder)
    // 5. output bytes (ProgramBuilder.build)

    // TODO take in a config object too that specifies how the segments should be laid out, including the program size
    @VisibleForTesting
    Program link(AssembledSegments segments, int programSize, int startIndex) {
        this.environment = segments.getEnvironment();
        labelResolver.resolve(segments, startIndex);

        programBuilder = new ProgramBuilder(programSize, startIndex);
        segments.getSegment("CODE")
                .orElseThrow(() -> new RuntimeException("Expected at least a code segment for now"))
                .getSegmentData()
                .forEach(this::link);

        return programBuilder.build();
    }

    private void link(SegmentData segmentData) {
        segmentData.accept(this);
    }

    @Override
    public void visit(InstructionData instructionData) {
        programBuilder.bytes(instructionData.getOpcode().getOpcode());
        link(instructionData.getOperand());
    }

    @Override
    public void visit(RawData rawData) {
        programBuilder.bytes(rawData.getBytes());
    }

    @Override
    public void visit(Label label) {
        int address = expressionEvaluator.evaluate(label, environment);
        programBuilder.label(label.getName(), address);
    }

    @Override
    public void visit(UnresolvedExpression unresolvedExpression) {
        int value = expressionEvaluator.evaluate(unresolvedExpression.getExpression(), environment);
        if (unresolvedExpression.isZeroPage()) {
            programBuilder.bytes((byte) value);
        } else {
            Address address = new Address(value);
            programBuilder.bytes(List.of(address.getLowByte(), address.getHighByte()));
        }
    }
}
