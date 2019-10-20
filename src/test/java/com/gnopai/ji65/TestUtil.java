package com.gnopai.ji65;

import com.gnopai.ji65.config.*;
import com.gnopai.ji65.interpreter.EndProgramAtValue;
import com.gnopai.ji65.interpreter.ProgramEndStrategy;
import com.gnopai.ji65.scanner.SourceFile;
import com.gnopai.ji65.util.ErrorPrinter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TestUtil {
    public static final Address DEFAULT_PROGRAM_START_ADDRESS = new Address(0x8000);
    private static final ProgramConfig DEFAULT_CONFIG = buildDefaultConfig();
    private static final ProgramEndStrategy DEFAULT_PROGRAM_END_STRATEGY = new EndProgramAtValue((byte) 0);

    public static void assembleAndRun(Cpu cpu, String... lines) {
        assembleAndRun(cpu, DEFAULT_CONFIG, DEFAULT_PROGRAM_END_STRATEGY, lines);
    }

    public static void assembleAndRun(Cpu cpu, ProgramConfig programConfig, String... lines) {
        assembleAndRun(cpu, programConfig, DEFAULT_PROGRAM_END_STRATEGY, lines);
    }

    public static void assembleAndRun(Cpu cpu, ProgramEndStrategy programEndStrategy, String... lines) {
        assembleAndRun(cpu, DEFAULT_CONFIG, programEndStrategy, lines);
    }

    public static void assembleAndRun(Cpu cpu, ProgramConfig programConfig, ProgramEndStrategy programEndStrategy, String... lines) {
        String startLabel = "start";
        String programText = startLabel + ":\n" + Arrays.stream(lines)
                .map(line -> line + "\n")
                .collect(Collectors.joining(""));
        SourceFile sourceFile = new SourceFile(null, programText);
        Ji65 ji65 = new Ji65(new ErrorPrinter());
        Program program = ji65.assemble(sourceFile, programConfig);
        Address startAddress = program.getLabelAddress(startLabel)
            .orElseThrow(() -> new RuntimeException("Label not found: " + startLabel));
        ji65.run(program, startAddress, cpu, programEndStrategy);
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
                                .startAddress(DEFAULT_PROGRAM_START_ADDRESS)
                                .size(0x4000)
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
                                .startAddress(DEFAULT_PROGRAM_START_ADDRESS)
                                .build(),
                        SegmentConfig.builder()
                                .segmentName("RODATA")
                                .memoryConfigName("PRG")
                                .segmentType(SegmentType.READ_ONLY)
                                .build()
                ))
                .build();
    }
}
