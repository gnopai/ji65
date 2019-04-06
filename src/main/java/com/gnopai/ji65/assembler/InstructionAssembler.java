package com.gnopai.ji65.assembler;

import com.gnopai.ji65.AddressingModeType;
import com.gnopai.ji65.InstructionType;
import com.gnopai.ji65.Opcode;
import com.gnopai.ji65.parser.statement.InstructionStatement;

import java.util.Optional;

public class InstructionAssembler {
    private final ExpressionZeroPageChecker expressionZeroPageChecker;

    public InstructionAssembler(ExpressionZeroPageChecker expressionZeroPageChecker) {
        this.expressionZeroPageChecker = expressionZeroPageChecker;
    }

    public SegmentData assemble(InstructionStatement instructionStatement, Environment environment) {
        switch (instructionStatement.getAddressingModeType()) {
            case IMPLICIT:
            case ACCUMULATOR:
                return assembleSingleByteInstruction(instructionStatement);
            case ZERO_PAGE:
            case ZERO_PAGE_X:
            case ZERO_PAGE_Y:
            case IMMEDIATE:
            case RELATIVE:
            case INDEXED_INDIRECT:
            case INDIRECT_INDEXED:
                return assembleTwoByteInstruction(instructionStatement);
            case ABSOLUTE:
                return assembleThreeByteInstruction(instructionStatement, AddressingModeType.ZERO_PAGE, environment);
            case ABSOLUTE_X:
                return assembleThreeByteInstruction(instructionStatement, AddressingModeType.ZERO_PAGE_X, environment);
            case ABSOLUTE_Y:
                return assembleThreeByteInstruction(instructionStatement, AddressingModeType.ZERO_PAGE_Y, environment);
            case INDIRECT:
                return assembleThreeByteInstruction(instructionStatement, null, environment);
            default:
                // this should be unreachable
                throw new IllegalStateException("Unknown address mode type encountered: " + instructionStatement.getAddressingModeType().name());
        }
    }

    private SegmentData assembleSingleByteInstruction(InstructionStatement instructionStatement) {
        InstructionType instructionType = instructionStatement.getInstructionType();
        Opcode opcode = Opcode.of(instructionType, instructionStatement.getAddressingModeType())
                .or(() -> Opcode.of(instructionType, AddressingModeType.ACCUMULATOR))
                .orElseThrow(() -> invalidOpcode(instructionStatement));
        return new InstructionData(opcode);
    }

    private SegmentData assembleTwoByteInstruction(InstructionStatement instructionStatement) {
        Opcode opcode = getOpcode(instructionStatement);
        return new InstructionData(opcode, new UnresolvedExpression(instructionStatement.getAddressExpression(), true));
    }

    private SegmentData assembleThreeByteInstruction(InstructionStatement instructionStatement, AddressingModeType zeroPageAddressingModeType, Environment environment) {
        boolean isZeroPage = expressionZeroPageChecker.isZeroPage(instructionStatement.getAddressExpression(), environment);
        UnresolvedExpression operand = new UnresolvedExpression(instructionStatement.getAddressExpression(), isZeroPage);
        if (operand.isSingleByte()) {
            Optional<Opcode> zeroPageOpcode = Opcode.of(instructionStatement.getInstructionType(), zeroPageAddressingModeType);
            if (zeroPageOpcode.isPresent()) {
                return new InstructionData(zeroPageOpcode.get(), operand);
            }
        }
        Opcode opcode = getOpcode(instructionStatement);
        return new InstructionData(opcode, operand);
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
