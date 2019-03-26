package com.gnopai.ji65.config;

import java.util.List;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;

public class ConfigInterpreter {
    static final String MEMORY_BLOCK = "memory";
    static final String SEGMENTS_BLOCK = "segments";

    public ProgramConfig interpret(List<ConfigBlock> blocks) {
        return ProgramConfig.builder()
                .memoryConfigs(interpretBlock(blocks, MEMORY_BLOCK, this::interpretMemoryConfig))
                .segmentConfigs(interpretBlock(blocks, SEGMENTS_BLOCK, this::interpretSegmentConfig))
                .build();
    }

    private <T> List<T> interpretBlock(List<ConfigBlock> blocks, String name, Function<ConfigSegment, T> segmentInterpreter) {
        return blocks.stream()
                .filter(block -> name.equalsIgnoreCase(block.getName()))
                .findFirst()
                .orElseThrow()
                .getSegments()
                .stream()
                .map(segmentInterpreter)
                .collect(toList());
    }

    private MemoryConfig interpretMemoryConfig(ConfigSegment segment) {
        return MemoryConfig.builder()
                .name(segment.getName())
                .startAddress(segment.getAddressValue("start").orElse(null))
                .size(segment.getIntValue("size").orElse(0))
                .memoryType(segment.getValue("type", MemoryType::fromName).orElse(null))
                .file(segment.getStringValue("file").orElse(null))
                .dataFillEnabled(segment.getBooleanValue("fill").orElse(false))
                .fillValue(segment.getByteValue("fillval").orElse((byte) 0))
                .build();
    }

    private SegmentConfig interpretSegmentConfig(ConfigSegment segment) {
        return SegmentConfig.builder()
                .segmentName(segment.getName())
                .memoryConfigName(segment.getStringValue("load").orElse(null))
                .segmentType(segment.getValue("type", SegmentType::fromName).orElse(null))
                .startAddress(segment.getAddressValue("start").orElse(null))
                .alignment(segment.getIntValue("align").orElse(0))
                .build();
    }
}
