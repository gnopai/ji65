package com.gnopai.ji65.parser.expression;

import lombok.Value;

@Value
public class IdentifierExpression implements Expression {
    String name;

    @Override
    public int accept(ExpressionVisitor visitor) {
        return visitor.visit(this);
    }
}
