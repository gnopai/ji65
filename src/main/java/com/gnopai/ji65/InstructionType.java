package com.gnopai.ji65;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toUnmodifiableMap;

@Getter
public enum InstructionType {
    ADC("adc"), // TODO
    AND("and"), // TODO
    ASL("asl"), // TODO
    BCC("bcc"), // TODO
    BCS("bcs"), // TODO
    BEQ("beq"), // TODO
    BIT("bit"), // TODO
    BMI("bmi"), // TODO
    BNE("bne"), // TODO
    BPL("bpl"), // TODO
    BRK("brk"), // TODO
    BVC("bvc"), // TODO
    BVS("bvs"), // TODO
    CLC("clc"),
    CLD("cld"),
    CLI("cli"),
    CLV("clv"),
    CMP("cmp"), // TODO
    CPX("cpx"), // TODO
    CPY("cpy"), // TODO
    DEC("dec"), // TODO
    DEX("dex"), // TODO
    DEY("dey"), // TODO
    EOR("eor"), // TODO
    INC("inc"), // TODO
    INX("inx"), // TODO
    INY("iny"), // TODO
    JMP("jmp"), // TODO
    JSR("jsr"), // TODO
    LDA("lda"),
    LDX("ldx"),
    LDY("ldy"),
    LSR("lsr"), // TODO
    NOP("nop"), // TODO
    ORA("ora"), // TODO
    PHA("pha"), // TODO
    PHP("php"), // TODO
    PLA("pla"), // TODO
    PLP("plp"), // TODO
    ROL("rol"), // TODO
    ROR("ror"), // TODO
    RTI("rti"), // TODO
    RTS("rts"), // TODO
    SBC("sbc"), // TODO
    SEC("sec"),
    SED("sed"),
    SEI("sei"),
    STA("sta"),
    STX("stx"),
    STY("sty"),
    TAX("tax"), // TODO
    TAY("tay"), // TODO
    TSX("tsx"), // TODO
    TXA("txa"), // TODO
    TXS("txs"), // TODO
    TYA("tya"), // TODO
    ;

    private final String identifier;

    InstructionType(String identifier) {
        this.identifier = identifier;
    }

    public boolean isBranchInstruction() {
        return List.of(BCC, BCS, BEQ, BNE, BMI, BPL, BVC, BVS).contains(this);
    }

    private static final Map<String, InstructionType> TYPES_BY_IDENTIFIER =
            Arrays.stream(InstructionType.values())
                    .collect(toUnmodifiableMap(
                            InstructionType::getIdentifier,
                            type -> type)
                    );

    public static Optional<InstructionType> fromIdentifier(String string) {
        return ofNullable(TYPES_BY_IDENTIFIER.get(string.toLowerCase()));
    }
}
