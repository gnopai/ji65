package com.gnopai.ji65.assembler;

import com.gnopai.ji65.parser.expression.*;

public class ExpressionZeroPageChecker implements ExpressionVisitor<Boolean, Expression> {
    public boolean isZeroPage(Expression expression, Environment environment) {
        return expression.accept(this, environment);
    }

    @Override
    public Boolean visit(PrimaryExpression primaryExpression, Environment environment) {
        return primaryExpression.getValue() < 256;
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
    public Boolean visit(IdentifierExpression identifierExpression, Environment<Expression> environment) {
        String name = identifierExpression.getName();
        return environment.get(name)
                .map(expression -> isZeroPage(expression, environment))
                .orElseThrow(() ->
                        new RuntimeException("Unknown identifier referenced \"" + name + "\"")
                );
    }

    @Override
    public Boolean visit(Label label, Environment environment) {
        return label.isZeroPage();
    }
}
