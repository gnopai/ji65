package com.gnopai.ji65.parser.expression;

import com.gnopai.ji65.compiler.Environment;
import com.gnopai.ji65.compiler.Label;

public class ExpressionEvaluator implements ExpressionVisitor<Integer, Integer> {

    public int evaluate(Expression expression, Environment<Integer> environment) {
        return expression.accept(this, environment);
    }

    @Override
    public Integer visit(PrimaryExpression primaryExpression, Environment<Integer> environment) {
        return primaryExpression.getValue();
    }

    @Override
    public Integer visit(PrefixExpression expression, Environment<Integer> environment) {
        int rightSideValue = evaluate(expression.getExpression(), environment);

        switch (expression.getOperator()) {
            case MINUS:
                return -rightSideValue;
            default:
                throw new RuntimeException("Unsupported prefix expression: " + expression.getOperator());
        }
    }

    @Override
    public Integer visit(BinaryOperatorExpression expression, Environment<Integer> environment) {
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
    public Integer visit(IdentifierExpression identifierExpression, Environment<Integer> environment) {
        return getValueFromEnvironment(identifierExpression.getName(), environment);
    }

    @Override
    public Integer visit(Label label, Environment<Integer> environment) {
        return getValueFromEnvironment(label.getName(), environment);
    }

    private Integer getValueFromEnvironment(String name, Environment<Integer> environment) {
        return environment.get(name)
                .orElseThrow(() ->
                        new RuntimeException("Unknown identifier referenced \"" + name + "\"")
                );
    }
}
