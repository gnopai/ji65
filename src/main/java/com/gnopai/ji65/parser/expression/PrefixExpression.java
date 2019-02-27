package com.gnopai.ji65.parser.expression;

import com.gnopai.ji65.assembler.Environment;
import com.gnopai.ji65.scanner.TokenType;
import lombok.Value;

@Value
public class PrefixExpression implements Expression {
    TokenType operator;
    Expression expression;

    @Override
    public <T, E> T accept(ExpressionVisitor<T, E> visitor, Environment<E> environment) {
        return visitor.visit(this, environment);
    }
}
