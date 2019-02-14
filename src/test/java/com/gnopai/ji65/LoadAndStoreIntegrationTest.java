package com.gnopai.ji65;

import com.gnopai.ji65.scanner.ErrorPrinter;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LoadAndStoreIntegrationTest {

    // TODO break these out into tests for each individual opcode, at least for all currently supported addressing modes
    @Test
    void test() {
        Cpu cpu = Cpu.builder().build();
        compileAndRun(cpu,
                "lda #$10",
                "sta $05",
                "ldx $05",
                "stx $08",
                "ldy $08",
                "sty $11"
        );

        byte expectedValue = (byte) 0x10;
        assertEquals(expectedValue, cpu.getAccumulator());
        assertEquals(expectedValue, cpu.getX());
        assertEquals(expectedValue, cpu.getY());
        assertEquals(expectedValue, cpu.getMemoryValue(new Address(0x05)));
        assertEquals(expectedValue, cpu.getMemoryValue(new Address(0x08)));
        assertEquals(expectedValue, cpu.getMemoryValue(new Address(0x11)));
    }

    private void compileAndRun(Cpu cpu, String... lines) {
        String programText = buildProgramText(lines);
        Ji65 ji65 = new Ji65(new ErrorPrinter());
        Program program = ji65.compile(programText);
        ji65.run(program, cpu);
    }

    private String buildProgramText(String... lines) {
        return Arrays.stream(lines)
                .map(line -> line + "\n")
                .collect(Collectors.joining(""));

    }
}
