package com.gnopai.ji65.compiler;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode
@ToString
public class Segment {
    private final String name;
    private final List<SegmentData> data;

    public Segment(String name) {
        this(name, new ArrayList<>());
    }

    public Segment(String name, List<SegmentData> data) {
        this.name = name;
        this.data = data;
    }

    public void add(SegmentData segmentData) {
        data.add(segmentData);
    }

    public List<SegmentData> getSegmentData() {
        return List.copyOf(data);
    }
}
