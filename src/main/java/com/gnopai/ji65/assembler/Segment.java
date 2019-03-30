package com.gnopai.ji65.assembler;

import com.gnopai.ji65.Address;
import com.gnopai.ji65.config.MemoryConfig;
import com.gnopai.ji65.config.SegmentConfig;
import com.gnopai.ji65.config.SegmentType;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.Optional.ofNullable;

@EqualsAndHashCode
@ToString
public class Segment {
    private final SegmentConfig config;
    private final List<SegmentData> data;

    public Segment(SegmentConfig config) {
        this(config, new ArrayList<>());
    }

    public Segment(SegmentConfig config, List<SegmentData> data) {
        this.config = config;
        this.data = data;
    }

    public void add(SegmentData segmentData) {
        data.add(segmentData);
    }

    public String getName() {
        return config.getSegmentName();
    }

    public Optional<Address> getStartAddress() {
        return ofNullable(config.getStartAddress());
    }

    public int getAlignment() {
        return config.getAlignment();
    }

    public boolean isForMemoryConfig(MemoryConfig memoryConfig) {
        return memoryConfig.getName().equalsIgnoreCase(config.getMemoryConfigName());
    }

    public List<SegmentData> getSegmentData() {
        return List.copyOf(data);
    }

    public boolean isZeroPage() {
        return SegmentType.ZERO_PAGE.equals(config.getSegmentType());
    }

    public int getSize() {
        return data.stream()
                .map(SegmentData::getByteCount)
                .reduce(0, (a, b) -> a + b);
    }
}
