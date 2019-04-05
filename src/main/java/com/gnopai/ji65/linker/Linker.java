package com.gnopai.ji65.linker;

import com.gnopai.ji65.Address;
import com.gnopai.ji65.Program;
import com.gnopai.ji65.assembler.*;
import com.gnopai.ji65.config.ProgramConfig;
import com.gnopai.ji65.parser.expression.ExpressionEvaluator;

import java.util.List;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;

public class Linker implements SegmentDataVisitor {
    private final SegmentMapper segmentMapper;
    private final LabelResolver labelResolver;
    private final ExpressionEvaluator expressionEvaluator;
    private ProgramBuilder programBuilder;
    private Environment environment;

    public Linker(SegmentMapper segmentMapper, LabelResolver labelResolver, ExpressionEvaluator expressionEvaluator) {
        this.segmentMapper = segmentMapper;
        this.labelResolver = labelResolver;
        this.expressionEvaluator = expressionEvaluator;
    }

    public Program link(AssembledSegments assembledSegments, ProgramConfig programConfig) {
        this.environment = assembledSegments.getEnvironment();
        MappedSegments mappedSegments = segmentMapper.mapSegments(programConfig, assembledSegments.getSegments());
        labelResolver.resolve(mappedSegments, environment);

        programBuilder = new ProgramBuilder();
        mappedSegments.getSegments().forEach(this::link);
        return programBuilder.build();
    }

    private void link(MappedSegment segment) {
        programBuilder.segment(segment);
        segment.getData().forEach(this::link);
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

    @Override
    public void visit(ReservedData reservedData) {
        List<Byte> bytes = IntStream.range(0, reservedData.getSize())
                .mapToObj(i -> (byte) 0)
                .collect(toList());
        programBuilder.bytes(bytes);
    }
}
