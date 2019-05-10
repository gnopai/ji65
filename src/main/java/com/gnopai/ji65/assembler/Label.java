package com.gnopai.ji65.assembler;

import com.gnopai.ji65.parser.expression.Expression;
import com.gnopai.ji65.parser.expression.ExpressionVisitor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import lombok.experimental.Wither;

@Value
@Builder
@Wither
@AllArgsConstructor
public class Label implements Expression, SegmentData {
    String name;
    boolean zeroPage;
    boolean local;

    public Label(String name) {
        this(name, false, false);
    }

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
