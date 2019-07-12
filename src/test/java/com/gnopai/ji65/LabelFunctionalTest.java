package com.gnopai.ji65;

import org.junit.jupiter.api.Test;

import static com.gnopai.ji65.TestUtil.DEFAULT_PROGRAM_START_ADDRESS;
import static com.gnopai.ji65.TestUtil.assembleAndRun;
import static org.junit.jupiter.api.Assertions.*;

class LabelFunctionalTest {
    @Test
    void testSimpleLabelJump() {
        Cpu cpu = Cpu.builder().build();
        assembleAndRun(cpu,
                "begin:",
                "lda #11",
                "ldx #12",
                "ldy #13",
                "jmp end",
                "middle:",
                "lda #21",
                "ldx #22",
                "ldy #23",
                "end:"
        );
        assertEquals(11, cpu.getAccumulator());
        assertEquals(12, cpu.getX());
        assertEquals(13, cpu.getY());
    }

    @Test
    void testLabelBytes() {
        Cpu cpu = Cpu.builder().build();
        assembleAndRun(cpu,
                "sec",
                "whee:",
                "ldx #<whee",
                "ldy #>whee"
        );
        assertEquals((byte) 0x01, cpu.getX());
        assertEquals((byte) 0x80, cpu.getY());
    }

    @Test
    void testLabelZeroPageEvaluation() {
        Cpu cpu = Cpu.builder().build();
        assembleAndRun(cpu,
                ".segment \"ZEROPAGE\"",
                "lda #0",
                "zeroey:",
                "lda #1",

                ".segment \"CODE\"",
                "lda zeroey", // should become an LDA zero-page
                "lda not_zeroey", // should become an LDA absolute

                ".segment \"RODATA\"",
                "not_zeroey:",
                "ldx #11"
        );

        assertEquals(Opcode.LDA_ZERO_PAGE.getOpcode(), cpu.getMemoryValue(DEFAULT_PROGRAM_START_ADDRESS));
        Address nextAddress = DEFAULT_PROGRAM_START_ADDRESS.plus(Opcode.LDA_ZERO_PAGE.getByteCount());
        assertEquals(Opcode.LDA_ABSOLUTE.getOpcode(), cpu.getMemoryValue(nextAddress));
    }

    @Test
    void testProgramCounterReference() {
        Cpu cpu = Cpu.builder().build();
        assembleAndRun(cpu,
                "sec",
                "ldx #<*",
                "ldy #<*",
                "lda #>*"
        );
        Address startAddress = DEFAULT_PROGRAM_START_ADDRESS;
        assertEquals(startAddress.plus(1).getLowByte(), cpu.getX());
        assertEquals(startAddress.plus(3).getLowByte(), cpu.getY());
        assertEquals(startAddress.plus(5).getHighByte(), cpu.getAccumulator());
    }

    @Test
    void testBranchWithPositiveOffset() {
        Cpu cpu = Cpu.builder().build();
        assembleAndRun(cpu,
                "bcc end",
                "sec",
                "end:",
                "sed"
        );
        assertTrue(cpu.isDecimalModeSet());
        assertFalse(cpu.isCarryFlagSet());
    }

    @Test
    void testBranchWithNegativeOffset() {
        Cpu cpu = Cpu.builder().build();
        assembleAndRun(cpu,
                "jmp one",
                "sed",
                "two:",
                "sec",
                "one:",
                "bcc two"
        );
        assertFalse(cpu.isDecimalModeSet());
        assertTrue(cpu.isCarryFlagSet());
    }

    @Test
    void testLocalLabels() {
        Cpu cpu = Cpu.builder().build();
        assembleAndRun(cpu,
                "begin:",
                "jmp @derp",
                "lda #1",
                "@derp:",
                "ldx #2",
                "middle:",
                "jmp @derp",
                "lda #1",
                "@derp:",
                "ldy #3",
                "end:"
        );
        assertEquals(0, cpu.getAccumulator());
        assertEquals(2, cpu.getX());
        assertEquals(3, cpu.getY());
    }

    @Test
    void testUnnamedLabels() {
        Cpu cpu = Cpu.builder().build();
        assembleAndRun(cpu,
                "begin:",
                "ldx #7",
                "ldy #0",
                ": txa", "iny", "dex", "bne :-",
                "lda #0",
                "beq :+",
                "sec",
                ":"
        );
        assertEquals(0, cpu.getX());
        assertEquals(7, cpu.getY());
        assertFalse(cpu.isCarryFlagSet());
    }
}
