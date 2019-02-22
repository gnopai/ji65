package com.gnopai.ji65.parser.expression;

import com.gnopai.ji65.compiler.Environment;

public interface ExpressionVisitor {
    int visit(PrimaryExpression primaryExpression, Environment environment);

    int visit(PrefixExpression prefixExpression, Environment environment);

    int visit(BinaryOperatorExpression binaryOperatorExpression, Environment environment);

    int visit(IdentifierExpression identifierExpression, Environment environment);
}
