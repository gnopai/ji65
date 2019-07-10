package com.gnopai.ji65.parser.statement;

public interface StatementVisitor<T> {
    T visit(InstructionStatement instructionStatement);

    T visit(LabelStatement labelStatement);

    T visit(LocalLabelStatement localLabelStatement);

    T visit(DirectiveStatement directiveStatement);

    T visit(AssignmentStatement assignmentStatement);
}
