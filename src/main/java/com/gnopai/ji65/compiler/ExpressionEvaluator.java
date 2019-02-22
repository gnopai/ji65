package com.gnopai.ji65.compiler;

import com.gnopai.ji65.parser.expression.*;

public class ExpressionEvaluator implements ExpressionVisitor {

    public int evaluate(Expression expression, Environment environment) {
        return expression.accept(this, environment);
    }

    @Override
    public int visit(PrimaryExpression primaryExpression, Environment environment) {
        return primaryExpression.getValue();
    }

    @Override
    public int visit(PrefixExpression expression, Environment environment) {
        int rightSideValue = evaluate(expression.getExpression(), environment);

        switch (expression.getOperator()) {
            case MINUS:
                return -rightSideValue;
            default:
                throw new RuntimeException("Unsupported prefix expression: " + expression.getOperator());
        }
    }

    @Override
    public int visit(BinaryOperatorExpression expression, Environment environment) {
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
    public int visit(IdentifierExpression identifierExpression, Environment environment) {
        return environment.get(identifierExpression.getName())
                .orElseThrow(() ->
                        new RuntimeException("Unknown identifier referenced \"" + identifierExpression.getName() + "\"")
                );
    }
}
