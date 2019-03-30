package com.gnopai.ji65.linker;

import com.gnopai.ji65.Address;
import com.gnopai.ji65.assembler.RawData;
import com.gnopai.ji65.assembler.Segment;
import com.gnopai.ji65.config.MemoryConfig;
import com.gnopai.ji65.config.ProgramConfig;
import com.gnopai.ji65.config.SegmentConfig;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;
import static org.junit.jupiter.api.Assertions.assertEquals;

class SegmentMapperTest {
    private final SegmentMapper segmentMapper = new SegmentMapper(new SegmentAddressCalculator());

    @Test
    void testSingleProgramSegmentInSingleMemorySegment() {
        MemoryConfig memoryConfig1 = memoryConfig("one", 0x1000, 0x2000);
        Segment segment1 = segment("1", "one", 0x800);
        ProgramConfig programConfig = ProgramConfig.builder()
                .memoryConfigs(List.of(memoryConfig1))
                .build();

        MappedSegments mappedSegments = segmentMapper.mapSegments(programConfig, List.of(segment1));

        MappedSegments expectedMappedSegments = new MappedSegments(List.of(
                mappedSegment(segment1, 0x1000)
        ));
        assertEquals(expectedMappedSegments, mappedSegments);
    }

    @Test
    void testMultipleProgramSegmentsInSingleMemorySegment() {
        MemoryConfig memoryConfig1 = memoryConfig("one", 0x2000, 0x1000);
        Segment segment1 = segment("1", "one", 0x800);
        Segment segment2 = segment("2", "one", 0x800);
        ProgramConfig programConfig = programConfig(memoryConfig1);

        MappedSegments mappedSegments = segmentMapper.mapSegments(programConfig, List.of(segment1, segment2));

        MappedSegments expectedMappedSegments = new MappedSegments(List.of(
                mappedSegment(segment1, 0x2000),
                mappedSegment(segment2, 0x2800)

        ));
        assertEquals(expectedMappedSegments, mappedSegments);
    }

    @Test
    void testMultipleProgramSegmentsInSingleMemorySegment_withNonMatchingProgramSegments() {
        MemoryConfig memoryConfig1 = memoryConfig("one", 0x2000, 0x1000);
        Segment segment1 = segment("1", "one", 0x400);
        Segment segment2 = segment("2", "two", 0x100);
        Segment segment3 = segment("3", "one", 0x800);
        Segment segment4 = segment("4", "three", 0x1000);
        ProgramConfig programConfig = programConfig(memoryConfig1);

        MappedSegments mappedSegments = segmentMapper.mapSegments(programConfig, List.of(segment1, segment2, segment3, segment4));

        MappedSegments expectedMappedSegments = new MappedSegments(List.of(
                mappedSegment(segment1, 0x2000),
                mappedSegment(segment3, 0x2400)

        ));
        assertEquals(expectedMappedSegments, mappedSegments);
    }

    @Test
    void testSingleProgramSegmentInSingleMemorySegment_noStartAddress() {
        MemoryConfig memoryConfig1 = memoryConfig("one", 0x800);
        Segment segment1 = segment("1", "one", 0x800);
        ProgramConfig programConfig = ProgramConfig.builder()
                .memoryConfigs(List.of(memoryConfig1))
                .build();

        MappedSegments mappedSegments = segmentMapper.mapSegments(programConfig, List.of(segment1));

        MappedSegments expectedMappedSegments = new MappedSegments(List.of(
                mappedSegment(segment1, 0)
        ));
        assertEquals(expectedMappedSegments, mappedSegments);
    }

    @Test
    void testMultipleMemorySegmentsWithSingleProgramSegments_noStartAddresses() {
        MemoryConfig memoryConfig1 = memoryConfig("one", 0x800);
        MemoryConfig memoryConfig2 = memoryConfig("two", 0x400);
        Segment segment1 = segment("1", "one", 0x200);
        Segment segment2 = segment("2", "two", 0x300);
        ProgramConfig programConfig = ProgramConfig.builder()
                .memoryConfigs(List.of(memoryConfig1, memoryConfig2))
                .build();

        MappedSegments mappedSegments = segmentMapper.mapSegments(programConfig, List.of(segment1, segment2));

        MappedSegments expectedMappedSegments = new MappedSegments(List.of(
                mappedSegment(segment1, 0),
                mappedSegment(segment2, 0x800)
        ));
        assertEquals(expectedMappedSegments, mappedSegments);
    }

    @Test
    void testMultipleMemorySegmentsWithMultipleProgramSegments() {
        MemoryConfig memoryConfig1 = memoryConfig("one", 0x800);
        MemoryConfig memoryConfig2 = memoryConfig("two", 0x600);
        MemoryConfig memoryConfig3 = memoryConfig("three", 0x2000, 0x1000);
        Segment segment1 = segment("1", "one", 0x200);
        Segment segment2 = segment("2", "one", 0x300);
        Segment segment3 = segment("3", "two", 0x200);
        Segment segment4 = segment("4", "two", 0x300);
        Segment segment5 = segment("5", "three", 0x1000);
        ProgramConfig programConfig = ProgramConfig.builder()
                .memoryConfigs(List.of(memoryConfig1, memoryConfig2, memoryConfig3))
                .build();

        MappedSegments mappedSegments = segmentMapper.mapSegments(programConfig, List.of(segment1, segment2, segment3, segment4, segment5));

        MappedSegments expectedMappedSegments = new MappedSegments(List.of(
                mappedSegment(segment1, 0),
                mappedSegment(segment2, 0x200),
                mappedSegment(segment3, 0x800),
                mappedSegment(segment4, 0xA00),
                mappedSegment(segment5, 0x2000)
        ));
        assertEquals(expectedMappedSegments, mappedSegments);
    }

    private MemoryConfig memoryConfig(String name, int size) {
        return MemoryConfig.builder()
                .name(name)
                .size(size)
                .build();
    }

    private MemoryConfig memoryConfig(String name, int startAddress, int size) {
        return MemoryConfig.builder()
                .name(name)
                .startAddress(new Address(startAddress))
                .size(size)
                .build();
    }

    private Segment segment(String name, String memoryConfigName, int size) {
        return new Segment(SegmentConfig.builder()
                .segmentName(name)
                .memoryConfigName(memoryConfigName)
                .build(),
                List.of(rawDataOfSize(size))
        );
    }

    private ProgramConfig programConfig(MemoryConfig... memoryConfigs) {
        return ProgramConfig.builder()
                .memoryConfigs(List.of(memoryConfigs))
                .segmentConfigs(List.of())
                .build();
    }

    private MappedSegment mappedSegment(Segment segment, int address) {
        return new MappedSegment(segment, new Address(address));
    }

    private RawData rawDataOfSize(int size) {
        return new RawData(
                IntStream.range(0, size)
                        .mapToObj(i -> (byte) i)
                        .collect(toList())
        );
    }
}