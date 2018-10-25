package com.gnopai.ji65.parser;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static com.gnopai.ji65.Opcode.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class InstructionParserTest {
    private final NumberParser numberParser = mock(NumberParser.class);
    private final ParsingData parsingData = mock(ParsingData.class);
    private InstructionParser testClass;

    @BeforeEach
    public void setUp() {
        testClass = new InstructionParser(numberParser);
    }

    @Test
    public void testUnrecognizedInstructionType() {
        Optional<UnresolvedInstruction> instruction = testClass.parseInstruction("DERP $55", parsingData);
        assertFalse(instruction.isPresent());
    }

    @Test
    public void testImplicit() {
        Optional<UnresolvedInstruction> instruction = testClass.parseInstruction(" TAX ", parsingData);
        UnresolvedInstruction expectedInstruction = new UnresolvedInstruction(TAX_IMPLICIT, null);
        assertTrue(instruction.isPresent());
        assertEquals(expectedInstruction, instruction.get());
    }

    @Test
    public void testAccumulator() {
        Optional<UnresolvedInstruction> instruction = testClass.parseInstruction(" ASL  A ", parsingData);
        UnresolvedInstruction expectedInstruction = new UnresolvedInstruction(ASL_ACCUMULATOR, null);
        assertTrue(instruction.isPresent());
        assertEquals(expectedInstruction, instruction.get());
    }

    @Test
    public void testRelative_symbol() {
        when(parsingData.isAddressLabel("thing")).thenReturn(true);
        Optional<UnresolvedInstruction> instruction = testClass.parseInstruction(" BNE thing ", parsingData);
        UnresolvedInstruction expectedInstruction = new UnresolvedInstruction(BNE_RELATIVE, "thing");
        assertTrue(instruction.isPresent());
        assertEquals(expectedInstruction, instruction.get());
    }

    @Test
    public void testRelative_explicitPositive() {
        Optional<UnresolvedInstruction> instruction = testClass.parseInstruction(" BNE *+4 ", parsingData);
        UnresolvedInstruction expectedInstruction = new UnresolvedInstruction(BNE_RELATIVE, "*+4");
        assertTrue(instruction.isPresent());
        assertEquals(expectedInstruction, instruction.get());
    }

    @Test
    public void testRelative_explicitNegative() {
        Optional<UnresolvedInstruction> instruction = testClass.parseInstruction(" BNE *-8 ", parsingData);
        UnresolvedInstruction expectedInstruction = new UnresolvedInstruction(BNE_RELATIVE, "*-8");
        assertTrue(instruction.isPresent());
        assertEquals(expectedInstruction, instruction.get());
    }

    @Test
    public void testRelative_invalidJunk() {
        Exception exception = assertThrows(RuntimeException.class, () ->
                testClass.parseInstruction(" BNE --8 ", parsingData)
        );
        assertEquals("Invalid instruction argument: '--8'", exception.getMessage());
    }

    @Test
    public void testImmediate_value() {
        when(numberParser.isValidValue("$BB")).thenReturn(true);
        Optional<UnresolvedInstruction> instruction = testClass.parseInstruction(" LDA #$BB ", parsingData);
        UnresolvedInstruction expectedInstruction = new UnresolvedInstruction(LDA_IMMEDIATE, "$BB");
        assertTrue(instruction.isPresent());
        assertEquals(expectedInstruction, instruction.get());
    }

    @Test
    public void testImmediate_symbol() {
        when(parsingData.isConstant("THING")).thenReturn(true);
        Optional<UnresolvedInstruction> instruction = testClass.parseInstruction(" LDA #THING ", parsingData);
        UnresolvedInstruction expectedInstruction = new UnresolvedInstruction(LDA_IMMEDIATE, "THING");
        assertTrue(instruction.isPresent());
        assertEquals(expectedInstruction, instruction.get());
    }

    @Test
    public void testZeroPage_value() {
        when(numberParser.isZeroPageValue("$F5")).thenReturn(true);
        Optional<UnresolvedInstruction> instruction = testClass.parseInstruction(" LDA  $F5 ", parsingData);
        UnresolvedInstruction expectedInstruction = new UnresolvedInstruction(LDA_ZERO_PAGE, "$F5");
        assertTrue(instruction.isPresent());
        assertEquals(expectedInstruction, instruction.get());
    }

    @Test
    public void testZeroPage_symbol() {
        when(numberParser.isZeroPageValue("THING")).thenReturn(true);
        when(parsingData.isZeroPageSymbol("THING")).thenReturn(true);
        Optional<UnresolvedInstruction> instruction = testClass.parseInstruction(" LDA THING ", parsingData);
        UnresolvedInstruction expectedInstruction = new UnresolvedInstruction(LDA_ZERO_PAGE, "THING");
        assertTrue(instruction.isPresent());
        assertEquals(expectedInstruction, instruction.get());
    }

    @Test
    public void testAbsolute_value() {
        when(numberParser.isAbsoluteValue("$55A2")).thenReturn(true);
        Optional<UnresolvedInstruction> instruction = testClass.parseInstruction(" LDA  $55A2 ", parsingData);
        UnresolvedInstruction expectedInstruction = new UnresolvedInstruction(LDA_ABSOLUTE, "$55A2");
        assertTrue(instruction.isPresent());
        assertEquals(expectedInstruction, instruction.get());
    }

    @Test
    public void testAbsolute_symbol() {
        when(parsingData.isAddressLabel("THING")).thenReturn(true);
        Optional<UnresolvedInstruction> instruction = testClass.parseInstruction(" LDA THING ", parsingData);
        UnresolvedInstruction expectedInstruction = new UnresolvedInstruction(LDA_ABSOLUTE, "THING");
        assertTrue(instruction.isPresent());
        assertEquals(expectedInstruction, instruction.get());
    }

    @Test
    public void testZeroPageXIndexed_value() {
        when(numberParser.isZeroPageValue("$55")).thenReturn(true);
        Optional<UnresolvedInstruction> instruction = testClass.parseInstruction(" LDA $55,X ", parsingData);
        UnresolvedInstruction expectedInstruction = new UnresolvedInstruction(LDA_ZERO_PAGE_X, "$55");
        assertTrue(instruction.isPresent());
        assertEquals(expectedInstruction, instruction.get());
    }

    @Test
    public void testZeroPageXIndexed_symbol() {
        when(parsingData.isZeroPageSymbol("thing")).thenReturn(true);
        Optional<UnresolvedInstruction> instruction = testClass.parseInstruction(" LDA thing,X ", parsingData);
        UnresolvedInstruction expectedInstruction = new UnresolvedInstruction(LDA_ZERO_PAGE_X, "thing");
        assertTrue(instruction.isPresent());
        assertEquals(expectedInstruction, instruction.get());
    }

    @Test
    public void testZeroPageYIndexed_value() {
        when(numberParser.isZeroPageValue("$55")).thenReturn(true);
        Optional<UnresolvedInstruction> instruction = testClass.parseInstruction(" LDX $55, Y ", parsingData);
        UnresolvedInstruction expectedInstruction = new UnresolvedInstruction(LDX_ZERO_PAGE_Y, "$55");
        assertTrue(instruction.isPresent());
        assertEquals(expectedInstruction, instruction.get());
    }

    @Test
    public void testZeroPageYIndexed_symbol() {
        when(parsingData.isZeroPageSymbol("thing")).thenReturn(true);
        Optional<UnresolvedInstruction> instruction = testClass.parseInstruction(" LDX thing,Y ", parsingData);
        UnresolvedInstruction expectedInstruction = new UnresolvedInstruction(LDX_ZERO_PAGE_Y, "thing");
        assertTrue(instruction.isPresent());
        assertEquals(expectedInstruction, instruction.get());
    }

    @Test
    public void testAbsoluteXIndexed_value() {
        when(numberParser.isAbsoluteValue("$4455")).thenReturn(true);
        Optional<UnresolvedInstruction> instruction = testClass.parseInstruction(" LDA $4455,X ", parsingData);
        UnresolvedInstruction expectedInstruction = new UnresolvedInstruction(LDA_ABSOLUTE_X, "$4455");
        assertTrue(instruction.isPresent());
        assertEquals(expectedInstruction, instruction.get());
    }

    @Test
    public void testAbsoluteXIndexed_symbol() {
        when(parsingData.isAddressLabel("thing")).thenReturn(true);
        Optional<UnresolvedInstruction> instruction = testClass.parseInstruction(" LDA thing,X ", parsingData);
        UnresolvedInstruction expectedInstruction = new UnresolvedInstruction(LDA_ABSOLUTE_X, "thing");
        assertTrue(instruction.isPresent());
        assertEquals(expectedInstruction, instruction.get());
    }

    @Test
    public void testAbsoluteYIndexed_value() {
        when(numberParser.isAbsoluteValue("$4455")).thenReturn(true);
        Optional<UnresolvedInstruction> instruction = testClass.parseInstruction(" LDX $4455, Y ", parsingData);
        UnresolvedInstruction expectedInstruction = new UnresolvedInstruction(LDX_ABSOLUTE_Y, "$4455");
        assertTrue(instruction.isPresent());
        assertEquals(expectedInstruction, instruction.get());
    }

    @Test
    public void testAbsoluteYIndexed_symbol() {
        when(parsingData.isAddressLabel("thing")).thenReturn(true);
        Optional<UnresolvedInstruction> instruction = testClass.parseInstruction(" LDX thing,Y ", parsingData);
        UnresolvedInstruction expectedInstruction = new UnresolvedInstruction(LDX_ABSOLUTE_Y, "thing");
        assertTrue(instruction.isPresent());
        assertEquals(expectedInstruction, instruction.get());
    }

    @Test
    public void testIndirectIndexed_value() {
        when(numberParser.isAbsoluteValue("$4455")).thenReturn(true);
        Optional<UnresolvedInstruction> instruction = testClass.parseInstruction(" LDA ($4455), Y ", parsingData);
        UnresolvedInstruction expectedInstruction = new UnresolvedInstruction(LDA_INDIRECT_INDEXED, "$4455");
        assertTrue(instruction.isPresent());
        assertEquals(expectedInstruction, instruction.get());
    }

    @Test
    public void testIndirectIndexed_symbol() {
        when(parsingData.isAddressLabel("thing")).thenReturn(true);
        Optional<UnresolvedInstruction> instruction = testClass.parseInstruction(" LDA (thing), Y ", parsingData);
        UnresolvedInstruction expectedInstruction = new UnresolvedInstruction(LDA_INDIRECT_INDEXED, "thing");
        assertTrue(instruction.isPresent());
        assertEquals(expectedInstruction, instruction.get());
    }

    @Test
    public void testIndexedXModeJunk() {
        Exception exception = assertThrows(RuntimeException.class, () ->
                testClass.parseInstruction("LDX derp,X", parsingData)
        );
        assertEquals("Invalid instruction argument: 'derp,X'", exception.getMessage());
    }

    @Test
    public void testIndexedYModeJunk() {
        Exception exception = assertThrows(RuntimeException.class, () ->
                testClass.parseInstruction("LDX derp,Y", parsingData)
        );
        assertEquals("Invalid instruction argument: 'derp,Y'", exception.getMessage());
    }

    @Test
    public void testIndirectIndexedJunk() {
        Exception exception = assertThrows(RuntimeException.class, () ->
                testClass.parseInstruction("LDA (derp),Y", parsingData)
        );
        assertEquals("Invalid instruction argument: '(derp),Y'", exception.getMessage());
    }

    @Test
    public void testIndirect_value() {
        when(numberParser.isAbsoluteValue("$1234")).thenReturn(true);
        Optional<UnresolvedInstruction> instruction = testClass.parseInstruction(" JMP ($1234) ", parsingData);
        UnresolvedInstruction expectedInstruction = new UnresolvedInstruction(JMP_INDIRECT, "$1234");
        assertTrue(instruction.isPresent());
        assertEquals(expectedInstruction, instruction.get());
    }

    @Test
    public void testIndirect_symbol() {
        when(parsingData.isAddressLabel("thing")).thenReturn(true);
        Optional<UnresolvedInstruction> instruction = testClass.parseInstruction(" JMP (thing) ", parsingData);
        UnresolvedInstruction expectedInstruction = new UnresolvedInstruction(JMP_INDIRECT, "thing");
        assertTrue(instruction.isPresent());
        assertEquals(expectedInstruction, instruction.get());
    }

    @Test
    public void testIndexedIndirect_value() {
        when(numberParser.isAbsoluteValue("$1234")).thenReturn(true);
        Optional<UnresolvedInstruction> instruction = testClass.parseInstruction(" LDA ($1234,X) ", parsingData);
        UnresolvedInstruction expectedInstruction = new UnresolvedInstruction(LDA_INDEXED_INDIRECT, "$1234");
        assertTrue(instruction.isPresent());
        assertEquals(expectedInstruction, instruction.get());
    }

    @Test
    public void testIndexedIndirect_symbol() {
        when(parsingData.isAddressLabel("thing")).thenReturn(true);
        Optional<UnresolvedInstruction> instruction = testClass.parseInstruction(" LDA (thing,X) ", parsingData);
        UnresolvedInstruction expectedInstruction = new UnresolvedInstruction(LDA_INDEXED_INDIRECT, "thing");
        assertTrue(instruction.isPresent());
        assertEquals(expectedInstruction, instruction.get());
    }

    @Test
    public void testIndexedIndirect_invalidJunk() {
        Exception exception = assertThrows(RuntimeException.class, () ->
                testClass.parseInstruction(" LDA (derp,X) ", parsingData)
        );
        assertEquals("Invalid instruction argument: '(derp,X)'", exception.getMessage());
    }

    @Test
    public void testIndirect_invalidJunk() {
        Exception exception = assertThrows(RuntimeException.class, () ->
                testClass.parseInstruction(" JMP (derp) ", parsingData)
        );
        assertEquals("Invalid instruction argument: '(derp)'", exception.getMessage());
    }

    @Test
    public void testInvalidAddressingMode() {
        Exception exception = assertThrows(RuntimeException.class, () ->
                testClass.parseInstruction("LDA )-:", parsingData)
        );
        assertEquals("Invalid instruction argument: ')-:'", exception.getMessage());
    }

    @Test
    public void testInvalidOpcode() {
        when(numberParser.isZeroPageValue("$55")).thenReturn(true);
        Exception exception = assertThrows(RuntimeException.class, () ->
                testClass.parseInstruction("TAX $55", parsingData)
        );
        assertEquals("Invalid instruction line: 'TAX $55'", exception.getMessage());
    }
}