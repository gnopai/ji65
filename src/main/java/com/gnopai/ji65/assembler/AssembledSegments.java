package com.gnopai.ji65.assembler;

import lombok.Value;

import java.util.List;
import java.util.Optional;

/**
 * Output object of the assembly stage.
 **/
@Value
public class AssembledSegments {
    private final List<Segment> segments;
    private final Environment environment;

    public void add(String segmentName, SegmentData segmentData) {
        getSegment(segmentName)
                .orElseThrow(() -> new RuntimeException("Unknown segment encountered: " + segmentName))
                .add(segmentData);
    }

    public Optional<Segment> getSegment(String segmentName) {
        return segments.stream()
                .filter(segment -> segmentName.equalsIgnoreCase(segment.getName()))
                .findFirst();
    }
}
