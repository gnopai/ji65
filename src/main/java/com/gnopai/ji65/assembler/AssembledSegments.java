package com.gnopai.ji65.assembler;

import lombok.Value;

import java.util.List;
import java.util.Optional;

import static java.util.Optional.ofNullable;

/**
 * Output object of the assembly stage.
 **/
@Value
public class AssembledSegments {
    static final String DEFAULT_SEGMENT_NAME = "CODE";

    private final List<Segment> segments;
    private final Environment environment;

    public void add(String segmentName, SegmentData segmentData) {
        getSegment(segmentName)
                .orElseThrow(() -> new RuntimeException("Unknown segment encountered: " + segmentName))
                .add(segmentData);
    }

    public Optional<Segment> getSegment(String segmentName) {
        String segmentNameToFind = ofNullable(segmentName).orElse(DEFAULT_SEGMENT_NAME);
        return segments.stream()
                .filter(segment -> segmentNameToFind.equalsIgnoreCase(segment.getName()))
                .findFirst();
    }
}
