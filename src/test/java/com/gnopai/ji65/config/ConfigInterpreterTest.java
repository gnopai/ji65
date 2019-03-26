package com.gnopai.ji65.config;

import com.gnopai.ji65.Address;
import com.gnopai.ji65.scanner.Token;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static com.gnopai.ji65.config.ConfigInterpreter.MEMORY_BLOCK;
import static com.gnopai.ji65.config.ConfigInterpreter.SEGMENTS_BLOCK;
import static com.gnopai.ji65.scanner.TokenType.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ConfigInterpreterTest {

    @Test
    void testMemoryBlock_singleSegment_allFieldsPresent() {
        List<ConfigBlock> blocks = List.of(
                new ConfigBlock(MEMORY_BLOCK, List.of(
                        new ConfigSegment("test", Map.of(
                                "start", new Token(NUMBER, "$8000", 0x8000, 0),
                                "size", new Token(NUMBER, "$2000", 0x2000, 0),
                                "type", new Token(IDENTIFIER, "ro", null, 0),
                                "file", new Token(STRING, "\"%O\"", "%O", 0),
                                "fill", new Token(IDENTIFIER, "yes", null, 0),
                                "fillval", new Token(NUMBER, "$FF", 0xFF, 0)
                        ))
                )),
                new ConfigBlock(SEGMENTS_BLOCK, List.of())
        );

        ProgramConfig programConfig = new ConfigInterpreter().interpret(blocks);

        ProgramConfig expectedProgramConfig = ProgramConfig.builder()
                .segmentConfigs(List.of())
                .memoryConfigs(List.of(
                        MemoryConfig.builder()
                                .name("test")
                                .startAddress(new Address(0x8000))
                                .size(0x2000)
                                .memoryType(MemoryType.READ_ONLY)
                                .file("%O")
                                .dataFillEnabled(true)
                                .fillValue((byte) 0xFF)
                                .build()
                ))
                .build();
        assertEquals(expectedProgramConfig, programConfig);
    }

    @Test
    void testMemoryBlock_singleSegment_noFieldsPresent() {
        List<ConfigBlock> blocks = List.of(
                new ConfigBlock(MEMORY_BLOCK, List.of(
                        new ConfigSegment("test", Map.of())
                )),
                new ConfigBlock(SEGMENTS_BLOCK, List.of())
        );

        ProgramConfig programConfig = new ConfigInterpreter().interpret(blocks);

        ProgramConfig expectedProgramConfig = ProgramConfig.builder()
                .segmentConfigs(List.of())
                .memoryConfigs(List.of(
                        MemoryConfig.builder().name("test").build()
                ))
                .build();
        assertEquals(expectedProgramConfig, programConfig);
    }

    @Test
    void testSegmentsBlock_singleSegment_allFieldsPresent() {
        List<ConfigBlock> blocks = List.of(
                new ConfigBlock(MEMORY_BLOCK, List.of()),
                new ConfigBlock(SEGMENTS_BLOCK, List.of(
                        new ConfigSegment("CODE", Map.of(
                                "load", new Token(IDENTIFIER, "PRG", null, 0),
                                "type", new Token(IDENTIFIER, "ro", null, 0),
                                "start", new Token(NUMBER, "$8000", 0x8000, 0),
                                "align", new Token(NUMBER, "$100", 0x100, 0)
                        ))
                ))
        );

        ProgramConfig programConfig = new ConfigInterpreter().interpret(blocks);

        ProgramConfig expectedProgramConfig = ProgramConfig.builder()
                .segmentConfigs(List.of(
                        SegmentConfig.builder()
                                .segmentName("CODE")
                                .memoryConfigName("PRG")
                                .segmentType(SegmentType.READ_ONLY)
                                .startAddress(new Address(0x8000))
                                .alignment(0x100)
                                .build()
                ))
                .memoryConfigs(List.of())
                .build();
        assertEquals(expectedProgramConfig, programConfig);
    }

    @Test
    void testSegmentsBlock_singleSegment_noFieldsPresent() {
        List<ConfigBlock> blocks = List.of(
                new ConfigBlock(MEMORY_BLOCK, List.of()),
                new ConfigBlock(SEGMENTS_BLOCK, List.of(
                        new ConfigSegment("CODE", Map.of())
                ))
        );

        ProgramConfig programConfig = new ConfigInterpreter().interpret(blocks);

        ProgramConfig expectedProgramConfig = ProgramConfig.builder()
                .segmentConfigs(List.of(
                        SegmentConfig.builder().segmentName("CODE").build()
                ))
                .memoryConfigs(List.of())
                .build();
        assertEquals(expectedProgramConfig, programConfig);
    }

    @Test
    void testMultipleSegmentsInBothBlocks() {
        List<ConfigBlock> blocks = List.of(
                new ConfigBlock(MEMORY_BLOCK, List.of(
                        new ConfigSegment("ZP", Map.of()),
                        new ConfigSegment("PRG", Map.of())
                )),
                new ConfigBlock(SEGMENTS_BLOCK, List.of(
                        new ConfigSegment("ZEROPAGE", Map.of()),
                        new ConfigSegment("CODE", Map.of())
                ))
        );

        ProgramConfig programConfig = new ConfigInterpreter().interpret(blocks);

        ProgramConfig expectedProgramConfig = ProgramConfig.builder()
                .memoryConfigs(List.of(
                        MemoryConfig.builder().name("ZP").build(),
                        MemoryConfig.builder().name("PRG").build()
                ))
                .segmentConfigs(List.of(
                        SegmentConfig.builder().segmentName("ZEROPAGE").build(),
                        SegmentConfig.builder().segmentName("CODE").build()
                ))
                .build();
        assertEquals(expectedProgramConfig, programConfig);
    }
}