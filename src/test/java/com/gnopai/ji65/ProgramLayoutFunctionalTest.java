package com.gnopai.ji65;

import com.gnopai.ji65.config.*;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.gnopai.ji65.TestUtil.assembleAndRun;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ProgramLayoutFunctionalTest {

    private final MemoryConfig zeroPageMemoryConfig = MemoryConfig.builder()
            .name("ZP")
            .memoryType(MemoryType.READ_WRITE)
            .startAddress(new Address(0))
            .size(0x100)
            .build();

    private final MemoryConfig programMemoryConfig = MemoryConfig.builder()
            .name("PRG")
            .memoryType(MemoryType.READ_ONLY)
            .startAddress(new Address(0x8000))
            .size(0x8000)
            .build();

    private final SegmentConfig zeroPageSegmentConfig = SegmentConfig.builder()
            .segmentName("ZEROPAGE")
            .memoryConfigName("ZP")
            .segmentType(SegmentType.ZERO_PAGE)
            .build();
    private final SegmentConfig programSegmentConfig = SegmentConfig.builder()
            .segmentName("CODE")
            .memoryConfigName("PRG")
            .segmentType(SegmentType.READ_ONLY)
            .build();

    @Test
    void testSingleSegmentStartAddress() {
        Address programStartAddress = new Address(0x4567);
        ProgramConfig programConfig = ProgramConfig.builder()
                .memoryConfigs(List.of(
                        zeroPageMemoryConfig,
                        programMemoryConfig.withStartAddress(programStartAddress))
                )
                .segmentConfigs(List.of(zeroPageSegmentConfig, programSegmentConfig))
                .build();
        Cpu cpu = Cpu.builder().build();
        assembleAndRun(cpu, programConfig, "lda #10");
        assertEquals(10, cpu.getAccumulator());
        assertEquals(Opcode.LDA_IMMEDIATE.getOpcode(), cpu.getMemoryValue(programStartAddress));
    }

    @Test
    void testMultipleSegmentStartAddresses() {
        MemoryConfig baseMemoryConfig = MemoryConfig.builder()
                .size(0x100)
                .memoryType(MemoryType.READ_ONLY)
                .build();
        SegmentConfig baseSegmentConfig = SegmentConfig.builder()
                .segmentType(SegmentType.READ_ONLY)
                .build();

        ProgramConfig programConfig = ProgramConfig.builder()
                .memoryConfigs(List.of(
                        zeroPageMemoryConfig,
                        programMemoryConfig.withStartAddress(new Address(0x4000)),
                        baseMemoryConfig.withName("TEST1").withStartAddress(new Address(0x5000)),
                        baseMemoryConfig.withName("TEST2").withStartAddress(new Address(0x6000))
                ))
                .segmentConfigs(List.of(
                        zeroPageSegmentConfig,
                        programSegmentConfig,
                        baseSegmentConfig.withSegmentName("T1").withMemoryConfigName("TEST1"),
                        baseSegmentConfig.withSegmentName("T2").withMemoryConfigName("TEST2")
                ))
                .build();
        Cpu cpu = Cpu.builder().build();
        assembleAndRun(cpu, programConfig,
                "lda #10",
                ".segment \"T1\"",
                "ldx #10",
                ".segment \"T2\"",
                "ldy #10"
        );
        assertEquals(10, cpu.getAccumulator());
        assertEquals(0, cpu.getX()); // not run
        assertEquals(0, cpu.getY()); // not run;
        assertEquals(Opcode.LDA_IMMEDIATE.getOpcode(), cpu.getMemoryValue(new Address(0x4000)));
        assertEquals(Opcode.LDX_IMMEDIATE.getOpcode(), cpu.getMemoryValue(new Address(0x5000)));
        assertEquals(Opcode.LDY_IMMEDIATE.getOpcode(), cpu.getMemoryValue(new Address(0x6000)));
    }
}
