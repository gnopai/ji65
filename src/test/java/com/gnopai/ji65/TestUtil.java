package com.gnopai.ji65;

import com.gnopai.ji65.config.*;
import com.gnopai.ji65.util.ErrorPrinter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TestUtil {
    private static final ProgramConfig DEFAULT_CONFIG = buildDefaultConfig();

    public static void assembleAndRun(Cpu cpu, String... lines) {
        assembleAndRun(cpu, DEFAULT_CONFIG, lines);
    }

    public static void assembleAndRun(Cpu cpu, ProgramConfig programConfig, String... lines) {
        String programText = "start:\n" + Arrays.stream(lines)
                .map(line -> line + "\n")
                .collect(Collectors.joining(""));
        Ji65 ji65 = new Ji65(new ErrorPrinter());
        Program program = ji65.assemble(programText, programConfig);
        ji65.run(program, cpu);
    }

    private static ProgramConfig buildDefaultConfig() {
        return ProgramConfig.builder()
                .memoryConfigs(List.of(
                        MemoryConfig.builder()
                                .name("ZP")
                                .memoryType(MemoryType.READ_WRITE)
                                .startAddress(new Address(0))
                                .size(0x100)
                                .build(),
                        MemoryConfig.builder()
                                .name("PRG")
                                .memoryType(MemoryType.READ_ONLY)
                                .startAddress(new Address(0x8000))
                                .size(0x8000)
                                .build()
                ))
                .segmentConfigs(List.of(
                        SegmentConfig.builder()
                                .segmentName("ZEROPAGE")
                                .memoryConfigName("ZP")
                                .segmentType(SegmentType.ZERO_PAGE)
                                .build(),
                        SegmentConfig.builder()
                                .segmentName("CODE")
                                .memoryConfigName("PRG")
                                .segmentType(SegmentType.READ_ONLY)
                                .build()
                ))
                .build();
    }
}
