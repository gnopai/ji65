package com.gnopai.ji65.parser.expression;

import com.gnopai.ji65.assembler.Environment;
import com.gnopai.ji65.assembler.Label;

public class ExpressionEvaluator implements ExpressionVisitor<Integer> {

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
        Expression value = getValueFromEnvironment(identifierExpression.getName(), environment);
        return evaluate(value, environment);
    }

    @Override
    public Integer visit(Label label, Environment environment) {
        Expression value = getValueFromEnvironment(label.getName(), environment);
        if (value instanceof Label) {
            throw new RuntimeException("Unresolved label encountered: \"" + label.getName() + "\"");
        }
        return evaluate(value, environment);
    }

    private Expression getValueFromEnvironment(String name, Environment environment) {
        return environment.get(name)
                .orElseThrow(() ->
                        new RuntimeException("Unknown identifier referenced \"" + name + "\"")
                );
    }
}
