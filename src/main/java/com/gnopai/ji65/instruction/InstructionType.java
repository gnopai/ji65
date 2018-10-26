package com.gnopai.ji65.instruction;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toUnmodifiableMap;

@Getter
public enum InstructionType {
    ADC("adc", Lda.class), // TODO
    AND("and", Lda.class), // TODO
    ASL("asl", Lda.class), // TODO
    BCC("bcc", Lda.class), // TODO
    BCS("bcs", Lda.class), // TODO
    BEQ("beq", Lda.class), // TODO
    BIT("bit", Lda.class), // TODO
    BMI("bmi", Lda.class), // TODO
    BNE("bne", Lda.class), // TODO
    BPL("bpl", Lda.class), // TODO
    BRK("brk", Lda.class), // TODO
    BVC("bvc", Lda.class), // TODO
    BVS("bvs", Lda.class), // TODO
    CLC("clc", Lda.class), // TODO
    CLD("cld", Lda.class), // TODO
    CLI("cli", Lda.class), // TODO
    CLV("clv", Lda.class), // TODO
    CMP("cmp", Lda.class), // TODO
    CPX("cpx", Lda.class), // TODO
    CPY("cpy", Lda.class), // TODO
    DEC("dec", Lda.class), // TODO
    DEX("dex", Lda.class), // TODO
    DEY("dey", Lda.class), // TODO
    EOR("eor", Lda.class), // TODO
    INC("inc", Lda.class), // TODO
    INX("inx", Lda.class), // TODO
    INY("iny", Lda.class), // TODO
    JMP("jmp", Lda.class), // TODO
    JSR("jsr", Lda.class), // TODO
    LDA("lda", Lda.class),
    LDX("ldx", Ldx.class),
    LDY("ldy", Ldy.class),
    LSR("lsr", Lda.class), // TODO
    NOP("nop", Lda.class), // TODO
    ORA("ora", Lda.class), // TODO
    PHA("pha", Lda.class), // TODO
    PHP("php", Lda.class), // TODO
    PLA("pla", Lda.class), // TODO
    PLP("plp", Lda.class), // TODO
    ROL("rol", Lda.class), // TODO
    ROR("ror", Lda.class), // TODO
    RTI("rti", Lda.class), // TODO
    RTS("rts", Lda.class), // TODO
    SBC("sbc", Lda.class), // TODO
    SEC("sec", Lda.class), // TODO
    SED("sed", Lda.class), // TODO
    SEI("sei", Lda.class), // TODO
    STA("sta", Sta.class),
    STX("stx", Stx.class),
    STY("sty", Sty.class),
    TAX("tax", Lda.class), // TODO
    TAY("tay", Lda.class), // TODO
    TSX("tsx", Lda.class), // TODO
    TXA("txa", Lda.class), // TODO
    TXS("txs", Lda.class), // TODO
    TYA("tya", Lda.class), // TODO
    ;

    private final String instructionName;
    private final Class<? extends Instruction> instructionClass;

    InstructionType(String instructionName, Class<? extends Instruction> instructionClass) {
        this.instructionName = instructionName;
        this.instructionClass = instructionClass;
    }

    public boolean isBranchInstruction() {
        return List.of(BCC, BCS, BEQ, BNE, BMI, BPL, BVC, BVS).contains(this);
    }

    private static final Map<String, InstructionType> INSTRUCTION_TYPES_BY_NAME =
            Arrays.stream(InstructionType.values())
                    .collect(toUnmodifiableMap(
                            InstructionType::getInstructionName,
                            instruction -> instruction)
                    );

    public static Optional<InstructionType> fromName(String string) {
        return ofNullable(INSTRUCTION_TYPES_BY_NAME.get(string.toLowerCase()));
    }
}
