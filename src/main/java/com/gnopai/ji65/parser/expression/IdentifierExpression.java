package com.gnopai.ji65.parser.expression;

import com.gnopai.ji65.compiler.Environment;
import lombok.Value;

@Value
public class IdentifierExpression implements Expression {
    String name;

    @Override
    public <T, E> T accept(ExpressionVisitor<T, E> visitor, Environment<E> environment) {
        return visitor.visit(this, environment);
    }
}
