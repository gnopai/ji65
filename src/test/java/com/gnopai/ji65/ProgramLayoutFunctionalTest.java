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

    private final List<SegmentConfig> segmentConfigs = List.of(
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
    );

    @Test
    void testSegmentStartAddresses() {
        Address programStartAddress = new Address(0x4567);
        ProgramConfig programConfig = ProgramConfig.builder()
                .memoryConfigs(List.of(
                        zeroPageMemoryConfig,
                        programMemoryConfig.withStartAddress(programStartAddress))
                )
                .segmentConfigs(segmentConfigs)
                .build();
        Cpu cpu = Cpu.builder().build();
        assembleAndRun(cpu, programConfig, "lda #10");
        assertEquals(10, cpu.getAccumulator());
        assertEquals(Opcode.LDA_IMMEDIATE.getOpcode(), cpu.getMemoryValue(programStartAddress));
    }
}
