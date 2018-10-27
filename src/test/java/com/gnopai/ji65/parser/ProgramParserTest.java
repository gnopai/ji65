package com.gnopai.ji65.parser;

import com.gnopai.ji65.BytesValue;
import com.gnopai.ji65.Program;
import com.gnopai.ji65.RuntimeInstruction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static com.gnopai.ji65.Opcode.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ProgramParserTest {
    private final InstructionParser instructionParser = mock(InstructionParser.class);
    private final InstructionResolver instructionResolver = mock(InstructionResolver.class);
    private ProgramParser testClass;

    @BeforeEach
    void setUp() {
        testClass = new ProgramParser(instructionParser, instructionResolver);
    }

    @Test
    void test() {
        // GIVEN
        String rawInstruction1 = "lda $05";
        UnresolvedInstruction unresolvedInstruction1 = new UnresolvedInstruction(LDA_ZERO_PAGE, "$05");
        when(instructionParser.parseInstruction(eq(rawInstruction1), any(ParsingData.class)))
                .thenReturn(Optional.of(unresolvedInstruction1));
        RuntimeInstruction runtimeInstruction1 = new RuntimeInstruction(LDA_ZERO_PAGE, new BytesValue((byte) 0x05));
        when(instructionResolver.resolve(eq(unresolvedInstruction1), any(ParsingData.class)))
                .thenReturn(runtimeInstruction1);

        String rawInstruction2 = "sta $07";
        UnresolvedInstruction unresolvedInstruction2 = new UnresolvedInstruction(STA_ZERO_PAGE, "$07");
        when(instructionParser.parseInstruction(eq(rawInstruction2), any(ParsingData.class)))
                .thenReturn(Optional.of(unresolvedInstruction2));
        RuntimeInstruction runtimeInstruction2 = new RuntimeInstruction(STA_ZERO_PAGE, new BytesValue((byte) 0x07));
        when(instructionResolver.resolve(eq(unresolvedInstruction2), any(ParsingData.class)))
                .thenReturn(runtimeInstruction2);

        String rawInstruction3 = "ldx $10";
        UnresolvedInstruction unresolvedInstruction3 = new UnresolvedInstruction(LDX_ZERO_PAGE, "$10");
        when(instructionParser.parseInstruction(eq(rawInstruction3), any(ParsingData.class)))
                .thenReturn(Optional.of(unresolvedInstruction3));
        RuntimeInstruction runtimeInstruction3 = new RuntimeInstruction(LDX_ZERO_PAGE, new BytesValue((byte) 0x10));
        when(instructionResolver.resolve(eq(unresolvedInstruction3), any(ParsingData.class)))
                .thenReturn(runtimeInstruction3);

        // WHEN
        Program program = testClass.parseProgram(List.of(rawInstruction1, rawInstruction2, rawInstruction3));

        // THEN
        Program expectedProgram = new Program(List.of(runtimeInstruction1, runtimeInstruction2, runtimeInstruction3));
        assertEquals(expectedProgram, program);
    }
}