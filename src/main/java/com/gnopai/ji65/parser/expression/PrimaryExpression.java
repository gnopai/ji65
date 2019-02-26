package com.gnopai.ji65.parser.expression;

import com.gnopai.ji65.compiler.Environment;
import com.gnopai.ji65.scanner.TokenType;
import lombok.Value;

@Value
public class PrimaryExpression implements Expression {
    TokenType type;
    int value;

    @Override
    public <T, E> T accept(ExpressionVisitor<T, E> visitor, Environment<E> environment) {
        return visitor.visit(this, environment);
    }
}
