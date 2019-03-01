package com.gnopai.ji65.assembler;

public interface SegmentData {
    int getByteCount();

    void accept(SegmentDataVisitor visitor);
}
