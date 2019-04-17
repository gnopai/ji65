package com.gnopai.ji65;

import com.gnopai.ji65.interpreter.EndProgramAtAddress;
import com.gnopai.ji65.interpreter.EndProgramAtValue;
import org.junit.jupiter.api.Test;

import static com.gnopai.ji65.TestUtil.assembleAndRun;
import static org.junit.jupiter.api.Assertions.*;

class InstructionFunctionalTest {

    @Test
    void testAdcImmediate() {
        Cpu cpu = Cpu.builder()
                .accumulator((byte) 0x44)
                .build();
        cpu.setCarryFlag(true);
        assembleAndRun(cpu, "adc #$22");
        assertEquals((byte) 0x67, cpu.getAccumulator());
        assertFalse(cpu.isCarryFlagSet());
        assertFalse(cpu.isZeroFlagSet());
        assertFalse(cpu.isNegativeFlagSet());
    }

    @Test
    void testAdcZeroPage() {
        Cpu cpu = Cpu.builder()
                .accumulator((byte) 0x44)
                .build();
        cpu.setCarryFlag(true);
        cpu.setMemoryValue(new Address(0x66), (byte) 0x11);
        assembleAndRun(cpu, "adc $66");
        assertEquals((byte) 0x56, cpu.getAccumulator());
        assertFalse(cpu.isCarryFlagSet());
        assertFalse(cpu.isZeroFlagSet());
        assertFalse(cpu.isNegativeFlagSet());
    }

    @Test
    void testAdcZeroPageX() {
        Cpu cpu = Cpu.builder()
                .accumulator((byte) 0x44)
                .x((byte) 2)
                .build();
        cpu.setCarryFlag(true);
        cpu.setMemoryValue(new Address(0x24), (byte) 0x11);
        assembleAndRun(cpu, "adc $22,X");
        assertEquals((byte) 0x56, cpu.getAccumulator());
        assertFalse(cpu.isCarryFlagSet());
        assertFalse(cpu.isZeroFlagSet());
        assertFalse(cpu.isNegativeFlagSet());
    }

    @Test
    void testAdcAbsolute() {
        Cpu cpu = Cpu.builder()
                .accumulator((byte) 0x44)
                .build();
        cpu.setCarryFlag(true);
        cpu.setMemoryValue(new Address(0x1234), (byte) 0x14);
        assembleAndRun(cpu, "adc $1234");
        assertEquals((byte) 0x59, cpu.getAccumulator());
        assertFalse(cpu.isCarryFlagSet());
        assertFalse(cpu.isZeroFlagSet());
        assertFalse(cpu.isNegativeFlagSet());
    }

    @Test
    void testAdcAbsoluteX() {
        Cpu cpu = Cpu.builder()
                .accumulator((byte) 0x44)
                .x((byte) 3)
                .build();
        cpu.setCarryFlag(true);
        cpu.setMemoryValue(new Address(0x1237), (byte) 0x14);
        assembleAndRun(cpu, "adc $1234,X");
        assertEquals((byte) 0x59, cpu.getAccumulator());
        assertFalse(cpu.isCarryFlagSet());
        assertFalse(cpu.isZeroFlagSet());
        assertFalse(cpu.isNegativeFlagSet());
    }

    @Test
    void testAdcAbsoluteY() {
        Cpu cpu = Cpu.builder()
                .accumulator((byte) 0x44)
                .y((byte) 5)
                .build();
        cpu.setCarryFlag(true);
        cpu.setMemoryValue(new Address(0x1239), (byte) 0x14);
        assembleAndRun(cpu, "adc $1234,Y");
        assertEquals((byte) 0x59, cpu.getAccumulator());
        assertFalse(cpu.isCarryFlagSet());
        assertFalse(cpu.isZeroFlagSet());
        assertFalse(cpu.isNegativeFlagSet());
    }

    @Test
    void testAdcIndexedIndirect() {
        Cpu cpu = Cpu.builder()
                .accumulator((byte) 0x44)
                .x((byte) 5)
                .build();
        cpu.setCarryFlag(true);
        cpu.setMemoryValue(new Address(0x0039), (byte) 0x45);
        cpu.setMemoryValue(new Address(0x003A), (byte) 0x23);
        cpu.setMemoryValue(new Address(0x2345), (byte) 0x66);
        assembleAndRun(cpu, "adc ($34,X)");
        assertEquals((byte) 0xAB, cpu.getAccumulator());
        assertFalse(cpu.isCarryFlagSet());
        assertFalse(cpu.isZeroFlagSet());
        assertTrue(cpu.isNegativeFlagSet());
    }

    @Test
    void testAdcIndirectIndexed() {
        Cpu cpu = Cpu.builder()
                .accumulator((byte) 0x44)
                .y((byte) 4)
                .build();
        cpu.setCarryFlag(true);
        cpu.setMemoryValue(new Address(0x0034), (byte) 0x45);
        cpu.setMemoryValue(new Address(0x0035), (byte) 0x23);
        cpu.setMemoryValue(new Address(0x2349), (byte) 0x33);
        assembleAndRun(cpu, "adc ($34),Y");
        assertEquals((byte) 0x78, cpu.getAccumulator());
        assertFalse(cpu.isCarryFlagSet());
        assertFalse(cpu.isZeroFlagSet());
        assertFalse(cpu.isNegativeFlagSet());
    }

    @Test
    void testAndImmediate() {
        Cpu cpu = Cpu.builder()
                .accumulator((byte) 0x4F)
                .build();
        cpu.setCarryFlag(true);
        assembleAndRun(cpu, "and #$F4");
        assertEquals((byte) 0x44, cpu.getAccumulator());
        assertFalse(cpu.isZeroFlagSet());
        assertFalse(cpu.isNegativeFlagSet());
    }

    @Test
    void testAndZeroPage() {
        Cpu cpu = Cpu.builder()
                .accumulator((byte) 0x4F)
                .build();
        cpu.setCarryFlag(true);
        cpu.setMemoryValue(new Address(0x66), (byte) 0x45);
        assembleAndRun(cpu, "and $66");
        assertEquals((byte) 0x45, cpu.getAccumulator());
        assertFalse(cpu.isZeroFlagSet());
        assertFalse(cpu.isNegativeFlagSet());
    }

    @Test
    void testAndZeroPageX() {
        Cpu cpu = Cpu.builder()
                .accumulator((byte) 0x4F)
                .x((byte) 2)
                .build();
        cpu.setCarryFlag(true);
        cpu.setMemoryValue(new Address(0x24), (byte) 0xF6);
        assembleAndRun(cpu, "and $22,X");
        assertEquals((byte) 0x46, cpu.getAccumulator());
        assertFalse(cpu.isZeroFlagSet());
        assertFalse(cpu.isNegativeFlagSet());
    }

    @Test
    void testAndAbsolute() {
        Cpu cpu = Cpu.builder()
                .accumulator((byte) 0x4F)
                .build();
        cpu.setCarryFlag(true);
        cpu.setMemoryValue(new Address(0x1234), (byte) 0x14);
        assembleAndRun(cpu, "and $1234");
        assertEquals((byte) 0x04, cpu.getAccumulator());
        assertFalse(cpu.isZeroFlagSet());
        assertFalse(cpu.isNegativeFlagSet());
    }

    @Test
    void testAndAbsoluteX() {
        Cpu cpu = Cpu.builder()
                .accumulator((byte) 0x4F)
                .x((byte) 3)
                .build();
        cpu.setCarryFlag(true);
        cpu.setMemoryValue(new Address(0x1237), (byte) 0xF2);
        assembleAndRun(cpu, "and $1234,X");
        assertEquals((byte) 0x42, cpu.getAccumulator());
        assertFalse(cpu.isZeroFlagSet());
        assertFalse(cpu.isNegativeFlagSet());
    }

