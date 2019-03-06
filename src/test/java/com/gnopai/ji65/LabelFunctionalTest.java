package com.gnopai.ji65;

import org.junit.jupiter.api.Test;

import static com.gnopai.ji65.TestUtil.assembleAndRun;
import static org.junit.jupiter.api.Assertions.assertEquals;

class LabelFunctionalTest {
    @Test
    void testSimpleLabelJump() {
        Cpu cpu = Cpu.builder().build();
        assembleAndRun(cpu,
                "start:",
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
}
