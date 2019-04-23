package com.gnopai.ji65.assembler;

import com.gnopai.ji65.parser.expression.Expression;
import lombok.Value;

@Value
public class RelativeUnresolvedExpression implements SegmentData {
    Expression expression;

    @Override
    public int getByteCount() {
        return 1;
    }

    @Override
    public void accept(SegmentDataVisitor visitor) {
        visitor.visit(this);
    }
}