    @Test
    void testAndAbsoluteY() {
        Cpu cpu = Cpu.builder()
                .accumulator((byte) 0x4F)
                .y((byte) 5)
                .build();
        cpu.setCarryFlag(true);
        cpu.setMemoryValue(new Address(0x1239), (byte) 0xFF);
        assembleAndRun(cpu, "and $1234,Y");
        assertEquals((byte) 0x4F, cpu.getAccumulator());
        assertFalse(cpu.isZeroFlagSet());
        assertFalse(cpu.isNegativeFlagSet());
    }

    @Test
    void testAndIndexedIndirect() {
        Cpu cpu = Cpu.builder()
                .accumulator((byte) 0x4F)
                .x((byte) 5)
                .build();
        cpu.setCarryFlag(true);
        cpu.setMemoryValue(new Address(0x0039), (byte) 0x45);
        cpu.setMemoryValue(new Address(0x003A), (byte) 0x23);
        cpu.setMemoryValue(new Address(0x2345), (byte) 0xF9);
        assembleAndRun(cpu, "and ($34,X)");
        assertEquals((byte) 0x49, cpu.getAccumulator());
        assertFalse(cpu.isZeroFlagSet());
        assertFalse(cpu.isNegativeFlagSet());
    }

    @Test
    void testAndIndirectIndexed() {
        Cpu cpu = Cpu.builder()
                .accumulator((byte) 0x4F)
                .y((byte) 4)
                .build();
        cpu.setCarryFlag(true);
        cpu.setMemoryValue(new Address(0x0034), (byte) 0x45);
        cpu.setMemoryValue(new Address(0x0035), (byte) 0x23);
        cpu.setMemoryValue(new Address(0x2349), (byte) 0x08);
        assembleAndRun(cpu, "and ($34),Y");
        assertEquals((byte) 0x08, cpu.getAccumulator());
        assertFalse(cpu.isZeroFlagSet());
        assertFalse(cpu.isNegativeFlagSet());
    }
    
    @Test
    void testAslAccumulator() {
        Cpu cpu = Cpu.builder()
                .accumulator((byte) 0b11001100)
                .build();
        assembleAndRun(cpu, "asl");
        assertEquals((byte) 0b10011000, cpu.getAccumulator());
        assertTrue(cpu.isCarryFlagSet());
        assertFalse(cpu.isZeroFlagSet());
        assertTrue(cpu.isNegativeFlagSet());
    }

    @Test
    void testAslZeroPage() {
        Cpu cpu = Cpu.builder().build();
        Address address = new Address(0x0034);
        cpu.setMemoryValue(address, (byte) 0b11001100);
        assembleAndRun(cpu, "asl $34");
        assertEquals((byte) 0b10011000, cpu.getMemoryValue(address));
        assertTrue(cpu.isCarryFlagSet());
        assertFalse(cpu.isZeroFlagSet());
        assertTrue(cpu.isNegativeFlagSet());
    }

    @Test
    void testAslZeroPageX() {
        Cpu cpu = Cpu.builder()
                .x((byte) 3)
                .build();
        Address address = new Address(0x0037);
        cpu.setMemoryValue(address, (byte) 0b11001100);
        assembleAndRun(cpu, "asl $34,X");
        assertEquals((byte) 0b10011000, cpu.getMemoryValue(address));
        assertTrue(cpu.isCarryFlagSet());
        assertFalse(cpu.isZeroFlagSet());
        assertTrue(cpu.isNegativeFlagSet());
    }

    @Test
    void testAslAbsolute() {
        Cpu cpu = Cpu.builder().build();
        Address address = new Address(0x1234);
        cpu.setMemoryValue(address, (byte) 0b11001100);
        assembleAndRun(cpu, "asl $1234");
        assertEquals((byte) 0b10011000, cpu.getMemoryValue(address));
        assertTrue(cpu.isCarryFlagSet());
        assertFalse(cpu.isZeroFlagSet());
        assertTrue(cpu.isNegativeFlagSet());
    }

    @Test
    void testAslAbsoluteX() {
        Cpu cpu = Cpu.builder()
                .x((byte) 3)
                .build();
        Address address = new Address(0x1237);
        cpu.setMemoryValue(address, (byte) 0b11001100);
        assembleAndRun(cpu, "asl $1234,X");
        assertEquals((byte) 0b10011000, cpu.getMemoryValue(address));
        assertTrue(cpu.isCarryFlagSet());
        assertFalse(cpu.isZeroFlagSet());
        assertTrue(cpu.isNegativeFlagSet());
    }

    @Test
    void testBrk() {
        byte processorStatus = (byte) 0b01000000;
        Cpu cpu = Cpu.builder()
                .processorStatus(processorStatus)
                .build();
        cpu.setMemoryValue(new Address(0xFFFE), (byte) 0x00);
        cpu.setMemoryValue(new Address(0xFFFF), (byte) 0x90);
        cpu.setMemoryValue(new Address(0x9000), Opcode.SEC_IMPLICIT.getOpcode());
        Address programEndAddress = new Address(0x9001);

        assembleAndRun(cpu, new EndProgramAtAddress(programEndAddress), "brk");
        assertTrue(cpu.isBreakCommandSet());
        assertEquals(0x9001, cpu.getProgramCounter());
        assertEquals(processorStatus, cpu.pullFromStack());
        assertTrue(cpu.isCarryFlagSet());
    }

    @Test
    void testClc() {
        Cpu cpu = Cpu.builder().build();
        cpu.setCarryFlag(true);
        assembleAndRun(cpu, "clc");
        assertFalse(cpu.isCarryFlagSet());
    }

    @Test
    void testCld() {
        Cpu cpu = Cpu.builder().build();
        cpu.setDecimalMode(true);
        assembleAndRun(cpu, "cld");
        assertFalse(cpu.isDecimalModeSet());
    }

    @Test
    void testCli() {
        Cpu cpu = Cpu.builder().build();
        cpu.setInterruptDisable(true);
        assembleAndRun(cpu, "cli");
        assertFalse(cpu.isInterruptDisableSet());
    }

    @Test
    void testClv() {
        Cpu cpu = Cpu.builder().build();
        cpu.setOverflowFlag(true);
        assembleAndRun(cpu, "clv");
        assertFalse(cpu.isOverflowFlagSet());
    }

    @Test
    void testCmpImmediate() {
        Cpu cpu = Cpu.builder()
                .accumulator((byte) 0x45)
                .build();
        assembleAndRun(cpu, "cmp #$44");
        assertTrue(cpu.isCarryFlagSet());
        assertFalse(cpu.isZeroFlagSet());
        assertFalse(cpu.isNegativeFlagSet());
    }

    @Test
    void testCmpZeroPage() {
        Cpu cpu = Cpu.builder()
                .accumulator((byte) 0x45)
                .build();
        cpu.setCarryFlag(true);
        cpu.setMemoryValue(new Address(0x66), (byte) 0x44);
        assembleAndRun(cpu, "cmp $66");
        assertTrue(cpu.isCarryFlagSet());
        assertFalse(cpu.isZeroFlagSet());
        assertFalse(cpu.isNegativeFlagSet());
    }

    @Test
    void testCmpZeroPageX() {
        Cpu cpu = Cpu.builder()
                .accumulator((byte) 0x45)
                .x((byte) 2)
                .build();
        cpu.setCarryFlag(true);
        cpu.setMemoryValue(new Address(0x24), (byte) 0x44);
        assembleAndRun(cpu, "cmp $22,X");
        assertTrue(cpu.isCarryFlagSet());
        assertFalse(cpu.isZeroFlagSet());
        assertFalse(cpu.isNegativeFlagSet());
    }

