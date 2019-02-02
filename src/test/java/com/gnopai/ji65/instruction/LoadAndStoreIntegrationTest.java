package com.gnopai.ji65.instruction;

import com.gnopai.ji65.Address;
import com.gnopai.ji65.Cpu;
import com.gnopai.ji65.Program;
import com.gnopai.ji65.ProgramRunner;
import com.gnopai.ji65.address.AddressingModeFactory;
import com.gnopai.ji65.parser.InstructionParser;
import com.gnopai.ji65.parser.InstructionResolver;
import com.gnopai.ji65.parser.NumberParser;
import com.gnopai.ji65.parser.ProgramParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LoadAndStoreIntegrationTest {
    private ProgramParser programParser;
    private ProgramRunner programRunner;

    @BeforeEach
    void setUp() {
        NumberParser numberParser = new NumberParser();
        programParser = new ProgramParser(new InstructionParser(numberParser), new InstructionResolver(numberParser));
        programRunner = new ProgramRunner(new AddressingModeFactory(), new InstructionFactory());
    }

    // TODO break these out into tests for each individual opcode, at least for all currently supported addressing modes
    @Test
    void test() {
        List<String> rawInstructions = List.of(
                "lda #$10",
                "sta $05",
                "ldx $05",
                "stx $08",
                "ldy $08",
                "sty $11"
        );

        Program program = programParser.parseProgram(rawInstructions);
        Cpu cpu = Cpu.builder().build();
        programRunner.run(cpu, program);

        byte expectedValue = (byte) 0x10;
        assertEquals(expectedValue, cpu.getAccumulator());
        assertEquals(expectedValue, cpu.getX());
        assertEquals(expectedValue, cpu.getY());
        assertEquals(expectedValue, cpu.getMemoryValue(new Address(0x05)));
        assertEquals(expectedValue, cpu.getMemoryValue(new Address(0x08)));
        assertEquals(expectedValue, cpu.getMemoryValue(new Address(0x11)));
    }
}
