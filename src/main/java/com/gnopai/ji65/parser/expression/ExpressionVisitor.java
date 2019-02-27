package com.gnopai.ji65.parser.expression;

import com.gnopai.ji65.assembler.Environment;
import com.gnopai.ji65.assembler.Label;

public interface ExpressionVisitor<T, E> {
    T visit(PrimaryExpression primaryExpression, Environment<E> environment);

    T visit(PrefixExpression prefixExpression, Environment<E> environment);

    T visit(BinaryOperatorExpression binaryOperatorExpression, Environment<E> environment);

    T visit(IdentifierExpression identifierExpression, Environment<E> environment);

    T visit(Label label, Environment<E> environment);
}