    @Test
    void testCmpAbsolute() {
        Cpu cpu = Cpu.builder()
                .accumulator((byte) 0x45)
                .build();
        cpu.setCarryFlag(true);
        cpu.setMemoryValue(new Address(0x1234), (byte) 0x44);
        assembleAndRun(cpu, "cmp $1234");
        assertTrue(cpu.isCarryFlagSet());
        assertFalse(cpu.isZeroFlagSet());
        assertFalse(cpu.isNegativeFlagSet());
    }

    @Test
    void testCmpAbsoluteX() {
        Cpu cpu = Cpu.builder()
                .accumulator((byte) 0x45)
                .x((byte) 3)
                .build();
        cpu.setCarryFlag(true);
        cpu.setMemoryValue(new Address(0x1237), (byte) 0x44);
        assembleAndRun(cpu, "cmp $1234,X");
        assertTrue(cpu.isCarryFlagSet());
        assertFalse(cpu.isZeroFlagSet());
        assertFalse(cpu.isNegativeFlagSet());
    }

    @Test
    void testCmpAbsoluteY() {
        Cpu cpu = Cpu.builder()
                .accumulator((byte) 0x45)
                .y((byte) 5)
                .build();
        cpu.setCarryFlag(true);
        cpu.setMemoryValue(new Address(0x1239), (byte) 0x44);
        assembleAndRun(cpu, "cmp $1234,Y");
        assertTrue(cpu.isCarryFlagSet());
        assertFalse(cpu.isZeroFlagSet());
        assertFalse(cpu.isNegativeFlagSet());
    }

    @Test
    void testCmpIndexedIndirect() {
        Cpu cpu = Cpu.builder()
                .accumulator((byte) 0x45)
                .x((byte) 5)
                .build();
        cpu.setCarryFlag(true);
        cpu.setMemoryValue(new Address(0x0039), (byte) 0x45);
        cpu.setMemoryValue(new Address(0x003A), (byte) 0x23);
        cpu.setMemoryValue(new Address(0x2345), (byte) 0x44);
        assembleAndRun(cpu, "cmp ($34,X)");
        assertTrue(cpu.isCarryFlagSet());
        assertFalse(cpu.isZeroFlagSet());
        assertFalse(cpu.isNegativeFlagSet());
    }

    @Test
    void testCmpIndirectIndexed() {
        Cpu cpu = Cpu.builder()
                .accumulator((byte) 0x45)
                .y((byte) 4)
                .build();
        cpu.setCarryFlag(true);
        cpu.setMemoryValue(new Address(0x0034), (byte) 0x45);
        cpu.setMemoryValue(new Address(0x0035), (byte) 0x23);
        cpu.setMemoryValue(new Address(0x2349), (byte) 0x44);
        assembleAndRun(cpu, "cmp ($34),Y");
        assertTrue(cpu.isCarryFlagSet());
        assertFalse(cpu.isZeroFlagSet());
        assertFalse(cpu.isNegativeFlagSet());
    }

    @Test
    void testCpxImmediate() {
        Cpu cpu = Cpu.builder()
                .x((byte) 0x45)
                .build();
        assembleAndRun(cpu, "cpx #$44");
        assertTrue(cpu.isCarryFlagSet());
        assertFalse(cpu.isZeroFlagSet());
        assertFalse(cpu.isNegativeFlagSet());
    }

    @Test
    void testCpxZeroPage() {
        Cpu cpu = Cpu.builder()
                .x((byte) 0x45)
                .build();
        cpu.setMemoryValue(new Address(0x0022), (byte) 0x44);
        assembleAndRun(cpu, "cpx $22");
        assertTrue(cpu.isCarryFlagSet());
        assertFalse(cpu.isZeroFlagSet());
        assertFalse(cpu.isNegativeFlagSet());
    }

    @Test
    void testCpxAbsolute() {
        Cpu cpu = Cpu.builder()
                .x((byte) 0x45)
                .build();
        cpu.setMemoryValue(new Address(0x1234), (byte) 0x44);
        assembleAndRun(cpu, "cpx $1234");
        assertTrue(cpu.isCarryFlagSet());
        assertFalse(cpu.isZeroFlagSet());
        assertFalse(cpu.isNegativeFlagSet());
    }

    @Test
    void testCpyImmediate() {
        Cpu cpu = Cpu.builder()
                .y((byte) 0x45)
                .build();
        assembleAndRun(cpu, "cpy #$44");
        assertTrue(cpu.isCarryFlagSet());
        assertFalse(cpu.isZeroFlagSet());
        assertFalse(cpu.isNegativeFlagSet());
    }

    @Test
    void testCpyZeroPage() {
        Cpu cpu = Cpu.builder()
                .y((byte) 0x45)
                .build();
        cpu.setMemoryValue(new Address(0x0022), (byte) 0x44);
        assembleAndRun(cpu, "cpy $22");
        assertTrue(cpu.isCarryFlagSet());
        assertFalse(cpu.isZeroFlagSet());
        assertFalse(cpu.isNegativeFlagSet());
    }

    @Test
    void testCpyAbsolute() {
        Cpu cpu = Cpu.builder()
                .y((byte) 0x45)
                .build();
        cpu.setMemoryValue(new Address(0x1234), (byte) 0x44);
        assembleAndRun(cpu, "cpy $1234");
        assertTrue(cpu.isCarryFlagSet());
        assertFalse(cpu.isZeroFlagSet());
        assertFalse(cpu.isNegativeFlagSet());
    }

    @Test
    void testDecZeroPage() {
        Cpu cpu = Cpu.builder().build();
        Address address = new Address(0x0035);
        cpu.setMemoryValue(address, (byte) 0x78);
        assembleAndRun(cpu, "dec $35");
        assertEquals((byte) 0x77, cpu.getMemoryValue(address));
        assertFalse(cpu.isZeroFlagSet());
        assertFalse(cpu.isNegativeFlagSet());
    }

    @Test
    void testDecZeroPageX() {
        Cpu cpu = Cpu.builder()
                .x((byte) 7)
                .build();
        Address address = new Address(0x0039);
        cpu.setMemoryValue(address, (byte) 0x78);
        assembleAndRun(cpu, "dec $32,X");
        assertEquals((byte) 0x77, cpu.getMemoryValue(address));
        assertFalse(cpu.isZeroFlagSet());
        assertFalse(cpu.isNegativeFlagSet());
    }

    @Test
    void testDecAbsolute() {
        Cpu cpu = Cpu.builder().build();
        Address address = new Address(0x1234);
        cpu.setMemoryValue(address, (byte) 0x78);
        assembleAndRun(cpu, "dec $1234");
        assertEquals((byte) 0x77, cpu.getMemoryValue(address));
        assertFalse(cpu.isZeroFlagSet());
        assertFalse(cpu.isNegativeFlagSet());
    }

    @Test
    void testDecAbsoluteX() {
        Cpu cpu = Cpu.builder()
                .x((byte) 7)
                .build();
        Address address = new Address(0x1239);
        cpu.setMemoryValue(address, (byte) 0x78);
        assembleAndRun(cpu, "dec $1232,X");
        assertEquals((byte) 0x77, cpu.getMemoryValue(address));
        assertFalse(cpu.isZeroFlagSet());
        assertFalse(cpu.isNegativeFlagSet());
    }

    @Test
    void testDex() {
        Cpu cpu = Cpu.builder()
                .x((byte) 0x77)
                .build();
        assembleAndRun(cpu, "dex");
        assertEquals((byte) 0x76, cpu.getX());
        assertFalse(cpu.isZeroFlagSet());
        assertFalse(cpu.isNegativeFlagSet());
    }

    @Test
    void testDey() {
        Cpu cpu = Cpu.builder()
                .y((byte) 0x77)
                .build();
        assembleAndRun(cpu, "dey");
        assertEquals((byte) 0x76, cpu.getY());
        assertFalse(cpu.isZeroFlagSet());
        assertFalse(cpu.isNegativeFlagSet());
    }

