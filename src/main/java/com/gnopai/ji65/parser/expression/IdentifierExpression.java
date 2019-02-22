package com.gnopai.ji65.parser.expression;

import com.gnopai.ji65.compiler.Environment;
import lombok.Value;

@Value
public class IdentifierExpression implements Expression {
    String name;

    @Override
    public int accept(ExpressionVisitor visitor, Environment environment) {
        return visitor.visit(this, environment);
    }
}
