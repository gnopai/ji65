package com.gnopai.ji65.parser.statement;

public interface StatementVisitor<T> {
    T visit(InstructionStatement instructionStatement);

    T visit(LabelStatement labelStatement);

    T visit(UnnamedLabelStatement unnamedLabelStatement);

    T visit(LocalLabelStatement localLabelStatement);

    T visit(DirectiveStatement directiveStatement);

    T visit(AssignmentStatement assignmentStatement);

    T visit(MacroStatement macroStatement);
}