    @Test
    void testEorImmediate() {
        Cpu cpu = Cpu.builder()
                .accumulator((byte) 0xFF)
                .build();
        cpu.setCarryFlag(true);
        assembleAndRun(cpu, "eor #$85");
        assertEquals((byte) 0x7A, cpu.getAccumulator());
        assertFalse(cpu.isZeroFlagSet());
        assertFalse(cpu.isNegativeFlagSet());
    }

    @Test
    void testEorZeroPage() {
        Cpu cpu = Cpu.builder()
                .accumulator((byte) 0xFF)
                .build();
        cpu.setCarryFlag(true);
        cpu.setMemoryValue(new Address(0x66), (byte) 0x85);
        assembleAndRun(cpu, "eor $66");
        assertEquals((byte) 0x7A, cpu.getAccumulator());
        assertFalse(cpu.isZeroFlagSet());
        assertFalse(cpu.isNegativeFlagSet());
    }

    @Test
    void testEorZeroPageX() {
        Cpu cpu = Cpu.builder()
                .accumulator((byte) 0xFF)
                .x((byte) 2)
                .build();
        cpu.setCarryFlag(true);
        cpu.setMemoryValue(new Address(0x24), (byte) 0x85);
        assembleAndRun(cpu, "eor $22,X");
        assertEquals((byte) 0x7A, cpu.getAccumulator());
        assertFalse(cpu.isZeroFlagSet());
        assertFalse(cpu.isNegativeFlagSet());
    }

    @Test
    void testEorAbsolute() {
        Cpu cpu = Cpu.builder()
                .accumulator((byte) 0xFF)
                .build();
        cpu.setCarryFlag(true);
        cpu.setMemoryValue(new Address(0x1234), (byte) 0x85);
        assembleAndRun(cpu, "eor $1234");
        assertEquals((byte) 0x7A, cpu.getAccumulator());
        assertFalse(cpu.isZeroFlagSet());
        assertFalse(cpu.isNegativeFlagSet());
    }

    @Test
    void testEorAbsoluteX() {
        Cpu cpu = Cpu.builder()
                .accumulator((byte) 0xFF)
                .x((byte) 3)
                .build();
        cpu.setCarryFlag(true);
        cpu.setMemoryValue(new Address(0x1237), (byte) 0x85);
        assembleAndRun(cpu, "eor $1234,X");
        assertEquals((byte) 0x7A, cpu.getAccumulator());
        assertFalse(cpu.isZeroFlagSet());
        assertFalse(cpu.isNegativeFlagSet());
    }

    @Test
    void testEorAbsoluteY() {
        Cpu cpu = Cpu.builder()
                .accumulator((byte) 0xFF)
                .y((byte) 5)
                .build();
        cpu.setCarryFlag(true);
        cpu.setMemoryValue(new Address(0x1239), (byte) 0x85);
        assembleAndRun(cpu, "eor $1234,Y");
        assertEquals((byte) 0x7A, cpu.getAccumulator());
        assertFalse(cpu.isZeroFlagSet());
        assertFalse(cpu.isNegativeFlagSet());
    }

    @Test
    void testEorIndexedIndirect() {
        Cpu cpu = Cpu.builder()
                .accumulator((byte) 0xFF)
                .x((byte) 5)
                .build();
        cpu.setCarryFlag(true);
        cpu.setMemoryValue(new Address(0x0039), (byte) 0x45);
        cpu.setMemoryValue(new Address(0x003A), (byte) 0x23);
        cpu.setMemoryValue(new Address(0x2345), (byte) 0x85);
        assembleAndRun(cpu, "eor ($34,X)");
        assertEquals((byte) 0x7A, cpu.getAccumulator());
        assertFalse(cpu.isZeroFlagSet());
        assertFalse(cpu.isNegativeFlagSet());
    }

    @Test
    void testEorIndirectIndexed() {
        Cpu cpu = Cpu.builder()
                .accumulator((byte) 0xFF)
                .y((byte) 4)
                .build();
        cpu.setCarryFlag(true);
        cpu.setMemoryValue(new Address(0x0034), (byte) 0x45);
        cpu.setMemoryValue(new Address(0x0035), (byte) 0x23);
        cpu.setMemoryValue(new Address(0x2349), (byte) 0x85);
        assembleAndRun(cpu, "eor ($34),Y");
        assertEquals((byte) 0x7A, cpu.getAccumulator());
        assertFalse(cpu.isZeroFlagSet());
        assertFalse(cpu.isNegativeFlagSet());
    }

    @Test
    void testIncZeroPage() {
        Cpu cpu = Cpu.builder().build();
        Address address = new Address(0x0035);
        cpu.setMemoryValue(address, (byte) 0x78);
        assembleAndRun(cpu, "inc $35");
        assertEquals((byte) 0x79, cpu.getMemoryValue(address));
        assertFalse(cpu.isZeroFlagSet());
        assertFalse(cpu.isNegativeFlagSet());
    }

    @Test
    void testIncZeroPageX() {
        Cpu cpu = Cpu.builder()
                .x((byte) 7)
                .build();
        Address address = new Address(0x0039);
        cpu.setMemoryValue(address, (byte) 0x78);
        assembleAndRun(cpu, "inc $32,X");
        assertEquals((byte) 0x79, cpu.getMemoryValue(address));
        assertFalse(cpu.isZeroFlagSet());
        assertFalse(cpu.isNegativeFlagSet());
    }

    @Test
    void testIncAbsolute() {
        Cpu cpu = Cpu.builder().build();
        Address address = new Address(0x1234);
        cpu.setMemoryValue(address, (byte) 0x78);
        assembleAndRun(cpu, "inc $1234");
        assertEquals((byte) 0x79, cpu.getMemoryValue(address));
        assertFalse(cpu.isZeroFlagSet());
        assertFalse(cpu.isNegativeFlagSet());
    }

    @Test
    void testIncAbsoluteX() {
        Cpu cpu = Cpu.builder()
                .x((byte) 7)
                .build();
        Address address = new Address(0x1239);
        cpu.setMemoryValue(address, (byte) 0x78);
        assembleAndRun(cpu, "inc $1232,X");
        assertEquals((byte) 0x79, cpu.getMemoryValue(address));
        assertFalse(cpu.isZeroFlagSet());
        assertFalse(cpu.isNegativeFlagSet());
    }

    @Test
    void testInx() {
        Cpu cpu = Cpu.builder()
                .x((byte) 0x77)
                .build();
        assembleAndRun(cpu, "inx");
        assertEquals((byte) 0x78, cpu.getX());
        assertFalse(cpu.isZeroFlagSet());
        assertFalse(cpu.isNegativeFlagSet());
    }

    @Test
    void testIny() {
        Cpu cpu = Cpu.builder()
                .y((byte) 0x77)
                .build();
        assembleAndRun(cpu, "iny");
        assertEquals((byte) 0x78, cpu.getY());
        assertFalse(cpu.isZeroFlagSet());
        assertFalse(cpu.isNegativeFlagSet());
    }

    @Test
    void testJmpAbsolute() {
        Cpu cpu = Cpu.builder().build();
        assembleAndRun(cpu, "jmp $1234");
        assertEquals(0x1234, cpu.getProgramCounter());
    }

    @Test
    void testJmpIndirect() {
        Cpu cpu = Cpu.builder().build();
        cpu.setMemoryValue(new Address(0x1256), (byte) 0x4A);
        cpu.setMemoryValue(new Address(0x1257), (byte) 0x56);
        assembleAndRun(cpu, "jmp ($1256)");
        assertEquals(0x564A, cpu.getProgramCounter());
    }

    @Test
    void testJsr() {
        Cpu cpu = Cpu.builder().build();
        assembleAndRun(cpu, "jsr derp", "sec", "sei", "derp:");
        assertFalse(cpu.isInterruptDisableSet());
        assertFalse(cpu.isCarryFlagSet());
        assertEquals((byte) 0xFD, cpu.getStackPointer());
    }

