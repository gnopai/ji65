package com.gnopai.ji65.parser.statement;

import com.gnopai.ji65.compiler.SegmentData;

public interface StatementVisitor {
    SegmentData visit(InstructionStatement instructionStatement);

    SegmentData visit(ExpressionStatement expressionStatement);

    SegmentData visit(LabelStatement labelStatement);

    SegmentData visit(DirectiveStatement directiveStatement);
}
