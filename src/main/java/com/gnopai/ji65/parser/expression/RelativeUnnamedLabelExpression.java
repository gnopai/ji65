package com.gnopai.ji65.parser.expression;

import com.gnopai.ji65.assembler.Environment;
import lombok.Value;

@Value
public class RelativeUnnamedLabelExpression implements Expression {
    int offset;

    @Override
    public <T> T accept(ExpressionVisitor<T> visitor, Environment environment) {
        return visitor.visit(this, environment);
    }
}