    @Test
    void testLdaImmediate() {
        Cpu cpu = Cpu.builder().build();
        assembleAndRun(cpu, "lda #$10");
        assertEquals((byte) 0x10, cpu.getAccumulator());
    }

    @Test
    void testLdaZeroPage() {
        Cpu cpu = Cpu.builder().build();
        cpu.setMemoryValue(new Address(8), (byte) 0x17);
        assembleAndRun(cpu, "lda $08");
        assertEquals((byte) 0x17, cpu.getAccumulator());
    }

    @Test
    void testLdaZeroPageX() {
        Cpu cpu = Cpu.builder().build();
        cpu.setMemoryValue(new Address(8), (byte) 0x19);
        cpu.setX((byte) 4);
        assembleAndRun(cpu, "lda $04,X");
        assertEquals((byte) 0x19, cpu.getAccumulator());
    }

    @Test
    void testLdaAbsolute() {
        Cpu cpu = Cpu.builder().build();
        cpu.setMemoryValue(new Address(0x5432), (byte) 0x17);
        assembleAndRun(cpu, "lda $5432");
        assertEquals((byte) 0x17, cpu.getAccumulator());
    }

    @Test
    void testLdaAbsoluteX() {
        Cpu cpu = Cpu.builder().build();
        cpu.setX((byte) 7);
        cpu.setMemoryValue(new Address(0x5439), (byte) 0x17);
        assembleAndRun(cpu, "lda $5432,X");
        assertEquals((byte) 0x17, cpu.getAccumulator());
    }

    @Test
    void testLdaAbsoluteY() {
        Cpu cpu = Cpu.builder().build();
        cpu.setY((byte) 6);
        cpu.setMemoryValue(new Address(0x5436), (byte) 0x12);
        assembleAndRun(cpu, "lda $5430,Y");
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
        assembleAndRun(cpu, "lda ($45,X)");
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
        assembleAndRun(cpu, "lda ($45),Y");
        assertEquals((byte) 0xBC, cpu.getAccumulator());
    }

    @Test
    void testLdxImmediate() {
        Cpu cpu = Cpu.builder().build();
        assembleAndRun(cpu, "ldx #$10");
        assertEquals((byte) 0x10, cpu.getX());
    }

    @Test
    void testLdxZeroPage() {
        Cpu cpu = Cpu.builder().build();
        cpu.setMemoryValue(new Address(8), (byte) 0x17);
        assembleAndRun(cpu, "ldx $08");
        assertEquals((byte) 0x17, cpu.getX());
    }

    @Test
    void testLdxZeroPageY() {
        Cpu cpu = Cpu.builder().build();
        cpu.setMemoryValue(new Address(7), (byte) 0x19);
        cpu.setY((byte) 3);
        assembleAndRun(cpu, "ldx $04,y");
        assertEquals((byte) 0x19, cpu.getX());
    }

    @Test
    void testLdxAbsolute() {
        Cpu cpu = Cpu.builder().build();
        cpu.setMemoryValue(new Address(0x5432), (byte) 0x17);
        assembleAndRun(cpu, "ldx $5432");
        assertEquals((byte) 0x17, cpu.getX());
    }

    @Test
    void testLdxAbsoluteY() {
        Cpu cpu = Cpu.builder().build();
        cpu.setY((byte) 6);
        cpu.setMemoryValue(new Address(0x5436), (byte) 0x12);
        assembleAndRun(cpu, "ldx $5430,Y");
        assertEquals((byte) 0x12, cpu.getX());
    }

    @Test
    void testLdyImmediate() {
        Cpu cpu = Cpu.builder().build();
        assembleAndRun(cpu, "ldy #$10");
        assertEquals((byte) 0x10, cpu.getY());
    }

    @Test
    void testLdyZeroPage() {
        Cpu cpu = Cpu.builder().build();
        cpu.setMemoryValue(new Address(8), (byte) 0x17);
        assembleAndRun(cpu, "ldy $08");
        assertEquals((byte) 0x17, cpu.getY());
    }

    @Test
    void testLdyZeroPageX() {
        Cpu cpu = Cpu.builder().build();
        cpu.setMemoryValue(new Address(8), (byte) 0x19);
        cpu.setX((byte) 4);
        assembleAndRun(cpu, "ldy $04,X");
        assertEquals((byte) 0x19, cpu.getY());
    }

    @Test
    void testLdyAbsolute() {
        Cpu cpu = Cpu.builder().build();
        cpu.setMemoryValue(new Address(0x5432), (byte) 0x17);
        assembleAndRun(cpu, "ldy $5432");
        assertEquals((byte) 0x17, cpu.getY());
    }

    @Test
    void testLdyAbsoluteX() {
        Cpu cpu = Cpu.builder().build();
        cpu.setX((byte) 6);
        cpu.setMemoryValue(new Address(0x5436), (byte) 0x12);
        assembleAndRun(cpu, "ldy $5430,X");
        assertEquals((byte) 0x12, cpu.getY());
    }

    @Test
    void testLsrAccumulator() {
        Cpu cpu = Cpu.builder()
                .accumulator((byte) 0b11001101)
                .build();
        assembleAndRun(cpu, "lsr");
        assertEquals((byte) 0b01100110, cpu.getAccumulator());
        assertTrue(cpu.isCarryFlagSet());
        assertFalse(cpu.isZeroFlagSet());
        assertFalse(cpu.isNegativeFlagSet());
    }

    @Test
    void testLsrZeroPage() {
        Cpu cpu = Cpu.builder().build();
        Address address = new Address(0x0034);
        cpu.setMemoryValue(address, (byte) 0b11001101);
        assembleAndRun(cpu, "lsr $34");
        assertEquals((byte) 0b01100110, cpu.getMemoryValue(address));
        assertTrue(cpu.isCarryFlagSet());
        assertFalse(cpu.isZeroFlagSet());
        assertFalse(cpu.isNegativeFlagSet());
    }

    @Test
    void testLsrZeroPageX() {
        Cpu cpu = Cpu.builder()
                .x((byte) 3)
                .build();
        Address address = new Address(0x0037);
        cpu.setMemoryValue(address, (byte) 0b11001101);
        assembleAndRun(cpu, "lsr $34,X");
        assertEquals((byte) 0b01100110, cpu.getMemoryValue(address));
        assertTrue(cpu.isCarryFlagSet());
        assertFalse(cpu.isZeroFlagSet());
        assertFalse(cpu.isNegativeFlagSet());
    }

    @Test
    void testLsrAbsolute() {
        Cpu cpu = Cpu.builder().build();
        Address address = new Address(0x1234);
        cpu.setMemoryValue(address, (byte) 0b11001101);
        assembleAndRun(cpu, "lsr $1234");
        assertEquals((byte) 0b01100110, cpu.getMemoryValue(address));
        assertTrue(cpu.isCarryFlagSet());
        assertFalse(cpu.isZeroFlagSet());
        assertFalse(cpu.isNegativeFlagSet());
    }

    @Test
    void testLsrAbsoluteX() {
        Cpu cpu = Cpu.builder()
                .x((byte) 3)
                .build();
        Address address = new Address(0x1237);
        cpu.setMemoryValue(address, (byte) 0b11001101);
        assembleAndRun(cpu, "lsr $1234,X");
        assertEquals((byte) 0b01100110, cpu.getMemoryValue(address));
        assertTrue(cpu.isCarryFlagSet());
        assertFalse(cpu.isZeroFlagSet());
        assertFalse(cpu.isNegativeFlagSet());
    }

    @Test
    void testOraImmediate() {
        Cpu cpu = Cpu.builder()
                .accumulator((byte) 0x4F)
                .build();
        cpu.setCarryFlag(true);
        assembleAndRun(cpu, "ora #$74");
        assertEquals((byte) 0x7F, cpu.getAccumulator());
        assertFalse(cpu.isZeroFlagSet());
        assertFalse(cpu.isNegativeFlagSet());
    }

