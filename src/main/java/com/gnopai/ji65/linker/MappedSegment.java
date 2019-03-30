package com.gnopai.ji65.linker;

import com.gnopai.ji65.Address;
import com.gnopai.ji65.assembler.Segment;
import com.gnopai.ji65.assembler.SegmentData;
import lombok.Value;
import lombok.experimental.NonFinal;

import java.util.List;

@Value
@NonFinal
public class MappedSegment {
    Segment segment;
    Address startAddress;

    public List<SegmentData> getData() {
        return segment.getSegmentData();
    }

    public int getSize() {
        return segment.getSize();
    }
}
