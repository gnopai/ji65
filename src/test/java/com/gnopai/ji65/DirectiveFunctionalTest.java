package com.gnopai.ji65;

import com.gnopai.ji65.config.*;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.gnopai.ji65.TestUtil.assembleAndRun;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DirectiveFunctionalTest {
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
    void testReserveDirective() {
        Address programStartAddress = new Address(0x4567);
        ProgramConfig programConfig = ProgramConfig.builder()
                .memoryConfigs(List.of(
                        zeroPageMemoryConfig,
                        programMemoryConfig.withStartAddress(programStartAddress))
                )
                .segmentConfigs(List.of(zeroPageSegmentConfig, programSegmentConfig))
                .build();
        Cpu cpu = Cpu.builder().build();
        assembleAndRun(cpu, programConfig,
                "sec",
                ".res 15",
                "sed",
                ".res 31",
                "sei"
        );
        assertTrue(cpu.isCarryFlagSet());
        assertEquals(Opcode.SEC_IMPLICIT.getOpcode(), cpu.getMemoryValue(programStartAddress));
        assertEquals(Opcode.SED_IMPLICIT.getOpcode(), cpu.getMemoryValue(programStartAddress.plus(16)));
        assertEquals(Opcode.SEI_IMPLICIT.getOpcode(), cpu.getMemoryValue(programStartAddress.plus(48)));
    }

    @Test
    void testByteDirective() {
        Address programStartAddress = new Address(0x4567);
        ProgramConfig programConfig = ProgramConfig.builder()
                .memoryConfigs(List.of(
                        zeroPageMemoryConfig,
                        programMemoryConfig.withStartAddress(programStartAddress))
                )
                .segmentConfigs(List.of(zeroPageSegmentConfig, programSegmentConfig))
                .build();
        Cpu cpu = Cpu.builder().build();
        assembleAndRun(cpu, programConfig,
                "sec",
                ".byte $00, 200 + 55, 0, $80, start, >start, <start"
        );
        assertTrue(cpu.isCarryFlagSet());
        assertEquals(Opcode.SEC_IMPLICIT.getOpcode(), cpu.getMemoryValue(programStartAddress));
        assertEquals((byte) 0x00, cpu.getMemoryValue(programStartAddress.plus(1)));
        assertEquals((byte) 0xFF, cpu.getMemoryValue(programStartAddress.plus(2)));
        assertEquals((byte) 0x00, cpu.getMemoryValue(programStartAddress.plus(3)));
        assertEquals((byte) 0x80, cpu.getMemoryValue(programStartAddress.plus(4)));
        assertEquals((byte) 0x67, cpu.getMemoryValue(programStartAddress.plus(5)));
        assertEquals((byte) 0x45, cpu.getMemoryValue(programStartAddress.plus(6)));
        assertEquals((byte) 0x67, cpu.getMemoryValue(programStartAddress.plus(7)));
    }

    @Test
    void testWordDirective() {
        Address programStartAddress = new Address(0x4567);
        ProgramConfig programConfig = ProgramConfig.builder()
                .memoryConfigs(List.of(
                        zeroPageMemoryConfig,
                        programMemoryConfig.withStartAddress(programStartAddress))
                )
                .segmentConfigs(List.of(zeroPageSegmentConfig, programSegmentConfig))
                .build();
        Cpu cpu = Cpu.builder().build();
        assembleAndRun(cpu, programConfig,
                "sec",
                ".word $00, 200 + 55, 258, $1234, $80, start, >start, <start"
        );
        assertTrue(cpu.isCarryFlagSet());
        assertEquals(Opcode.SEC_IMPLICIT.getOpcode(), cpu.getMemoryValue(programStartAddress));

        assertEquals((byte) 0x00, cpu.getMemoryValue(programStartAddress.plus(1)));
        assertEquals((byte) 0x00, cpu.getMemoryValue(programStartAddress.plus(2)));

        assertEquals((byte) 0xFF, cpu.getMemoryValue(programStartAddress.plus(3)));
        assertEquals((byte) 0x00, cpu.getMemoryValue(programStartAddress.plus(4)));

        assertEquals((byte) 0x02, cpu.getMemoryValue(programStartAddress.plus(5)));
        assertEquals((byte) 0x01, cpu.getMemoryValue(programStartAddress.plus(6)));

        assertEquals((byte) 0x34, cpu.getMemoryValue(programStartAddress.plus(7)));
        assertEquals((byte) 0x12, cpu.getMemoryValue(programStartAddress.plus(8)));

        assertEquals((byte) 0x80, cpu.getMemoryValue(programStartAddress.plus(9)));
        assertEquals((byte) 0x00, cpu.getMemoryValue(programStartAddress.plus(10)));

        assertEquals((byte) 0x67, cpu.getMemoryValue(programStartAddress.plus(11)));
        assertEquals((byte) 0x45, cpu.getMemoryValue(programStartAddress.plus(12)));

        assertEquals((byte) 0x45, cpu.getMemoryValue(programStartAddress.plus(13)));
        assertEquals((byte) 0x00, cpu.getMemoryValue(programStartAddress.plus(14)));

        assertEquals((byte) 0x67, cpu.getMemoryValue(programStartAddress.plus(15)));
        assertEquals((byte) 0x00, cpu.getMemoryValue(programStartAddress.plus(16)));
    }
}
