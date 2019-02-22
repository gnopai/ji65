package com.gnopai.ji65.parser.statement;

import com.gnopai.ji65.compiler.SegmentData;

import java.util.Optional;

public interface StatementVisitor {
    Optional<SegmentData> visit(InstructionStatement instructionStatement);

    Optional<SegmentData> visit(ExpressionStatement expressionStatement);

    Optional<SegmentData> visit(LabelStatement labelStatement);

    Optional<SegmentData> visit(DirectiveStatement directiveStatement);

    Optional<SegmentData> visit(AssignmentStatement assignmentStatement);
}
