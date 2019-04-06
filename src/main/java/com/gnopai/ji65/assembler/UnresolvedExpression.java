package com.gnopai.ji65.assembler;

import com.gnopai.ji65.parser.expression.Expression;
import lombok.Value;

@Value
public class UnresolvedExpression implements SegmentData {
    Expression expression;
    boolean singleByte;

    @Override
    public int getByteCount() {
        return singleByte ? 1 : 2;
    }

    @Override
    public void accept(SegmentDataVisitor visitor) {
        visitor.visit(this);
    }
}
