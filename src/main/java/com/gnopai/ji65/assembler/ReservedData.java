package com.gnopai.ji65.assembler;

import lombok.Value;

@Value
public class ReservedData implements SegmentData {
    int size;

    @Override
    public int getByteCount() {
        return size;
    }

    @Override
    public void accept(SegmentDataVisitor visitor) {
        visitor.visit(this);
    }
}
