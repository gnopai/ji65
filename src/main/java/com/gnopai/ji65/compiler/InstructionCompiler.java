package com.gnopai.ji65.compiler;

import com.gnopai.ji65.AddressingModeType;
import com.gnopai.ji65.InstructionType;
import com.gnopai.ji65.Opcode;
import com.gnopai.ji65.parser.statement.InstructionStatement;

import java.util.Optional;

public class InstructionCompiler {
    private final ExpressionEvaluator expressionEvaluator;

    public InstructionCompiler(ExpressionEvaluator expressionEvaluator) {
        this.expressionEvaluator = expressionEvaluator;
    }

    public SegmentData compile(InstructionStatement instructionStatement, Environment environment) {
        switch (instructionStatement.getAddressingModeType()) {
            case IMPLICIT:
            case ACCUMULATOR:
                return compileSingleByteInstruction(instructionStatement);
            case ZERO_PAGE:
            case ZERO_PAGE_X:
            case ZERO_PAGE_Y:
            case IMMEDIATE:
            case RELATIVE:
            case INDEXED_INDIRECT:
            case INDIRECT_INDEXED:
                return compileTwoByteInstruction(instructionStatement, environment);
            case ABSOLUTE:
                return compileThreeByteInstruction(instructionStatement, AddressingModeType.ZERO_PAGE, environment);
            case ABSOLUTE_X:
                return compileThreeByteInstruction(instructionStatement, AddressingModeType.ZERO_PAGE_X, environment);
            case ABSOLUTE_Y:
                return compileThreeByteInstruction(instructionStatement, AddressingModeType.ZERO_PAGE_Y, environment);
            case INDIRECT:
                return compileThreeByteInstruction(instructionStatement, null, environment);
            default:
                // this should be unreachable
                throw new IllegalStateException("Unknown address mode type encountered: " + instructionStatement.getAddressingModeType().name());
        }
    }

    private SegmentData compileSingleByteInstruction(InstructionStatement instructionStatement) {
        InstructionType instructionType = instructionStatement.getInstructionType();
        Opcode opcode = Opcode.of(instructionType, instructionStatement.getAddressingModeType())
                .or(() -> Opcode.of(instructionType, AddressingModeType.ACCUMULATOR))
                .orElseThrow(() -> invalidOpcode(instructionStatement));
        return new InstructionData(opcode);
    }

    private SegmentData compileTwoByteInstruction(InstructionStatement instructionStatement, Environment environment) {
        Opcode opcode = getOpcode(instructionStatement);
        int singleByteValue = expressionEvaluator.evaluate(instructionStatement.getAddressExpression(), environment);
        if (singleByteValue > 255) {
            throw new RuntimeException("Value too large for" +
                    " addressing mode \"" + instructionStatement.getAddressingModeType().name() + "\"" +
                    " on instruction \"" + instructionStatement.getInstructionType().getIdentifier() + "\"");
        }
        return new InstructionData(opcode, (byte) singleByteValue);
    }

    private SegmentData compileThreeByteInstruction(InstructionStatement instructionStatement, AddressingModeType zeroPageAddressingModeType, Environment environment) {
        int twoByteValue = expressionEvaluator.evaluate(instructionStatement.getAddressExpression(), environment);
        if (twoByteValue < 256) {
            Optional<Opcode> zeroPageOpcode = Opcode.of(instructionStatement.getInstructionType(), zeroPageAddressingModeType);
            if (zeroPageOpcode.isPresent()) {
                return new InstructionData(zeroPageOpcode.get(), (byte) twoByteValue);
            }
        }
        Opcode opcode = getOpcode(instructionStatement);
        return getBytesForThreeByteOpcode(opcode, twoByteValue);
    }

    private SegmentData getBytesForThreeByteOpcode(Opcode opcode, int value) {
        byte highByte = (byte) (value / 256);
        byte lowByte = (byte) (value % 256);
        return new InstructionData(opcode, lowByte, highByte);
    }

    private Opcode getOpcode(InstructionStatement instructionStatement) {
        return Opcode.of(instructionStatement.getInstructionType(), instructionStatement.getAddressingModeType())
                .orElseThrow(() -> invalidOpcode(instructionStatement));
    }

    private RuntimeException invalidOpcode(InstructionStatement instructionStatement) {
        return new RuntimeException("Invalid addressing mode " + instructionStatement.getAddressingModeType().name() +
                " for instruction \"" + instructionStatement.getInstructionType().getIdentifier() + "\"");
    }
}
