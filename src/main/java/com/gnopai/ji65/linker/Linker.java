package com.gnopai.ji65.linker;

import com.gnopai.ji65.Program;
import com.gnopai.ji65.assembler.*;
import com.google.common.annotations.VisibleForTesting;

public class Linker implements SegmentDataVisitor {
    private static final int PROGRAM_SIZE = 256 * 128; // TODO eventually get this from the config
    private static final int PROGRAM_START_INDEX = 0x8000; // TODO eventually get this from the config
    private ProgramBuilder programBuilder;

    public Program link(AssembledSegments segments) {
        return link(segments, PROGRAM_SIZE, PROGRAM_START_INDEX);
    }

    // TODO
    // 1. determine actual layout (segment addresses), define all labels
    // 2. resolve everything in the environment from expressions to values
    // 3. resolve all segment data bytes
    // 4. output bytes

    // TODO take in a config object too that specifies how the segments should be laid out, including the program size
    @VisibleForTesting
    Program link(AssembledSegments segments, int programSize, int startIndex) {
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
        programBuilder.label(label.getName());
    }

    @Override
    public void visit(UnresolvedExpression unresolvedExpression) {
        // TODO include ExpressionEvaluator and use it here???
    }
}
