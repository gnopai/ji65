package com.gnopai.ji65.assembler;

public interface SegmentDataVisitor {
    void visit(InstructionData instructionData);

    void visit(RawData rawData);

    void visit(Label label);

    void visit(UnresolvedExpression unresolvedExpression);

    void visit(RelativeUnresolvedExpression relativeUnresolvedExpression);

    void visit(ReservedData reservedData);

    void visit(TestData testData);
}
