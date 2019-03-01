package com.gnopai.ji65.assembler;

import com.gnopai.ji65.parser.expression.Expression;
import com.gnopai.ji65.parser.expression.ExpressionVisitor;
import lombok.Value;

@Value
public class Label implements Expression, SegmentData {
    String name;
    boolean zeroPage;

    @Override
    public int getByteCount() {
        return 0;
    }

    @Override
    public void accept(SegmentDataVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public <T> T accept(ExpressionVisitor<T> visitor, Environment environment) {
        return visitor.visit(this, environment);
    }
}
