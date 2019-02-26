package com.gnopai.ji65.compiler;

import com.gnopai.ji65.parser.expression.Expression;
import com.gnopai.ji65.parser.expression.ExpressionVisitor;
import lombok.Value;

@Value
public class Label implements Expression, SegmentData {
    String name;
    boolean zeroPage;

    @Override
    public void accept(SegmentDataVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public <T, E> T accept(ExpressionVisitor<T, E> visitor, Environment<E> environment) {
        return visitor.visit(this, environment);
    }
}
