package com.gnopai.ji65.assembler;

import com.gnopai.ji65.parser.expression.ExpressionEvaluator;
import com.gnopai.ji65.parser.expression.PrimaryExpression;
import com.gnopai.ji65.parser.statement.*;
import com.gnopai.ji65.scanner.TokenType;

import java.util.List;

public class FirstPassResolver implements StatementVisitor {
    private final ExpressionEvaluator expressionEvaluator;
    private Environment environment;

    public FirstPassResolver(ExpressionEvaluator expressionEvaluator) {
        this.expressionEvaluator = expressionEvaluator;
    }

    public void resolve(List<Statement> statements, Environment environment) {
        this.environment = environment;
        statements.forEach(statement -> statement.accept(this));
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
        // TODO support labels here? We'll error out on them as it is now.
        int value = expressionEvaluator.evaluate(assignmentStatement.getExpression(), environment);
        environment.define(assignmentStatement.getName(), new PrimaryExpression(TokenType.NUMBER, value));
    }
}
