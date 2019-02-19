package com.gnopai.ji65.compiler;

public interface SegmentDataVisitor {
    void visit(InstructionData instructionData);

    void visit(RawData rawData);

    void visit(Label label);
}
