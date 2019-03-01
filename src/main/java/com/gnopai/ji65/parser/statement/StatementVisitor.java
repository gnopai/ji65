package com.gnopai.ji65.parser.statement;

public interface StatementVisitor {
    void visit(InstructionStatement instructionStatement);

    void visit(LabelStatement labelStatement);

    void visit(DirectiveStatement directiveStatement);

    void visit(AssignmentStatement assignmentStatement);
}
