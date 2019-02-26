package com.gnopai.ji65.parser.expression;

import com.gnopai.ji65.compiler.Environment;
import com.gnopai.ji65.scanner.TokenType;
import lombok.Value;

@Value
public class BinaryOperatorExpression implements Expression {
    Expression left;
    TokenType operator;
    Expression right;

    @Override
    public <T, E> T accept(ExpressionVisitor<T, E> visitor, Environment<E> environment) {
        return visitor.visit(this, environment);
    }
}
