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
    ADC("adc"),
    AND("and"),
    ASL("asl"),
    BCC("bcc"),
    BCS("bcs"),
    BEQ("beq"),
    BIT("bit"),
    BMI("bmi"),
    BNE("bne"),
    BPL("bpl"),
    BRK("brk"),
    BVC("bvc"),
    BVS("bvs"),
    CLC("clc"),
    CLD("cld"),
    CLI("cli"),
    CLV("clv"),
    CMP("cmp"),
    CPX("cpx"),
    CPY("cpy"),
    DEC("dec"),
    DEX("dex"),
    DEY("dey"),
    EOR("eor"),
    INC("inc"),
    INX("inx"),
    INY("iny"),
    JMP("jmp"),
    JSR("jsr"),
    LDA("lda"),
    LDX("ldx"),
    LDY("ldy"),
    LSR("lsr"),
    NOP("nop"),
    ORA("ora"),
    PHA("pha"),
    PHP("php"),
    PLA("pla"),
    PLP("plp"),
    ROL("rol"),
    ROR("ror"),
    RTI("rti"),
    RTS("rts"),
    SBC("sbc"),
    SEC("sec"),
    SED("sed"),
    SEI("sei"),
    STA("sta"),
    STX("stx"),
    STY("sty"),
    TAX("tax"),
    TAY("tay"),
    TSX("tsx"),
    TXA("txa"),
    TXS("txs"),
    TYA("tya"),
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
