package com.gnopai.ji65.assembler;

import com.gnopai.ji65.parser.expression.*;

public class ExpressionZeroPageChecker implements ExpressionVisitor<Boolean> {
    public boolean isZeroPage(Expression expression, Environment environment) {
        return expression.accept(this, environment);
    }

    @Override
    public Boolean visit(PrimaryExpression primaryExpression, Environment environment) {
        return isZeroPage(primaryExpression.getValue());
    }

    @Override
    public Boolean visit(PrefixExpression prefixExpression, Environment environment) {
        return isZeroPage(prefixExpression.getExpression(), environment);
    }

    @Override
    public Boolean visit(BinaryOperatorExpression binaryOperatorExpression, Environment environment) {
        return isZeroPage(binaryOperatorExpression.getLeft(), environment) && isZeroPage(binaryOperatorExpression.getRight(), environment);
    }

    @Override
    public Boolean visit(IdentifierExpression identifierExpression, Environment environment) {
        String name = identifierExpression.getName();
        return environment.get(name)
                .map(value -> isZeroPage(value, environment))
                .orElseThrow(() ->
                        new RuntimeException("Unknown identifier referenced \"" + name + "\"")
                );
    }

    @Override
    public Boolean visit(Label label, Environment environment) {
        return label.isZeroPage();
    }

    @Override
    public Boolean visit(RelativeUnnamedLabelExpression relativeUnnamedLabelExpression, Environment environment) {
        // TODO is this good enough? We don't really know where it might be pointing, and they're typically used for relative instructions anyway
        return false;
    }

    private boolean isZeroPage(int value) {
        return value < 256;
    }
}