    @Test
    void testOraZeroPage() {
        Cpu cpu = Cpu.builder()
                .accumulator((byte) 0x4F)
                .build();
        cpu.setCarryFlag(true);
        cpu.setMemoryValue(new Address(0x66), (byte) 0x45);
        assembleAndRun(cpu, "ora $66");
        assertEquals((byte) 0x4F, cpu.getAccumulator());
        assertFalse(cpu.isZeroFlagSet());
        assertFalse(cpu.isNegativeFlagSet());
    }

    @Test
    void testOraZeroPageX() {
        Cpu cpu = Cpu.builder()
                .accumulator((byte) 0x42)
                .x((byte) 2)
                .build();
        cpu.setCarryFlag(true);
        cpu.setMemoryValue(new Address(0x24), (byte) 0xF6);
        assembleAndRun(cpu, "ora $22,X");
        assertEquals((byte) 0xF6, cpu.getAccumulator());
        assertFalse(cpu.isZeroFlagSet());
        assertTrue(cpu.isNegativeFlagSet());
    }

    @Test
    void testOraAbsolute() {
        Cpu cpu = Cpu.builder()
                .accumulator((byte) 0x22)
                .build();
        cpu.setCarryFlag(true);
        cpu.setMemoryValue(new Address(0x1234), (byte) 0x11);
        assembleAndRun(cpu, "ora $1234");
        assertEquals((byte) 0x33, cpu.getAccumulator());
        assertFalse(cpu.isZeroFlagSet());
        assertFalse(cpu.isNegativeFlagSet());
    }

    @Test
    void testOraAbsoluteX() {
        Cpu cpu = Cpu.builder()
                .accumulator((byte) 0x0F)
                .x((byte) 3)
                .build();
        cpu.setCarryFlag(true);
        cpu.setMemoryValue(new Address(0x1237), (byte) 0x72);
        assembleAndRun(cpu, "ora $1234,X");
        assertEquals((byte) 0x7F, cpu.getAccumulator());
        assertFalse(cpu.isZeroFlagSet());
        assertFalse(cpu.isNegativeFlagSet());
    }

    @Test
    void testOraAbsoluteY() {
        Cpu cpu = Cpu.builder()
                .accumulator((byte) 0x19)
                .y((byte) 5)
                .build();
        cpu.setCarryFlag(true);
        cpu.setMemoryValue(new Address(0x1239), (byte) 0x2F);
        assembleAndRun(cpu, "ora $1234,Y");
        assertEquals((byte) 0x3F, cpu.getAccumulator());
        assertFalse(cpu.isZeroFlagSet());
        assertFalse(cpu.isNegativeFlagSet());
    }

    @Test
    void testOraIndexedIndirect() {
        Cpu cpu = Cpu.builder()
                .accumulator((byte) 0x4F)
                .x((byte) 5)
                .build();
        cpu.setCarryFlag(true);
        cpu.setMemoryValue(new Address(0x0039), (byte) 0x45);
        cpu.setMemoryValue(new Address(0x003A), (byte) 0x23);
        cpu.setMemoryValue(new Address(0x2345), (byte) 0x29);
        assembleAndRun(cpu, "ora ($34,X)");
        assertEquals((byte) 0x6F, cpu.getAccumulator());
        assertFalse(cpu.isZeroFlagSet());
        assertFalse(cpu.isNegativeFlagSet());
    }

    @Test
    void testOraIndirectIndexed() {
        Cpu cpu = Cpu.builder()
                .accumulator((byte) 0x22)
                .y((byte) 4)
                .build();
        cpu.setCarryFlag(true);
        cpu.setMemoryValue(new Address(0x0034), (byte) 0x45);
        cpu.setMemoryValue(new Address(0x0035), (byte) 0x23);
        cpu.setMemoryValue(new Address(0x2349), (byte) 0x19);
        assembleAndRun(cpu, "ora ($34),Y");
        assertEquals((byte) 0x3B, cpu.getAccumulator());
        assertFalse(cpu.isZeroFlagSet());
        assertFalse(cpu.isNegativeFlagSet());
    }

    @Test
    void testPha() {
        byte value = (byte) 0x48;
        Cpu cpu = Cpu.builder()
                .accumulator(value)
                .build();
        assembleAndRun(cpu, "pha");
        assertEquals(value, cpu.getMemoryValue(new Address(0x01FF)));
        assertEquals(value, cpu.pullFromStack());
    }

    @Test
    void testPhp() {
        byte value = (byte) 0x48;
        Cpu cpu = Cpu.builder()
                .processorStatus(value)
                .build();
        assembleAndRun(cpu, "php");
        assertEquals(value, cpu.getMemoryValue(new Address(0x01FF)));
        assertEquals(value, cpu.pullFromStack());
    }

    @Test
    void testPla() {
        byte value = (byte) 0x88;
        Cpu cpu = Cpu.builder().build();
        cpu.pushOntoStack(value);
        assembleAndRun(cpu, "pla");
        assertEquals(value, cpu.getAccumulator());
        assertFalse(cpu.isZeroFlagSet());
        assertTrue(cpu.isNegativeFlagSet());
    }

    @Test
    void testPlp() {
        Cpu cpu = Cpu.builder()
                .processorStatus((byte) 0xFF)
                .build();
        byte value = (byte) 0x88;
        cpu.pushOntoStack(value);
        assembleAndRun(cpu, "plp");
        assertEquals(value, cpu.getProcessorStatus());
    }

    @Test
    void testRolAccumulator() {
        Cpu cpu = Cpu.builder()
                .accumulator((byte) 0b11001100)
                .build();
        cpu.setCarryFlag(true);
        assembleAndRun(cpu, "rol");
        assertEquals((byte) 0b10011001, cpu.getAccumulator());
        assertTrue(cpu.isCarryFlagSet());
        assertFalse(cpu.isZeroFlagSet());
        assertTrue(cpu.isNegativeFlagSet());
    }

    @Test
    void testRolZeroPage() {
        Cpu cpu = Cpu.builder().build();
        cpu.setCarryFlag(true);
        Address address = new Address(0x0034);
        cpu.setMemoryValue(address, (byte) 0b11001100);
        assembleAndRun(cpu, "rol $34");
        assertEquals((byte) 0b10011001, cpu.getMemoryValue(address));
        assertTrue(cpu.isCarryFlagSet());
        assertFalse(cpu.isZeroFlagSet());
        assertTrue(cpu.isNegativeFlagSet());
    }

    @Test
    void testRolZeroPageX() {
        Cpu cpu = Cpu.builder()
                .x((byte) 3)
                .build();
        cpu.setCarryFlag(true);
        Address address = new Address(0x0037);
        cpu.setMemoryValue(address, (byte) 0b11001100);
        assembleAndRun(cpu, "rol $34,X");
        assertEquals((byte) 0b10011001, cpu.getMemoryValue(address));
        assertTrue(cpu.isCarryFlagSet());
        assertFalse(cpu.isZeroFlagSet());
        assertTrue(cpu.isNegativeFlagSet());
    }

    @Test
    void testRolAbsolute() {
        Cpu cpu = Cpu.builder().build();
        cpu.setCarryFlag(true);
        Address address = new Address(0x1234);
        cpu.setMemoryValue(address, (byte) 0b11001100);
        assembleAndRun(cpu, "rol $1234");
        assertEquals((byte) 0b10011001, cpu.getMemoryValue(address));
        assertTrue(cpu.isCarryFlagSet());
        assertFalse(cpu.isZeroFlagSet());
        assertTrue(cpu.isNegativeFlagSet());
    }

