package com.gnopai.ji65.compiler;

import lombok.Value;

import java.util.List;

// TODO I think this will need splitting between label definition and label usage
@Value
public class Label implements SegmentData {
    String name;
    // TODO zero-page or not, cheap label, etc.

    @Override
    public List<Byte> accept(SegmentDataVisitor visitor) {
        return visitor.visit(this);
    }
}
