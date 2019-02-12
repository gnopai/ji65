package com.gnopai.ji65.parser.expression;

public interface ExpressionVisitor {
    int visit(PrimaryExpression primaryExpression);

    int visit(PrefixExpression prefixExpression);

    int visit(BinaryOperatorExpression binaryOperatorExpression);
}
