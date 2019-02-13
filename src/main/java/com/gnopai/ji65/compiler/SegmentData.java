package com.gnopai.ji65.compiler;

import java.util.List;

public interface SegmentData {
    List<Byte> accept(SegmentDataVisitor visitor);
}
