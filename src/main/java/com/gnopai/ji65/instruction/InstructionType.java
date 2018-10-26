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
    ADC("adc", Lda.class),
    AND("and", Lda.class),
    ASL("asl", Lda.class),
    BCC("bcc", Lda.class),
    BCS("bcs", Lda.class),
    BEQ("beq", Lda.class),
    BIT("bit", Lda.class),
    BMI("bmi", Lda.class),
    BNE("bne", Lda.class),
    BPL("bpl", Lda.class),
    BRK("brk", Lda.class),
    BVC("bvc", Lda.class),
    BVS("bvs", Lda.class),
    CLC("clc", Lda.class),
    CLD("cld", Lda.class),
    CLI("cli", Lda.class),
    CLV("clv", Lda.class),
    CMP("cmp", Lda.class),
    CPX("cpx", Lda.class),
    CPY("cpy", Lda.class),
    DEC("dec", Lda.class),
    DEX("dex", Lda.class),
    DEY("dey", Lda.class),
    EOR("eor", Lda.class),
    INC("inc", Lda.class),
    INX("inx", Lda.class),
    INY("iny", Lda.class),
    JMP("jmp", Lda.class),
    JSR("jsr", Lda.class),
    LDA("lda", Lda.class),
    LDX("ldx", Ldx.class),
    LDY("ldy", Ldy.class),
    LSR("lsr", Lda.class),
    NOP("nop", Lda.class),
    ORA("ora", Lda.class),
    PHA("pha", Lda.class),
    PHP("php", Lda.class),
    PLA("pla", Lda.class),
    PLP("plp", Lda.class),
    ROL("rol", Lda.class),
    ROR("ror", Lda.class),
    RTI("rti", Lda.class),
    RTS("rts", Lda.class),
    SBC("sbc", Lda.class),
    SEC("sec", Lda.class),
    SED("sed", Lda.class),
    SEI("sei", Lda.class),
    STA("sta", Lda.class),
    STX("stx", Lda.class),
    STY("sty", Lda.class),
    TAX("tax", Lda.class),
    TAY("tay", Lda.class),
    TSX("tsx", Lda.class),
    TXA("txa", Lda.class),
    TXS("txs", Lda.class),
    TYA("tya", Lda.class),
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
