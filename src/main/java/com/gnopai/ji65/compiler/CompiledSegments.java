package com.gnopai.ji65.compiler;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * Output object of the compilation stage.
 **/
@EqualsAndHashCode
@ToString
public class CompiledSegments {
    private final Map<String, Segment> segments;

    public CompiledSegments() {
        this.segments = new HashMap<>();
    }

    public CompiledSegments(Map<String, Segment> segments) {
        this.segments = segments;
    }

    public void add(String segmentName, SegmentData segmentData) {
        Segment segment = segments.computeIfAbsent(segmentName, Segment::new);
        segment.add(segmentData);
    }

    public Set<String> getSegmentNames() {
        return Set.copyOf(segments.keySet());
    }

    public Optional<Segment> getSegment(String segmentName) {
        return Optional.ofNullable(segments.get(segmentName));
    }
}
