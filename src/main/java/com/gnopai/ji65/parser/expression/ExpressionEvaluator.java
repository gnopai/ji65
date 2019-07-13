package com.gnopai.ji65.parser.expression;

import com.gnopai.ji65.Address;
import com.gnopai.ji65.assembler.Environment;
import com.gnopai.ji65.assembler.Label;

import java.util.function.Supplier;

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
            case LESS_THAN:
                return rightSideValue % 256; // low byte
            case GREATER_THAN:
                return rightSideValue / 256; // high byte
            case TILDE:
                return truncateToWordSize(~rightSideValue);
            case BANG:
                return fromBoolean(!(toBoolean(rightSideValue))); // boolean not
            default:
                throw new RuntimeException("Unsupported prefix expression: " + expression.getOperator());
        }
    }

    @Override
    public Integer visit(BinaryOperatorExpression expression, Environment environment) {
        Supplier<Integer> left = () -> evaluate(expression.getLeft(), environment);
        Supplier<Integer> right = () -> evaluate(expression.getRight(), environment);
        switch (expression.getOperator()) {
            case PLUS:
                return left.get() + right.get();
            case MINUS:
                return left.get() - right.get();
            case PIPE:
                return left.get() | right.get();
            case AMPERSAND:
                return left.get() & right.get();
            case CARET:
                return left.get() ^ right.get();
            case SHIFT_LEFT:
                return left.get() << right.get();
            case SHIFT_RIGHT:
                return left.get() >> right.get();
            case STAR:
                return left.get() * right.get();
            case SLASH:
                return left.get() / right.get();
            case EQUAL:
                return fromBoolean(left.get().equals(right.get()));
            case NOT_EQUAL:
                return fromBoolean(!left.get().equals(right.get()));
            case GREATER_THAN:
                return fromBoolean(left.get() > right.get());
            case GREATER_OR_EQUAL_THAN:
                return fromBoolean(left.get() >= right.get());
            case LESS_THAN:
                return fromBoolean(left.get() < right.get());
            case LESS_OR_EQUAL_THAN:
                return fromBoolean(left.get() <= right.get());
            case AND:
                if (toBoolean(left.get())) {
                    return fromBoolean(toBoolean(right.get()));
                }
                return fromBoolean(false);
            case OR:
                if (toBoolean(left.get())) {
                    return fromBoolean(true);
                }
                return fromBoolean(toBoolean(right.get()));
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

    @Override
    public Integer visit(RelativeUnnamedLabelExpression relativeUnnamedLabelExpression, Environment environment) {
        Address currentAddress = new Address(environment.getCurrentAddress());
        return environment.getUnnamedLabel(currentAddress, relativeUnnamedLabelExpression.getOffset());
    }

    private int truncateToWordSize(int i) {
        return i & 0xFFFF;
    }

    private Expression getValueFromEnvironment(String name, Environment environment) {
        return environment.get(name)
                .orElseThrow(() ->
                        new RuntimeException("Unknown identifier referenced \"" + name + "\"")
                );
    }

    private boolean toBoolean(int i) {
        return i != 0;
    }

    private int fromBoolean(boolean b) {
        return b ? 1 : 0;
    }
}
