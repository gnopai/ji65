package com.gnopai.ji65.linker;

import com.gnopai.ji65.Address;
import com.gnopai.ji65.assembler.Segment;
import com.gnopai.ji65.config.MemoryConfig;
import com.gnopai.ji65.config.ProgramConfig;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

import static java.util.Optional.ofNullable;

public class SegmentMapper {
    private final SegmentAddressCalculator segmentAddressCalculator;

    @Inject
    public SegmentMapper(SegmentAddressCalculator segmentAddressCalculator) {
        this.segmentAddressCalculator = segmentAddressCalculator;
    }

    public MappedSegments mapSegments(ProgramConfig programConfig, List<Segment> segments) {
        List<MappedSegment> allMappedSegments = new ArrayList<>();
        int nextAddress = 0;

        for (MemoryConfig memoryConfig : programConfig.getMemoryConfigs()) {
            int currentAddress = ofNullable(memoryConfig.getStartAddress())
                    .map(Address::getValue)
                    .orElse(nextAddress);
            nextAddress = currentAddress + memoryConfig.getSize();

            List<MappedSegment> mappedSegments = mapSegmentsForMemoryConfig(segments, memoryConfig, currentAddress, nextAddress);
            allMappedSegments.addAll(mappedSegments);
        }

        return new MappedSegments(allMappedSegments);
    }

    private List<MappedSegment> mapSegmentsForMemoryConfig(List<Segment> segments, MemoryConfig memoryConfig, int currentAddress, int nextMemorySegmentAddress) {
        List<MappedSegment> mappedSegments = new ArrayList<>();
        for (Segment segment : segments) {
            if (segment.isForMemoryConfig(memoryConfig)) {
                Address segmentAddress = segmentAddressCalculator.calculateAddress(segment, currentAddress, nextMemorySegmentAddress);
                MappedSegment mappedSegment = new MappedSegment(segment, segmentAddress);
                currentAddress += segment.getSize();
                mappedSegments.add(mappedSegment);
            }
        }
        return mappedSegments;
    }
}
