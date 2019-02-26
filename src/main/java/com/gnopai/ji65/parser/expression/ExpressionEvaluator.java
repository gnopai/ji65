package com.gnopai.ji65.parser.expression;

import com.gnopai.ji65.compiler.Environment;

public class ExpressionEvaluator implements ExpressionVisitor<Integer> {

    // TODO I don't think we should use this until linking -- another compile-stage process is needed
    // that returns what kind of value: raw value, zero page address, absolute address
    public int evaluate(Expression expression, Environment environment) {
        return expression.accept(this, environment);
    }

    @Override
    public Integer visit(PrimaryExpression primaryExpression, Environment environment) {
        return primaryExpression.getValue();
    }

    @Override
    public Integer visit(PrefixExpression expression, Environment environment) {
        int rightSideValue = evaluate(expression.getExpression(), environment);

        switch (expression.getOperator()) {
            case MINUS:
                return -rightSideValue;
            default:
                throw new RuntimeException("Unsupported prefix expression: " + expression.getOperator());
        }
    }

    @Override
    public Integer visit(BinaryOperatorExpression expression, Environment environment) {
        int leftSideValue = evaluate(expression.getLeft(), environment);
        int rightSideValue = evaluate(expression.getRight(), environment);
        switch (expression.getOperator()) {
            case PLUS:
                return leftSideValue + rightSideValue;
            case MINUS:
                return leftSideValue - rightSideValue;
            case STAR:
                return leftSideValue * rightSideValue;
            case SLASH:
                return leftSideValue / rightSideValue;
            default:
                throw new RuntimeException("Unsupported binary expression: " + expression.getOperator());
        }
    }

    @Override
    public Integer visit(IdentifierExpression identifierExpression, Environment environment) {
        String name = identifierExpression.getName();
        return environment.get(name)
                .orElseThrow(() ->
                        new RuntimeException("Unknown identifier referenced \"" + name + "\"")
                );
    }
}
