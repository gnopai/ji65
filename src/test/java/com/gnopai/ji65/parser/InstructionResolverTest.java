package com.gnopai.ji65.parser;

import com.gnopai.ji65.BytesValue;
import com.gnopai.ji65.Opcode;
import com.gnopai.ji65.RuntimeInstruction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class InstructionResolverTest {
    private final NumberParser numberParser = mock(NumberParser.class);
    private InstructionResolver testClass;

    @BeforeEach
    void setUp() {
        testClass = new InstructionResolver(numberParser);
    }

    @Test
    void testExplicitArgument() {
        // GIVEN
        ParsingData parsingData = new ParsingData();
        String argument = "63";
        when(numberParser.parseValue(argument)).thenReturn(Optional.of(63));
        Opcode opcode = Opcode.LDA_ZERO_PAGE;
        UnresolvedInstruction unresolvedInstruction = new UnresolvedInstruction(opcode, argument);

        // WHEN
        RuntimeInstruction result = testClass.resolve(unresolvedInstruction, parsingData);

        // THEN
        RuntimeInstruction expectedResult = new RuntimeInstruction(opcode, new BytesValue((byte) 0x3F));
        assertEquals(expectedResult, result);
    }

    // TODO actually resolve the name
    @Test
    void testNamedArgument() {
        // GIVEN
        ParsingData parsingData = new ParsingData();
        String argument = "foo";
        when(numberParser.parseValue(argument)).thenReturn(Optional.empty());
        Opcode opcode = Opcode.LDA_ZERO_PAGE;
        UnresolvedInstruction unresolvedInstruction = new UnresolvedInstruction(opcode, argument);

        // WHEN
        RuntimeInstruction result = testClass.resolve(unresolvedInstruction, parsingData);

        // THEN
        RuntimeInstruction expectedResult = new RuntimeInstruction(opcode, new BytesValue((byte) 0x00));
        assertEquals(expectedResult, result);
    }
}