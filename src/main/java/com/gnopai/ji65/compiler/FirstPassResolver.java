package com.gnopai.ji65.compiler;

import com.gnopai.ji65.parser.expression.Expression;
import com.gnopai.ji65.parser.statement.*;

import java.util.List;

public class FirstPassResolver implements StatementVisitor {
    private Environment<Expression> environment;
    private String currentSegment;

    public Environment<Expression> resolve(List<Statement> statements) {
        environment = new Environment<>();
        currentSegment = "CODE";
        statements.forEach(statement -> statement.accept(this));
        return environment;
    }

    @Override
    public void visit(InstructionStatement instructionStatement) {
        // second pass only
    }

    @Override
    public void visit(LabelStatement labelStatement) {
        // TODO track zero page by segment we're in
        String name = labelStatement.getName();
        environment.define(name, new Label(name, false));
    }

    @Override
    public void visit(DirectiveStatement directiveStatement) {
        // TODO -- track segment here?
        // TODO I think this is needed in both passes
    }

    @Override
    public void visit(AssignmentStatement assignmentStatement) {
        environment.define(assignmentStatement.getName(), assignmentStatement.getExpression());
    }
}
