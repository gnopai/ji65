package com.gnopai.ji65.assembler;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode
@ToString
public class Segment {
    private final String name;
    private final List<SegmentData> data;
    private final boolean zeroPage;

    public Segment(String name) {
        this(name, false);
    }

    public Segment(String name, boolean zeroPage) {
        this(name, zeroPage, new ArrayList<>());
    }

    // TODO getting ahead of ourselves here??? Maybe we should worry about zeropage AFTER we can actually evaluate labels...
    public static Segment zeroPage(String name) {
        return new Segment(name, true);
    }

    public Segment(String name, boolean zeroPage, List<SegmentData> data) {
        this.name = name;
        this.zeroPage = zeroPage;
        this.data = data;
    }

    public void add(SegmentData segmentData) {
        data.add(segmentData);
    }

    public List<SegmentData> getSegmentData() {
        return List.copyOf(data);
    }

    public boolean isZeroPage() {
        return zeroPage;
    }
}
