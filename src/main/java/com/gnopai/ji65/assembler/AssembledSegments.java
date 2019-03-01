package com.gnopai.ji65.assembler;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * Output object of the assembly stage.
 **/
@EqualsAndHashCode
@ToString
public class AssembledSegments {
    private final Map<String, Segment> segments;
    private final Environment environment;

    public AssembledSegments(Environment environment) {
        this(new HashMap<>(), environment);
    }

    public AssembledSegments(Map<String, Segment> segments, Environment environment) {
        this.segments = segments;
        this.environment = environment;
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

    public Environment getEnvironment() {
        return environment;
    }
}