    @Test
    void testRolAbsoluteX() {
        Cpu cpu = Cpu.builder()
                .x((byte) 3)
                .build();
        cpu.setCarryFlag(true);
        Address address = new Address(0x1237);
        cpu.setMemoryValue(address, (byte) 0b11001100);
        assembleAndRun(cpu, "rol $1234,X");
        assertEquals((byte) 0b10011001, cpu.getMemoryValue(address));
        assertTrue(cpu.isCarryFlagSet());
        assertFalse(cpu.isZeroFlagSet());
        assertTrue(cpu.isNegativeFlagSet());
    }

    @Test
    void testRorAccumulator() {
        Cpu cpu = Cpu.builder()
                .accumulator((byte) 0b11001101)
                .build();
        cpu.setCarryFlag(true);
        assembleAndRun(cpu, "ror");
        assertEquals((byte) 0b11100110, cpu.getAccumulator());
        assertTrue(cpu.isCarryFlagSet());
        assertFalse(cpu.isZeroFlagSet());
        assertTrue(cpu.isNegativeFlagSet());
    }

    @Test
    void testRorZeroPage() {
        Cpu cpu = Cpu.builder().build();
        cpu.setCarryFlag(true);
        Address address = new Address(0x0034);
        cpu.setMemoryValue(address, (byte) 0b11001101);
        assembleAndRun(cpu, "ror $34");
        assertEquals((byte) 0b11100110, cpu.getMemoryValue(address));
        assertTrue(cpu.isCarryFlagSet());
        assertFalse(cpu.isZeroFlagSet());
        assertTrue(cpu.isNegativeFlagSet());
    }

    @Test
    void testRorZeroPageX() {
        Cpu cpu = Cpu.builder()
                .x((byte) 3)
                .build();
        cpu.setCarryFlag(true);
        Address address = new Address(0x0037);
        cpu.setMemoryValue(address, (byte) 0b11001101);
        assembleAndRun(cpu, "ror $34,X");
        assertEquals((byte) 0b11100110, cpu.getMemoryValue(address));
        assertTrue(cpu.isCarryFlagSet());
        assertFalse(cpu.isZeroFlagSet());
        assertTrue(cpu.isNegativeFlagSet());
    }

    @Test
    void testRorAbsolute() {
        Cpu cpu = Cpu.builder().build();
        cpu.setCarryFlag(true);
        Address address = new Address(0x1234);
        cpu.setMemoryValue(address, (byte) 0b11001101);
        assembleAndRun(cpu, "ror $1234");
        assertEquals((byte) 0b11100110, cpu.getMemoryValue(address));
        assertTrue(cpu.isCarryFlagSet());
        assertFalse(cpu.isZeroFlagSet());
        assertTrue(cpu.isNegativeFlagSet());
    }

    @Test
    void testRorAbsoluteX() {
        Cpu cpu = Cpu.builder()
                .x((byte) 3)
                .build();
        cpu.setCarryFlag(true);
        Address address = new Address(0x1237);
        cpu.setMemoryValue(address, (byte) 0b11001101);
        assembleAndRun(cpu, "ror $1234,X");
        assertEquals((byte) 0b11100110, cpu.getMemoryValue(address));
        assertTrue(cpu.isCarryFlagSet());
        assertFalse(cpu.isZeroFlagSet());
        assertTrue(cpu.isNegativeFlagSet());
    }

    @Test
    void testRts() {
        Cpu cpu = Cpu.builder().build();
        assembleAndRun(cpu,
                "jsr derp",
                "jmp end",
                "sei",
                "derp:",
                "sec",
                "rts",
                "sed",
                "end:"
        );
        assertFalse(cpu.isInterruptDisableSet());
        assertTrue(cpu.isCarryFlagSet());
        assertFalse(cpu.isInterruptDisableSet()); // sei skipped over
        assertFalse(cpu.isDecimalModeSet()); // sed skipped over
        assertEquals((byte) 0xFF, cpu.getStackPointer());
    }

    @Test
    void testRti() {
        Cpu cpu = Cpu.builder().build();
        cpu.setMemoryValue(new Address(0xFFFE), (byte) 0x00);
        cpu.setMemoryValue(new Address(0xFFFF), (byte) 0x90);

        byte accumulatorValue = (byte) 0xAB;
        cpu.setMemoryValue(new Address(0x9000), Opcode.LDA_IMMEDIATE.getOpcode());
        cpu.setMemoryValue(new Address(0x9001), accumulatorValue);
        cpu.setMemoryValue(new Address(0x9002), Opcode.CLC_IMPLICIT.getOpcode());
        cpu.setMemoryValue(new Address(0x9003), Opcode.RTI_IMPLICIT.getOpcode());

        assembleAndRun(cpu, new EndProgramAtValue((byte) 0xFF), "sec", "brk", "sed", ".byte $FF");
        assertFalse(cpu.isBreakCommandSet()); // cleared after break
        assertTrue(cpu.isCarryFlagSet()); // flags should get restored from pre-break
        assertTrue(cpu.isDecimalModeSet()); // set after break
        assertEquals(accumulatorValue, cpu.getAccumulator()); // set during break
    }

    @Test
    void testSbcImmediate() {
        Cpu cpu = Cpu.builder()
                .accumulator((byte) 0x44)
                .build();
        assembleAndRun(cpu, "sbc #$11");
        assertEquals((byte) 0x32, cpu.getAccumulator());
        assertTrue(cpu.isCarryFlagSet());
        assertFalse(cpu.isZeroFlagSet());
        assertFalse(cpu.isNegativeFlagSet());
    }

    @Test
    void testSbcZeroPage() {
        Cpu cpu = Cpu.builder()
                .accumulator((byte) 0x44)
                .build();
        cpu.setMemoryValue(new Address(0x66), (byte) 0x11);
        assembleAndRun(cpu, "sbc $66");
        assertEquals((byte) 0x32, cpu.getAccumulator());
        assertTrue(cpu.isCarryFlagSet());
        assertFalse(cpu.isZeroFlagSet());
        assertFalse(cpu.isNegativeFlagSet());
    }

    @Test
    void testSbcZeroPageX() {
        Cpu cpu = Cpu.builder()
                .accumulator((byte) 0x44)
                .x((byte) 2)
                .build();
        cpu.setMemoryValue(new Address(0x24), (byte) 0x11);
        assembleAndRun(cpu, "sbc $22,X");
        assertEquals((byte) 0x32, cpu.getAccumulator());
        assertTrue(cpu.isCarryFlagSet());
        assertFalse(cpu.isZeroFlagSet());
        assertFalse(cpu.isNegativeFlagSet());
    }

    @Test
    void testSbcAbsolute() {
        Cpu cpu = Cpu.builder()
                .accumulator((byte) 0x44)
                .build();
        cpu.setMemoryValue(new Address(0x1234), (byte) 0x14);
        assembleAndRun(cpu, "sbc $1234");
        assertEquals((byte) 0x2F, cpu.getAccumulator());
        assertTrue(cpu.isCarryFlagSet());
        assertFalse(cpu.isZeroFlagSet());
        assertFalse(cpu.isNegativeFlagSet());
    }

    @Test
    void testSbcAbsoluteX() {
        Cpu cpu = Cpu.builder()
                .accumulator((byte) 0x44)
                .x((byte) 3)
                .build();
        cpu.setMemoryValue(new Address(0x1237), (byte) 0x14);
        assembleAndRun(cpu, "sbc $1234,X");
        assertEquals((byte) 0x2F, cpu.getAccumulator());
        assertTrue(cpu.isCarryFlagSet());
        assertFalse(cpu.isZeroFlagSet());
        assertFalse(cpu.isNegativeFlagSet());
    }

    @Test
    void testSbcAbsoluteY() {
        Cpu cpu = Cpu.builder()
                .accumulator((byte) 0x44)
                .y((byte) 5)
                .build();
        cpu.setMemoryValue(new Address(0x1239), (byte) 0x14);
        assembleAndRun(cpu, "sbc $1234,Y");
        assertEquals((byte) 0x2F, cpu.getAccumulator());
        assertTrue(cpu.isCarryFlagSet());
        assertFalse(cpu.isZeroFlagSet());
        assertFalse(cpu.isNegativeFlagSet());
    }

