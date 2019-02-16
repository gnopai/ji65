package com.gnopai.ji65;

import org.junit.jupiter.api.Test;

import static com.gnopai.ji65.TestUtil.compileAndRun;
import static org.junit.jupiter.api.Assertions.*;

class InstructionFunctionalTest {

    @Test
    void testClc() {
        Cpu cpu = Cpu.builder().carryFlagSet(true).build();
        compileAndRun(cpu, "clc");
        assertFalse(cpu.isCarryFlagSet());
    }

    @Test
    void testCld() {
        Cpu cpu = Cpu.builder().decimalModeSet(true).build();
        compileAndRun(cpu, "cld");
        assertFalse(cpu.isDecimalModeSet());
    }

    @Test
    void testCli() {
        Cpu cpu = Cpu.builder().interruptDisableSet(true).build();
        compileAndRun(cpu, "cli");
        assertFalse(cpu.isInterruptDisableSet());
    }

    @Test
    void testClv() {
        Cpu cpu = Cpu.builder().overflowFlagSet(true).build();
        compileAndRun(cpu, "clv");
        assertFalse(cpu.isOverflowFlagSet());
    }

    @Test
    void testLdaImmediate() {
        Cpu cpu = Cpu.builder().build();
        compileAndRun(cpu, "lda #$10");
        assertEquals((byte) 0x10, cpu.getAccumulator());
    }

    @Test
    void testLdaZeroPage() {
        Cpu cpu = Cpu.builder().build();
        cpu.setMemoryValue(new Address(8), (byte) 0x17);
        compileAndRun(cpu, "lda $08");
        assertEquals((byte) 0x17, cpu.getAccumulator());
    }

    @Test
    void testLdaZeroPageX() {
        Cpu cpu = Cpu.builder().build();
        cpu.setMemoryValue(new Address(8), (byte) 0x19);
        cpu.setX((byte) 4);
        compileAndRun(cpu, "lda $04,X");
        assertEquals((byte) 0x19, cpu.getAccumulator());
    }

    @Test
    void testLdaAbsolute() {
        Cpu cpu = Cpu.builder().build();
        cpu.setMemoryValue(new Address(0x5432), (byte) 0x17);
        compileAndRun(cpu, "lda $5432");
        assertEquals((byte) 0x17, cpu.getAccumulator());
    }

    @Test
    void testLdaAbsoluteX() {
        Cpu cpu = Cpu.builder().build();
        cpu.setX((byte) 7);
        cpu.setMemoryValue(new Address(0x5439), (byte) 0x17);
        compileAndRun(cpu, "lda $5432,X");
        assertEquals((byte) 0x17, cpu.getAccumulator());
    }

    @Test
    void testLdaAbsoluteY() {
        Cpu cpu = Cpu.builder().build();
        cpu.setY((byte) 6);
        cpu.setMemoryValue(new Address(0x5436), (byte) 0x12);
        compileAndRun(cpu, "lda $5430,Y");
        assertEquals((byte) 0x12, cpu.getAccumulator());
    }

    @Test
    void testLdaIndexedIndirect() {
        Cpu cpu = Cpu.builder()
                .x((byte) 0x11)
                .build();
        cpu.setMemoryValue(new Address(0x56), (byte) 0x77);
        cpu.setMemoryValue(new Address(0x57), (byte) 0x40);
        cpu.setMemoryValue(new Address(0x4077), (byte) 0x99);
        compileAndRun(cpu, "lda ($45,X)");
        assertEquals((byte) 0x99, cpu.getAccumulator());
    }

    @Test
    void testLdaIndirectIndexed() {
        Cpu cpu = Cpu.builder()
                .y((byte) 0x11)
                .build();
        cpu.setMemoryValue(new Address(0x45), (byte) 0x78);
        cpu.setMemoryValue(new Address(0x46), (byte) 0x41);
        cpu.setMemoryValue(new Address(0x4189), (byte) 0xBC);
        compileAndRun(cpu, "lda ($45),Y");
        assertEquals((byte) 0xBC, cpu.getAccumulator());
    }

