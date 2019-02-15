package com.gnopai.ji65;

import org.junit.jupiter.api.Test;

import static com.gnopai.ji65.TestUtil.compileAndRun;
import static org.junit.jupiter.api.Assertions.assertEquals;

class InstructionFunctionalTest {

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
}
