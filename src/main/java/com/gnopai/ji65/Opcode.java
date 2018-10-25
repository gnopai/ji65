package com.gnopai.ji65;

import com.gnopai.ji65.address.AddressingModeType;
import com.gnopai.ji65.instruction.InstructionType;
import lombok.Getter;
import lombok.Value;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import static com.gnopai.ji65.address.AddressingModeType.*;
import static com.gnopai.ji65.instruction.InstructionType.*;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toMap;

@Getter
public enum Opcode {
    ASL_ACCUMULATOR(ASL, ACCUMULATOR, 0x0A, 1),
    ASL_ZERO_PAGE(ASL, ZERO_PAGE, 0x06, 2),
    ASL_ZERO_PAGE_X(ASL, ZERO_PAGE_X, 0x16, 2),
    ASL_ABSOLUTE(ASL, ABSOLUTE, 0x0E, 3),
    ASL_ABSOLUTE_X(ASL, ABSOLUTE_X, 0x1E, 3),
    BNE_RELATIVE(BNE, RELATIVE, 0xD0, 2),
    LDA_IMMEDIATE(LDA, IMMEDIATE, 0xA9, 2),
    LDA_ZERO_PAGE(LDA, ZERO_PAGE, 0xA5, 2),
    LDA_ZERO_PAGE_X(LDA, ZERO_PAGE_X, 0xB5, 2),
    LDA_ABSOLUTE(LDA, ABSOLUTE, 0xAD, 3),
    LDA_ABSOLUTE_X(LDA, ABSOLUTE_X, 0xBD, 3),
    LDA_ABSOLUTE_Y(LDA, ABSOLUTE_Y, 0xB9, 3),
    LDA_INDEXED_INDIRECT(LDA, INDEXED_INDIRECT, 0xA1, 2),
    LDA_INDIRECT_INDEXED(LDA, INDIRECT_INDEXED, 0xB1, 2),
    LDX_IMMEDIATE(LDX, IMMEDIATE, 0xA2, 2),
    LDX_ZERO_PAGE(LDX, ZERO_PAGE, 0xA6, 2),
    LDX_ZERO_PAGE_Y(LDX, ZERO_PAGE_Y, 0xB6, 2),
    LDX_ABSOLUTE(LDX, ABSOLUTE, 0xAE, 3),
    LDX_ABSOLUTE_Y(LDX, ABSOLUTE_Y, 0xBE, 3),
    JMP_ABSOLUTE(JMP, ABSOLUTE, 0x4C, 3),
    JMP_INDIRECT(JMP, INDIRECT, 0x6C, 3),
    TAX_IMPLICIT(TAX, IMPLICIT, 0xAA, 1),
    ;

    private final InstructionType instructionType;
    private final byte opcode;
    private final AddressingModeType addressingModeType;
    private final int byteCount;

    Opcode(InstructionType instructionType, AddressingModeType addressingModeType, int opcode, int byteCount) {
        this.instructionType = instructionType;
        this.opcode = (byte) opcode;
        this.addressingModeType = addressingModeType;
        this.byteCount = byteCount;
    }

    @Value
    private static class OpcodeKey {
        InstructionType instructionType;
        AddressingModeType addressingModeType;
    }

    private final static Map<OpcodeKey, Opcode> BY_OPCODE_KEY = Arrays.stream(values())
            .collect(toMap(
                    opcode -> new OpcodeKey(opcode.getInstructionType(), opcode.getAddressingModeType()),
                    Function.identity()));

    public static Optional<Opcode> of(InstructionType instructionType, AddressingModeType addressingModeType) {
        OpcodeKey opcodeKey = new OpcodeKey(instructionType, addressingModeType);
        return ofNullable(BY_OPCODE_KEY.get(opcodeKey));
    }
}
