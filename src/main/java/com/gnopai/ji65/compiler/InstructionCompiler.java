package com.gnopai.ji65.compiler;

import com.gnopai.ji65.AddressingModeType;
import com.gnopai.ji65.Opcode;
import com.gnopai.ji65.parser.statement.InstructionStatement;

import java.util.Optional;

public class InstructionCompiler {
    private final ExpressionEvaluator expressionEvaluator;

    public InstructionCompiler(ExpressionEvaluator expressionEvaluator) {
        this.expressionEvaluator = expressionEvaluator;
    }

    public SegmentData compile(InstructionStatement instructionStatement) {
        AddressingModeType addressingModeType = instructionStatement.getAddressingModeType();
        // TODO try switching implicit to accumulator if possible
        Opcode opcode = Opcode.of(instructionStatement.getInstructionType(), addressingModeType)
                .orElseThrow(() -> new RuntimeException("Invalid addressing mode " + addressingModeType.name() +
                        " for instruction \"" + instructionStatement.getInstructionType().getIdentifier() + "\"")
                );

        switch (addressingModeType) {
            case IMPLICIT:
            case ACCUMULATOR:
                return new InstructionData(opcode);
            case ZERO_PAGE:
            case ZERO_PAGE_X:
            case ZERO_PAGE_Y:
            case IMMEDIATE:
            case RELATIVE:
            case INDEXED_INDIRECT:
            case INDIRECT_INDEXED:
                return compileTwoByteInstruction(instructionStatement, opcode);
            case ABSOLUTE:
                return compileThreeByteInstruction(instructionStatement, opcode, AddressingModeType.ZERO_PAGE);
            case ABSOLUTE_X:
                return compileThreeByteInstruction(instructionStatement, opcode, AddressingModeType.ZERO_PAGE_X);
            case ABSOLUTE_Y:
                return compileThreeByteInstruction(instructionStatement, opcode, AddressingModeType.ZERO_PAGE_Y);
            case INDIRECT:
                return compileThreeByteInstruction(instructionStatement, opcode, null);
            default:
                // this should be unreachable
                throw new IllegalStateException("Unknown address mode type encountered: " + addressingModeType.name());
        }
    }

    private SegmentData compileTwoByteInstruction(InstructionStatement instructionStatement, Opcode opcode) {
        int singleByteValue = expressionEvaluator.evaluate(instructionStatement.getAddressExpression());
        if (singleByteValue > 255) {
            throw new RuntimeException("Value too large for" +
                    " addressing mode \"" + instructionStatement.getAddressingModeType().name() + "\"" +
                    " on instruction \"" + instructionStatement.getInstructionType().getIdentifier() + "\"");
        }
        return new InstructionData(opcode, (byte) singleByteValue);
    }

    private SegmentData compileThreeByteInstruction(InstructionStatement instructionStatement, Opcode opcode, AddressingModeType zeroPageAddressingModeType) {
        int twoByteValue = expressionEvaluator.evaluate(instructionStatement.getAddressExpression());
        if (twoByteValue < 256) {
            Optional<Opcode> zeroPageOpcode = Opcode.of(instructionStatement.getInstructionType(), zeroPageAddressingModeType);
            if (zeroPageOpcode.isPresent()) {
                return new InstructionData(zeroPageOpcode.get(), (byte) twoByteValue);
            }
        }
        return getBytesForThreeByteOpcode(opcode, twoByteValue);
    }

    private SegmentData getBytesForThreeByteOpcode(Opcode opcode, int value) {
        byte highByte = (byte) (value / 256);
        byte lowByte = (byte) (value % 256);
        return new InstructionData(opcode, lowByte, highByte);
    }
}
