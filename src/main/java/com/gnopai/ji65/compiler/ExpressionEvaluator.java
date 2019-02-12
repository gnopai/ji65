package com.gnopai.ji65.compiler;

import com.gnopai.ji65.parser.expression.*;

public class ExpressionEvaluator implements ExpressionVisitor {

    public int evaluate(Expression expression) {
        return expression.accept(this);
    }

    @Override
    public int visit(PrimaryExpression primaryExpression) {
        return primaryExpression.getValue();
    }

    @Override
    public int visit(PrefixExpression expression) {
        int rightSideValue = evaluate(expression.getExpression());

        switch (expression.getOperator()) {
            case MINUS:
                return -rightSideValue;
            default:
                throw new RuntimeException("Unsupported prefix expression: " + expression.getOperator());
        }
    }

    @Override
    public int visit(BinaryOperatorExpression expression) {
        int leftSideValue = evaluate(expression.getLeft());
        int rightSideValue = evaluate(expression.getRight());
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
}