    @Test
    void testLdxImmediate() {
        Cpu cpu = Cpu.builder().build();
        compileAndRun(cpu, "ldx #$10");
        assertEquals((byte) 0x10, cpu.getX());
    }

    @Test
    void testLdxZeroPage() {
        Cpu cpu = Cpu.builder().build();
        cpu.setMemoryValue(new Address(8), (byte) 0x17);
        compileAndRun(cpu, "ldx $08");
        assertEquals((byte) 0x17, cpu.getX());
    }

    @Test
    void testLdxZeroPageY() {
        Cpu cpu = Cpu.builder().build();
        cpu.setMemoryValue(new Address(7), (byte) 0x19);
        cpu.setY((byte) 3);
        compileAndRun(cpu, "ldx $04,y");
        assertEquals((byte) 0x19, cpu.getX());
    }

    @Test
    void testLdxAbsolute() {
        Cpu cpu = Cpu.builder().build();
        cpu.setMemoryValue(new Address(0x5432), (byte) 0x17);
        compileAndRun(cpu, "ldx $5432");
        assertEquals((byte) 0x17, cpu.getX());
    }

    @Test
    void testLdxAbsoluteY() {
        Cpu cpu = Cpu.builder().build();
        cpu.setY((byte) 6);
        cpu.setMemoryValue(new Address(0x5436), (byte) 0x12);
        compileAndRun(cpu, "ldx $5430,Y");
        assertEquals((byte) 0x12, cpu.getX());
    }

    @Test
    void testLdyImmediate() {
        Cpu cpu = Cpu.builder().build();
        compileAndRun(cpu, "ldy #$10");
        assertEquals((byte) 0x10, cpu.getY());
    }

    @Test
    void testLdyZeroPage() {
        Cpu cpu = Cpu.builder().build();
        cpu.setMemoryValue(new Address(8), (byte) 0x17);
        compileAndRun(cpu, "ldy $08");
        assertEquals((byte) 0x17, cpu.getY());
    }

    @Test
    void testLdyZeroPageX() {
        Cpu cpu = Cpu.builder().build();
        cpu.setMemoryValue(new Address(8), (byte) 0x19);
        cpu.setX((byte) 4);
        compileAndRun(cpu, "ldy $04,X");
        assertEquals((byte) 0x19, cpu.getY());
    }

    @Test
    void testLdyAbsolute() {
        Cpu cpu = Cpu.builder().build();
        cpu.setMemoryValue(new Address(0x5432), (byte) 0x17);
        compileAndRun(cpu, "ldy $5432");
        assertEquals((byte) 0x17, cpu.getY());
    }

    @Test
    void testLdyAbsoluteX() {
        Cpu cpu = Cpu.builder().build();
        cpu.setX((byte) 6);
        cpu.setMemoryValue(new Address(0x5436), (byte) 0x12);
        compileAndRun(cpu, "ldy $5430,X");
        assertEquals((byte) 0x12, cpu.getY());
    }

    @Test
    void testSec() {
        Cpu cpu = Cpu.builder().carryFlagSet(false).build();
        compileAndRun(cpu, "sec");
        assertTrue(cpu.isCarryFlagSet());
    }

    @Test
    void testSed() {
        Cpu cpu = Cpu.builder().decimalModeSet(false).build();
        compileAndRun(cpu, "sed");
        assertTrue(cpu.isDecimalModeSet());
    }

    @Test
    void testSei() {
        Cpu cpu = Cpu.builder().interruptDisableSet(false).build();
        compileAndRun(cpu, "sei");
        assertTrue(cpu.isInterruptDisableSet());
    }

    @Test
    void testStaZeroPage() {
        Cpu cpu = Cpu.builder()
                .accumulator((byte) 0x14)
                .build();
        compileAndRun(cpu, "sta $23");
        assertEquals((byte) 0x14, cpu.getMemoryValue(new Address(0x23)));
    }

