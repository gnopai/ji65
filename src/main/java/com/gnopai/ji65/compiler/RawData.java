package com.gnopai.ji65.compiler;

import lombok.Value;

import java.util.List;

import static java.util.Collections.singletonList;

@Value
public class RawData implements SegmentData {
    List<Byte> bytes;

    public RawData(byte b) {
        this(singletonList(b));
    }

    public RawData(byte first, byte second) {
        this(List.of(first, second));
    }

    public RawData(List<Byte> bytes) {
        this.bytes = bytes;
    }

    @Override
    public void accept(SegmentDataVisitor visitor) {
        visitor.visit(this);
    }
}
