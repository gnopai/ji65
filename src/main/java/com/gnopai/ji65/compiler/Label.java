package com.gnopai.ji65.compiler;

import lombok.Value;

// TODO I think this will need splitting between label definition and label usage
@Value
public class Label implements SegmentData {
    String name;
    // TODO zero-page or not, cheap label, etc.

    @Override
    public void accept(SegmentDataVisitor visitor) {
        visitor.visit(this);
    }
}
