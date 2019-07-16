package com.gnopai.ji65.assembler;

import com.gnopai.ji65.parser.expression.*;

public class ExpressionValueSubstituter implements ExpressionVisitor<Expression> {
    private String name;
    private Expression value;
    private Environment environment;

    public Expression substituteValue(String name, Expression value, Expression expression, Environment environment) {
        this.name = name;
        this.value = value;
        this.environment = environment;
        return doSubstitution(expression);
    }

    private Expression doSubstitution(Expression expression) {
        return expression.accept(this, environment);
    }

    @Override
    public Expression visit(PrimaryExpression primaryExpression, Environment environment) {
        return primaryExpression;
    }

    @Override
    public Expression visit(PrefixExpression prefixExpression, Environment environment) {
        return new PrefixExpression(
                prefixExpression.getOperator(),
                doSubstitution(prefixExpression.getExpression())
        );
    }

    @Override
    public Expression visit(BinaryOperatorExpression binaryOperatorExpression, Environment environment) {
        return new BinaryOperatorExpression(
                doSubstitution(binaryOperatorExpression.getLeft()),
                binaryOperatorExpression.getOperator(),
                doSubstitution(binaryOperatorExpression.getRight())
        );
    }

    @Override
    public Expression visit(IdentifierExpression identifierExpression, Environment environment) {
        return name.equals(identifierExpression.getName()) ? value : identifierExpression;
    }

    @Override
    public Expression visit(Label label, Environment environment) {
        return label;
    }

    @Override
    public Expression visit(RelativeUnnamedLabelExpression relativeUnnamedLabelExpression, Environment environment) {
        return relativeUnnamedLabelExpression;
    }
}
