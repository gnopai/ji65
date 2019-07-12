package com.gnopai.ji65.parser.expression;

import com.gnopai.ji65.assembler.Environment;
import com.gnopai.ji65.assembler.Label;

public interface ExpressionVisitor<T> {
    T visit(PrimaryExpression primaryExpression, Environment environment);

    T visit(PrefixExpression prefixExpression, Environment environment);

    T visit(BinaryOperatorExpression binaryOperatorExpression, Environment environment);

    T visit(IdentifierExpression identifierExpression, Environment environment);

    T visit(Label label, Environment environment);

    T visit(RelativeUnnamedLabelExpression relativeUnnamedLabelExpression, Environment environment);
}
