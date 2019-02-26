package com.gnopai.ji65.compiler;

import com.gnopai.ji65.parser.expression.Expression;
import lombok.Value;

@Value
public class UnresolvedExpression implements SegmentData {
    Expression expression;
    boolean zeroPage;

    @Override
    public void accept(SegmentDataVisitor visitor) {
        visitor.visit(this);
    }
}
