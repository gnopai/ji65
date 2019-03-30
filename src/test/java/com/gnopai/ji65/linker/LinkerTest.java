package com.gnopai.ji65.linker;

import com.gnopai.ji65.Address;
import com.gnopai.ji65.Opcode;
import com.gnopai.ji65.Program;
import com.gnopai.ji65.assembler.*;
import com.gnopai.ji65.config.MemoryConfig;
import com.gnopai.ji65.config.ProgramConfig;
import com.gnopai.ji65.config.SegmentConfig;
import com.gnopai.ji65.config.SegmentType;
import com.gnopai.ji65.parser.expression.ExpressionEvaluator;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class LinkerTest {
    private final SegmentMapper segmentMapper = mock(SegmentMapper.class);
    private final LabelResolver labelResolver = mock(LabelResolver.class);
    private final ExpressionEvaluator expressionEvaluator = mock(ExpressionEvaluator.class);

    private final Environment environment = new Environment();
    private final AssembledSegments assembledSegments = new AssembledSegments(
            List.of(new Segment(SegmentConfig.builder().segmentName("whee").build())),
            environment
    );
    private final ProgramConfig programConfig = ProgramConfig.builder()
            .memoryConfigs(List.of(MemoryConfig.builder().name("whee").build()))
            .build();

    @Test
    void testVisitInstructionData() {
        InstructionData instructionData = new InstructionData(Opcode.LDA_ABSOLUTE_X, new RawData((byte) 80, (byte) 4));
        Label startLabel = label("start", 1);
        MappedSegments mappedSegments = mappedSegments(0x7045, instructionData, startLabel);
        when(segmentMapper.mapSegments(programConfig, assembledSegments.getSegments()))
                .thenReturn(mappedSegments);

        Program program = runLinker();

        List<Byte> expectedBytes = List.of((byte) 0xBD, (byte) 80, (byte) 4);
        Program.Chunk expectedChunk = new Program.Chunk(new Address(0x7045), expectedBytes);
        assertEquals(new Program(List.of(expectedChunk), Map.of("start", 1), new Address(1)), program);
    }

    @Test
    void testVisitRawData() {
        List<Byte> bytes = List.of((byte) 5, (byte) 7);
        Label startLabel = label("start", 1);
        MappedSegments mappedSegments = mappedSegments(0x7040, new RawData(bytes), startLabel);
        when(segmentMapper.mapSegments(programConfig, assembledSegments.getSegments()))
                .thenReturn(mappedSegments);

        Program program = runLinker();

        Program.Chunk expectedChunk = new Program.Chunk(new Address(0x7040), bytes);
        assertEquals(new Program(List.of(expectedChunk), Map.of("start", 1), new Address(1)), program);
    }

    @Test
    void testVisitLabel() {
        Label label = label("derp", 17);
        Label startLabel = label("start", 1);
        MappedSegments mappedSegments = mappedSegments(0x7001, label, startLabel);
        when(segmentMapper.mapSegments(programConfig, assembledSegments.getSegments()))
                .thenReturn(mappedSegments);

        Program program = runLinker();

        Program expectedProgram = new Program(
                List.of(new Program.Chunk(new Address(0x7001), List.of())),
                Map.of("derp", 17, "start", 1),
                new Address(1)
        );
        assertEquals(expectedProgram, program);
    }

    @Test
    void testLink_multipleSegmentData() {
        Label label = label("derp", 7);
        Label startLabel = label("start", 99);

        MappedSegments mappedSegments = mappedSegments(0x6001,
                new InstructionData(Opcode.LDX_IMMEDIATE, new RawData((byte) 77)),
                new InstructionData(Opcode.LDX_ABSOLUTE, new RawData((byte) 80, (byte) 4)),
                new InstructionData(Opcode.STX_ZERO_PAGE, new RawData((byte) 9)),
                label,
                new InstructionData(Opcode.INX_IMPLICIT),
                new InstructionData(Opcode.STX_ZERO_PAGE, new RawData((byte) 10)),
                startLabel
        );
        when(segmentMapper.mapSegments(programConfig, assembledSegments.getSegments()))
                .thenReturn(mappedSegments);

        Program program = runLinker();

        Program.Chunk expectedChunk = new Program.Chunk(new Address(0x6001), List.of(
                (byte) 0xA2, (byte) 77,
                (byte) 0xAE, (byte) 80, (byte) 4,
                (byte) 0x86, (byte) 9,
                (byte) 0xE8,
                (byte) 0x86, (byte) 10
        ));
        Map<String, Integer> expectedLabels = Map.of("derp", 7, "start", 99);
        assertEquals(new Program(List.of(expectedChunk), expectedLabels, new Address(99)), program);
    }

    private Program runLinker() {
        return new Linker(segmentMapper, labelResolver, expressionEvaluator)
                .link(assembledSegments, programConfig);
    }

    private MappedSegments mappedSegments(int startAddress, SegmentData... segmentData) {
        SegmentConfig config = SegmentConfig.builder()
                .segmentName("CODE")
                .segmentType(SegmentType.READ_ONLY)
                .build();
        MappedSegment mappedSegment = new MappedSegment(new Segment(config, List.of(segmentData)), new Address(startAddress));
        return new MappedSegments(List.of(mappedSegment));
    }

    private Label label(String name, int address) {
        Label label = new Label(name, false);
        environment.define(name, label);
        when(expressionEvaluator.evaluate(label, environment)).thenReturn(address);
        return label;
    }
}