    @Test
    void testSbcIndexedIndirect() {
        Cpu cpu = Cpu.builder()
                .accumulator((byte) 0x44)
                .x((byte) 5)
                .build();
        cpu.setMemoryValue(new Address(0x0039), (byte) 0x45);
        cpu.setMemoryValue(new Address(0x003A), (byte) 0x23);
        cpu.setMemoryValue(new Address(0x2345), (byte) 0x12);
        assembleAndRun(cpu, "sbc ($34,X)");
        assertEquals((byte) 0x31, cpu.getAccumulator());
        assertTrue(cpu.isCarryFlagSet());
        assertFalse(cpu.isZeroFlagSet());
        assertFalse(cpu.isNegativeFlagSet());
    }

    @Test
    void testSbcIndirectIndexed() {
        Cpu cpu = Cpu.builder()
                .accumulator((byte) 0x44)
                .y((byte) 4)
                .build();
        cpu.setMemoryValue(new Address(0x0034), (byte) 0x45);
        cpu.setMemoryValue(new Address(0x0035), (byte) 0x23);
        cpu.setMemoryValue(new Address(0x2349), (byte) 0x33);
        assembleAndRun(cpu, "sbc ($34),Y");
        assertEquals((byte) 0x10, cpu.getAccumulator());
        assertTrue(cpu.isCarryFlagSet());
        assertFalse(cpu.isZeroFlagSet());
        assertFalse(cpu.isNegativeFlagSet());
    }

    @Test
    void testSec() {
        Cpu cpu = Cpu.builder().build();
        cpu.setCarryFlag(false);
        assembleAndRun(cpu, "sec");
        assertTrue(cpu.isCarryFlagSet());
    }

    @Test
    void testSed() {
        Cpu cpu = Cpu.builder().build();
        cpu.setDecimalMode(false);
        assembleAndRun(cpu, "sed");
        assertTrue(cpu.isDecimalModeSet());
    }

    @Test
    void testSei() {
        Cpu cpu = Cpu.builder().build();
        cpu.setInterruptDisable(false);
        assembleAndRun(cpu, "sei");
        assertTrue(cpu.isInterruptDisableSet());
    }

    @Test
    void testStaZeroPage() {
        Cpu cpu = Cpu.builder()
                .accumulator((byte) 0x14)
                .build();
        assembleAndRun(cpu, "sta $23");
        assertEquals((byte) 0x14, cpu.getMemoryValue(new Address(0x23)));
    }

    @Test
    void testStaZeroPageX() {
        Cpu cpu = Cpu.builder()
                .accumulator((byte) 0x14)
                .x((byte) 3)
                .build();
        assembleAndRun(cpu, "sta $23,X");
        assertEquals((byte) 0x14, cpu.getMemoryValue(new Address(0x26)));
    }

    @Test
    void testStaAbsolute() {
        Cpu cpu = Cpu.builder()
                .accumulator((byte) 0x14)
                .build();
        assembleAndRun(cpu, "sta $2346");
        assertEquals((byte) 0x14, cpu.getMemoryValue(new Address(0x2346)));
    }

    @Test
    void testStaAbsoluteX() {
        Cpu cpu = Cpu.builder()
                .accumulator((byte) 0x14)
                .x((byte) 0x10)
                .build();
        assembleAndRun(cpu, "sta $2346,X");
        assertEquals((byte) 0x14, cpu.getMemoryValue(new Address(0x2356)));
    }

    @Test
    void testStaAbsoluteY() {
        Cpu cpu = Cpu.builder()
                .accumulator((byte) 0x15)
                .y((byte) 0x11)
                .build();
        assembleAndRun(cpu, "sta $2346,Y");
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
        assembleAndRun(cpu, "sta ($45,X)");
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
        assembleAndRun(cpu, "sta ($45),Y");
        assertEquals((byte) 0xB9, cpu.getMemoryValue(new Address(0x4189)));
    }

    @Test
    void testStxZeroPage() {
        Cpu cpu = Cpu.builder()
                .x((byte) 0x14)
                .build();
        assembleAndRun(cpu, "stx $23");
        assertEquals((byte) 0x14, cpu.getMemoryValue(new Address(0x23)));
    }

    @Test
    void testStxZeroPageY() {
        Cpu cpu = Cpu.builder()
                .x((byte) 0x14)
                .y((byte) 3)
                .build();
        assembleAndRun(cpu, "stx $23,y");
        assertEquals((byte) 0x14, cpu.getMemoryValue(new Address(0x26)));
    }

    @Test
    void testStxAbsolute() {
        Cpu cpu = Cpu.builder()
                .x((byte) 0x14)
                .build();
        assembleAndRun(cpu, "stx $2310");
        assertEquals((byte) 0x14, cpu.getMemoryValue(new Address(0x2310)));
    }

    @Test
    void testStyZeroPage() {
        Cpu cpu = Cpu.builder()
                .y((byte) 0x14)
                .build();
        assembleAndRun(cpu, "sty $23");
        assertEquals((byte) 0x14, cpu.getMemoryValue(new Address(0x23)));
    }

    @Test
    void testStyZeroPageX() {
        Cpu cpu = Cpu.builder()
                .y((byte) 0x14)
                .x((byte) 3)
                .build();
        assembleAndRun(cpu, "sty $23,X");
        assertEquals((byte) 0x14, cpu.getMemoryValue(new Address(0x26)));
    }

    @Test
    void testStyAbsolute() {
        Cpu cpu = Cpu.builder()
                .y((byte) 0x66)
                .build();
        assembleAndRun(cpu, "sty $5432");
        assertEquals((byte) 0x66, cpu.getMemoryValue(new Address(0x5432)));
    }

    @Test
    void testTax() {
        byte value = (byte) 0xAB;
        Cpu cpu = Cpu.builder()
                .accumulator(value)
                .x((byte) 0)
                .build();
        assembleAndRun(cpu, "tax");
        assertEquals(value, cpu.getAccumulator());
        assertEquals(value, cpu.getX());
    }

    @Test
    void testTay() {
        byte value = (byte) 0xAB;
        Cpu cpu = Cpu.builder()
                .accumulator(value)
                .y((byte) 0)
                .build();
        assembleAndRun(cpu, "tay");
        assertEquals(value, cpu.getAccumulator());
        assertEquals(value, cpu.getY());
    }

    @Test
    void testTsx() {
        Cpu cpu = Cpu.builder()
                .x((byte) 0)
                .build();
        assembleAndRun(cpu, "tsx");
        assertEquals((byte) 0xFF, cpu.getStackPointer());
        assertEquals((byte) 0xFF, cpu.getX());
    }

    @Test
    void testTxa() {
        byte value = (byte) 0xAB;
        Cpu cpu = Cpu.builder()
                .x(value)
                .accumulator((byte) 0)
                .build();
        assembleAndRun(cpu, "txa");
        assertEquals(value, cpu.getX());
        assertEquals(value, cpu.getAccumulator());
    }

    @Test
    void testTxs() {
        byte value = (byte) 0xF0;
        Cpu cpu = Cpu.builder()
                .x(value)
                .build();
        assembleAndRun(cpu, "txs");
        assertEquals(value, cpu.getX());
        assertEquals(value, cpu.getStackPointer());
    }

    @Test
    void testTya() {
        byte value = (byte) 0xAB;
        Cpu cpu = Cpu.builder()
                .y(value)
                .accumulator((byte) 0)
                .build();
        assembleAndRun(cpu, "tya");
        assertEquals(value, cpu.getY());
        assertEquals(value, cpu.getAccumulator());
    }
}
