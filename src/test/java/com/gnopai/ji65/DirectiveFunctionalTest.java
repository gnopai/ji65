package com.gnopai.ji65;

import com.gnopai.ji65.config.*;
import com.google.common.io.Resources;
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
                ".byte $00, 200 + 55, 0, $80, start, >start, <start, \"Whee\""
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
        assertEquals((byte) 0x57, cpu.getMemoryValue(programStartAddress.plus(8)));
        assertEquals((byte) 0x68, cpu.getMemoryValue(programStartAddress.plus(9)));
        assertEquals((byte) 0x65, cpu.getMemoryValue(programStartAddress.plus(10)));
        assertEquals((byte) 0x65, cpu.getMemoryValue(programStartAddress.plus(11)));
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
                ".word $00, 200 + 55, 258, $1234, $80, start, >start, <start, \"AB\""
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

        assertEquals((byte) 0x41, cpu.getMemoryValue(programStartAddress.plus(17)));
        assertEquals((byte) 0x00, cpu.getMemoryValue(programStartAddress.plus(18)));

        assertEquals((byte) 0x42, cpu.getMemoryValue(programStartAddress.plus(19)));
        assertEquals((byte) 0x00, cpu.getMemoryValue(programStartAddress.plus(20)));
    }

    @Test
    void testRepeatDirective() {
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
                ".repeat 2*3, I",
                "lda #(I*2)",
                "sta $2000+I",
                ".endrep"
        );

        assertEquals((byte) 0x00, cpu.getMemoryValue(new Address(0x2000)));
        assertEquals((byte) 0x02, cpu.getMemoryValue(new Address(0x2001)));
        assertEquals((byte) 0x04, cpu.getMemoryValue(new Address(0x2002)));
        assertEquals((byte) 0x06, cpu.getMemoryValue(new Address(0x2003)));
        assertEquals((byte) 0x08, cpu.getMemoryValue(new Address(0x2004)));
        assertEquals((byte) 0x0A, cpu.getMemoryValue(new Address(0x2005)));
        assertEquals((byte) 0x00, cpu.getMemoryValue(new Address(0x2006)));
    }

    @Test
    void testRepeatDirective_nested() {
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
                "ldx #0",
                ".repeat 2, I",
                ".repeat 3, J",
                "lda #(I+1)",
                "sta $2000,X",
                "inx",
                "lda #(J+1)",
                "sta $2000,X",
                "inx",
                ".endrep",
                ".endrep"
        );

        assertEquals((byte) 0x01, cpu.getMemoryValue(new Address(0x2000)));
        assertEquals((byte) 0x01, cpu.getMemoryValue(new Address(0x2001)));

        assertEquals((byte) 0x01, cpu.getMemoryValue(new Address(0x2002)));
        assertEquals((byte) 0x02, cpu.getMemoryValue(new Address(0x2003)));

        assertEquals((byte) 0x01, cpu.getMemoryValue(new Address(0x2004)));
        assertEquals((byte) 0x03, cpu.getMemoryValue(new Address(0x2005)));

        assertEquals((byte) 0x02, cpu.getMemoryValue(new Address(0x2006)));
        assertEquals((byte) 0x01, cpu.getMemoryValue(new Address(0x2007)));

        assertEquals((byte) 0x02, cpu.getMemoryValue(new Address(0x2008)));
        assertEquals((byte) 0x02, cpu.getMemoryValue(new Address(0x2009)));

        assertEquals((byte) 0x02, cpu.getMemoryValue(new Address(0x200A)));
        assertEquals((byte) 0x03, cpu.getMemoryValue(new Address(0x200B)));

        assertEquals((byte) 0x00, cpu.getMemoryValue(new Address(0x200C)));
    }

    @Test
    void testMacroDirective() {
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
                ".macro foo arg1, arg2",
                "lda #arg1",
                "clc",
                "adc #$10",
                "sta arg2",
                ".endmacro",

                "foo 1, $2000",
                "foo 1+2+3, $2001",
                "foo $35, $2002"
        );

        assertEquals((byte) 0x11, cpu.getMemoryValue(new Address(0x2000)));
        assertEquals((byte) 0x16, cpu.getMemoryValue(new Address(0x2001)));
        assertEquals((byte) 0x45, cpu.getMemoryValue(new Address(0x2002)));
    }

    @Test
    void testIncludeBinaryDirective() {
        Address programStartAddress = new Address(0x4567);
        ProgramConfig programConfig = ProgramConfig.builder()
                .memoryConfigs(List.of(
                        zeroPageMemoryConfig,
                        programMemoryConfig.withStartAddress(programStartAddress))
                )
                .segmentConfigs(List.of(zeroPageSegmentConfig, programSegmentConfig))
                .build();
        Cpu cpu = Cpu.builder().build();
        String fileName = Resources.getResource("file-load-sample.txt").getFile();

        assembleAndRun(cpu, programConfig,
                "sec",
                ".byte $00",
                ".incbin \"" + fileName + "\""
        );

        assertTrue(cpu.isCarryFlagSet());
        assertEquals(Opcode.SEC_IMPLICIT.getOpcode(), cpu.getMemoryValue(programStartAddress));
        assertEquals((byte) 0x00, cpu.getMemoryValue(programStartAddress.plus(1)));
        assertEquals((byte) 0x54, cpu.getMemoryValue(programStartAddress.plus(2)));
        assertEquals((byte) 0x68, cpu.getMemoryValue(programStartAddress.plus(3)));
        assertEquals((byte) 0x69, cpu.getMemoryValue(programStartAddress.plus(4)));
        assertEquals((byte) 0x73, cpu.getMemoryValue(programStartAddress.plus(5)));
        assertEquals((byte) 0x20, cpu.getMemoryValue(programStartAddress.plus(6)));
        assertEquals((byte) 0x69, cpu.getMemoryValue(programStartAddress.plus(7)));
        assertEquals((byte) 0x73, cpu.getMemoryValue(programStartAddress.plus(8)));
        assertEquals((byte) 0x20, cpu.getMemoryValue(programStartAddress.plus(9)));
        assertEquals((byte) 0x61, cpu.getMemoryValue(programStartAddress.plus(10)));
        assertEquals((byte) 0x20, cpu.getMemoryValue(programStartAddress.plus(11)));
        assertEquals((byte) 0x73, cpu.getMemoryValue(programStartAddress.plus(12)));
        assertEquals((byte) 0x61, cpu.getMemoryValue(programStartAddress.plus(13)));
        assertEquals((byte) 0x6D, cpu.getMemoryValue(programStartAddress.plus(14)));
        assertEquals((byte) 0x70, cpu.getMemoryValue(programStartAddress.plus(15)));
        assertEquals((byte) 0x6C, cpu.getMemoryValue(programStartAddress.plus(16)));
        assertEquals((byte) 0x65, cpu.getMemoryValue(programStartAddress.plus(17)));
        assertEquals((byte) 0x20, cpu.getMemoryValue(programStartAddress.plus(18)));
        assertEquals((byte) 0x69, cpu.getMemoryValue(programStartAddress.plus(19)));
        assertEquals((byte) 0x6E, cpu.getMemoryValue(programStartAddress.plus(20)));
        assertEquals((byte) 0x70, cpu.getMemoryValue(programStartAddress.plus(21)));
        assertEquals((byte) 0x75, cpu.getMemoryValue(programStartAddress.plus(22)));
        assertEquals((byte) 0x74, cpu.getMemoryValue(programStartAddress.plus(23)));
        assertEquals((byte) 0x20, cpu.getMemoryValue(programStartAddress.plus(24)));
        assertEquals((byte) 0x66, cpu.getMemoryValue(programStartAddress.plus(25)));
        assertEquals((byte) 0x69, cpu.getMemoryValue(programStartAddress.plus(26)));
        assertEquals((byte) 0x6C, cpu.getMemoryValue(programStartAddress.plus(27)));
        assertEquals((byte) 0x65, cpu.getMemoryValue(programStartAddress.plus(28)));
        assertEquals((byte) 0x2E, cpu.getMemoryValue(programStartAddress.plus(29)));
        assertEquals((byte) 0x0, cpu.getMemoryValue(programStartAddress.plus(30)));
    }
}
