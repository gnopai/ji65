package com.gnopai.ji65.compiler;

import com.gnopai.ji65.Opcode;
import com.gnopai.ji65.address.AddressingModeType;
import com.gnopai.ji65.instruction.InstructionType;
import com.gnopai.ji65.parser.expression.PrimaryExpression;
import com.gnopai.ji65.parser.statement.InstructionStatement;
import com.gnopai.ji65.scanner.TokenType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class InstructionCompilerTest {
    private final ExpressionEvaluator expressionEvaluator = mock(ExpressionEvaluator.class);

    @Test
    void testImplicit() {
        testSingleByteOpcode(InstructionType.CLC, AddressingModeType.IMPLICIT, Opcode.CLC_IMPLICIT);
    }

    @Test
    void testAccumulator() {
        testSingleByteOpcode(InstructionType.ROL, AddressingModeType.ACCUMULATOR, Opcode.ROL_ACCUMULATOR);
    }

    @Test
    void testImmediate() {
        testTwoByteOpcode(InstructionType.LDA, AddressingModeType.IMMEDIATE, Opcode.LDA_IMMEDIATE);
    }

    @Test
    void testZeroPage() {
        testTwoByteOpcode(InstructionType.LDA, AddressingModeType.ZERO_PAGE, Opcode.LDA_ZERO_PAGE);
    }

    @Test
    void testZeroPageX() {
        testTwoByteOpcode(InstructionType.LDA, AddressingModeType.ZERO_PAGE_X, Opcode.LDA_ZERO_PAGE_X);
    }

    @Test
    void testZeroPageY() {
        testTwoByteOpcode(InstructionType.LDX, AddressingModeType.ZERO_PAGE_Y, Opcode.LDX_ZERO_PAGE_Y);
    }

    @Test
    void testRelative() {
        testTwoByteOpcode(InstructionType.BNE, AddressingModeType.RELATIVE, Opcode.BNE_RELATIVE);
    }

    @Test
    void testIndexedIndirect() {
        testTwoByteOpcode(InstructionType.LDA, AddressingModeType.INDEXED_INDIRECT, Opcode.LDA_INDEXED_INDIRECT);
    }

    @Test
    void testIndirectIndexed() {
        testTwoByteOpcode(InstructionType.LDA, AddressingModeType.INDIRECT_INDEXED, Opcode.LDA_INDIRECT_INDEXED);
    }

    @Test
    void testInvalidAddressForTwoByteInstruction() {
        PrimaryExpression addressExpression = new PrimaryExpression(TokenType.NUMBER, 257);
        InstructionStatement instructionStatement = InstructionStatement.builder()
                .instructionType(InstructionType.LDX)
                .addressingModeType(AddressingModeType.IMMEDIATE)
                .addressExpression(addressExpression)
                .build();
        when(expressionEvaluator.evaluate(addressExpression)).thenReturn(257);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> compile(instructionStatement));
        assertEquals("Value too large for addressing mode \"IMMEDIATE\" on instruction \"ldx\"", exception.getMessage());
    }

    @Test
    void testAbsolute() {
        testThreeByteOpcode_twoByteValue(InstructionType.LDA, AddressingModeType.ABSOLUTE, Opcode.LDA_ABSOLUTE);
    }

    @Test
    void testAbsoluteX() {
        testThreeByteOpcode_twoByteValue(InstructionType.LDA, AddressingModeType.ABSOLUTE_X, Opcode.LDA_ABSOLUTE_X);
    }

    @Test
    void testAbsoluteY() {
        testThreeByteOpcode_twoByteValue(InstructionType.LDA, AddressingModeType.ABSOLUTE_Y, Opcode.LDA_ABSOLUTE_Y);
    }

    @Test
    void testIndirect() {
        testThreeByteOpcode_twoByteValue(InstructionType.JMP, AddressingModeType.INDIRECT, Opcode.JMP_INDIRECT);
    }

    @Test
    void testIndirect_singleByteValue() {
        testThreeByteOpcode_singleByteValue(InstructionType.JMP, AddressingModeType.INDIRECT, Opcode.JMP_INDIRECT);
    }

    @Test
    void testZeroPageFromAbsolute() {
        testThreeByteOpcodeConvertedToTwoByteOpcode(InstructionType.LDA, AddressingModeType.ABSOLUTE, Opcode.LDA_ZERO_PAGE);
    }

    @Test
    void testNoZeroPageFromAbsoluteWhenNotSupported() {
        testThreeByteOpcode_singleByteValue(InstructionType.JSR, AddressingModeType.ABSOLUTE, Opcode.JSR_ABSOLUTE);
    }

    @Test
    void testZeroPageXFromAbsoluteX() {
        testThreeByteOpcodeConvertedToTwoByteOpcode(InstructionType.LDA, AddressingModeType.ABSOLUTE_X, Opcode.LDA_ZERO_PAGE_X);
    }
    // note that there's no instruction that supports ABSOLUTE_X but not ZERO_PAGE_X, so there's no possible test for that here

    @Test
    void testZeroPageYFromAbsoluteY() {
        testThreeByteOpcodeConvertedToTwoByteOpcode(InstructionType.LDX, AddressingModeType.ABSOLUTE_Y, Opcode.LDX_ZERO_PAGE_Y);
    }

    @Test
    void testNoZeroPageYFromAbsoluteYWhenNotSupported() {
        testThreeByteOpcode_singleByteValue(InstructionType.LDA, AddressingModeType.ABSOLUTE_Y, Opcode.LDA_ABSOLUTE_Y);
    }

    @Test
    void testInvalidAddressingMode() {
        InstructionStatement instructionStatement = InstructionStatement.builder()
                .instructionType(InstructionType.CLC)
                .addressingModeType(AddressingModeType.ZERO_PAGE_X)
                .build();
        RuntimeException exception = assertThrows(RuntimeException.class, () -> compile(instructionStatement));
        assertEquals("Invalid addressing mode ZERO_PAGE_X for instruction \"clc\"", exception.getMessage());
    }

    private void testSingleByteOpcode(InstructionType instructionType, AddressingModeType addressingModeType, Opcode expectedOpcode) {
        InstructionStatement instructionStatement = InstructionStatement.builder()
                .instructionType(instructionType)
                .addressingModeType(addressingModeType)
                .build();

        SegmentData segmentData = compile(instructionStatement);

        assertEquals(new InstructionData(expectedOpcode), segmentData);
    }

    private void testTwoByteOpcode(InstructionType instructionType, AddressingModeType addressingModeType, Opcode expectedOpcode) {
        PrimaryExpression addressExpression = new PrimaryExpression(TokenType.NUMBER, 63);
        InstructionStatement instructionStatement = InstructionStatement.builder()
                .instructionType(instructionType)
                .addressingModeType(addressingModeType)
                .addressExpression(addressExpression)
                .build();
        when(expressionEvaluator.evaluate(addressExpression)).thenReturn(63);

        SegmentData segmentData = compile(instructionStatement);

        assertEquals(new InstructionData(expectedOpcode, (byte) 63), segmentData);
    }

    private void testThreeByteOpcode_twoByteValue(InstructionType instructionType, AddressingModeType addressingModeType, Opcode expectedOpcode) {
        PrimaryExpression addressExpression = new PrimaryExpression(TokenType.NUMBER, 261);
        InstructionStatement instructionStatement = InstructionStatement.builder()
                .instructionType(instructionType)
                .addressingModeType(addressingModeType)
                .addressExpression(addressExpression)
                .build();
        when(expressionEvaluator.evaluate(addressExpression)).thenReturn(261);

        SegmentData segmentData = compile(instructionStatement);

        assertEquals(new InstructionData(expectedOpcode, (byte) 5, (byte) 1), segmentData);
    }

    private void testThreeByteOpcode_singleByteValue(InstructionType instructionType, AddressingModeType addressingModeType, Opcode expectedOpcode) {
        PrimaryExpression addressExpression = new PrimaryExpression(TokenType.NUMBER, 4);
        InstructionStatement instructionStatement = InstructionStatement.builder()
                .instructionType(instructionType)
                .addressingModeType(addressingModeType)
                .addressExpression(addressExpression)
                .build();
        when(expressionEvaluator.evaluate(addressExpression)).thenReturn(4);

        SegmentData segmentData = compile(instructionStatement);

        assertEquals(new InstructionData(expectedOpcode, (byte) 4, (byte) 0), segmentData);
    }

    private void testThreeByteOpcodeConvertedToTwoByteOpcode(InstructionType instructionType, AddressingModeType addressingModeType, Opcode expectedOpcode) {
        PrimaryExpression addressExpression = new PrimaryExpression(TokenType.NUMBER, 4);
        InstructionStatement instructionStatement = InstructionStatement.builder()
                .instructionType(instructionType)
                .addressingModeType(addressingModeType)
                .addressExpression(addressExpression)
                .build();
        when(expressionEvaluator.evaluate(addressExpression)).thenReturn(4);

        SegmentData segmentData = compile(instructionStatement);

        assertEquals(new InstructionData(expectedOpcode, (byte) 4), segmentData);
    }

    private SegmentData compile(InstructionStatement instructionStatement) {
        return new InstructionCompiler(expressionEvaluator).compile(instructionStatement);
    }
}