    @Test
    void testStaZeroPageX() {
        Cpu cpu = Cpu.builder()
                .accumulator((byte) 0x14)
                .x((byte) 3)
                .build();
        compileAndRun(cpu, "sta $23,X");
        assertEquals((byte) 0x14, cpu.getMemoryValue(new Address(0x26)));
    }

    @Test
    void testStaAbsolute() {
        Cpu cpu = Cpu.builder()
                .accumulator((byte) 0x14)
                .build();
        compileAndRun(cpu, "sta $2346");
        assertEquals((byte) 0x14, cpu.getMemoryValue(new Address(0x2346)));
    }

    @Test
    void testStaAbsoluteX() {
        Cpu cpu = Cpu.builder()
                .accumulator((byte) 0x14)
                .x((byte) 0x10)
                .build();
        compileAndRun(cpu, "sta $2346,X");
        assertEquals((byte) 0x14, cpu.getMemoryValue(new Address(0x2356)));
    }

    @Test
    void testStaAbsoluteY() {
        Cpu cpu = Cpu.builder()
                .accumulator((byte) 0x15)
                .y((byte) 0x11)
                .build();
        compileAndRun(cpu, "sta $2346,Y");
        assertEquals((byte) 0x15, cpu.getMemoryValue(new Address(0x2357)));
    }

    @Test
    void testStaIndexedIndirect() {
        Cpu cpu = Cpu.builder()
                .accumulator((byte) 0x19)
                .x((byte) 0x11)
                .build();
        cpu.setMemoryValue(new Address(0x56), (byte) 0x77);
        cpu.setMemoryValue(new Address(0x57), (byte) 0x40);
        compileAndRun(cpu, "sta ($45,X)");
        assertEquals((byte) 0x19, cpu.getMemoryValue(new Address(0x4077)));
    }

    @Test
    void testStaIndirectIndexed() {
        Cpu cpu = Cpu.builder()
                .accumulator((byte) 0xB9)
                .y((byte) 0x11)
                .build();
        cpu.setMemoryValue(new Address(0x45), (byte) 0x78);
        cpu.setMemoryValue(new Address(0x46), (byte) 0x41);
        cpu.setMemoryValue(new Address(0x4189), (byte) 0xBC);
        compileAndRun(cpu, "sta ($45),Y");
        assertEquals((byte) 0xB9, cpu.getMemoryValue(new Address(0x4189)));
    }

    @Test
    void testStxZeroPage() {
        Cpu cpu = Cpu.builder()
                .x((byte) 0x14)
                .build();
        compileAndRun(cpu, "stx $23");
        assertEquals((byte) 0x14, cpu.getMemoryValue(new Address(0x23)));
    }

    @Test
    void testStxZeroPageY() {
        Cpu cpu = Cpu.builder()
                .x((byte) 0x14)
                .y((byte) 3)
                .build();
        compileAndRun(cpu, "stx $23,y");
        assertEquals((byte) 0x14, cpu.getMemoryValue(new Address(0x26)));
    }

    @Test
    void testStxAbsolute() {
        Cpu cpu = Cpu.builder()
                .x((byte) 0x14)
                .build();
        compileAndRun(cpu, "stx $2310");
        assertEquals((byte) 0x14, cpu.getMemoryValue(new Address(0x2310)));
    }

    @Test
    void testStyZeroPage() {
        Cpu cpu = Cpu.builder()
                .y((byte) 0x14)
                .build();
        compileAndRun(cpu, "sty $23");
        assertEquals((byte) 0x14, cpu.getMemoryValue(new Address(0x23)));
    }

    @Test
    void testStyZeroPageX() {
        Cpu cpu = Cpu.builder()
                .y((byte) 0x14)
                .x((byte) 3)
                .build();
        compileAndRun(cpu, "sty $23,X");
        assertEquals((byte) 0x14, cpu.getMemoryValue(new Address(0x26)));
    }

    @Test
    void testStyAbsolute() {
        Cpu cpu = Cpu.builder()
                .y((byte) 0x66)
                .build();
        compileAndRun(cpu, "sty $5432");
        assertEquals((byte) 0x66, cpu.getMemoryValue(new Address(0x5432)));
    }
}
