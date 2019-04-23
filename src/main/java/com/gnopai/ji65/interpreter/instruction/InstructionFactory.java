package com.gnopai.ji65.interpreter.instruction;

import com.gnopai.ji65.InstructionType;
import com.gnopai.ji65.util.TypeFactory;

import java.util.List;

public class InstructionFactory extends TypeFactory<InstructionType, Instruction> {
    public InstructionFactory() {
        super(InstructionType.class, Instruction::getInstructionType, List.of(
                new Adc(),
                new And(),
                new Asl(),
                new Bcc(),
                new Bcs(),
                new Bit(),
                new Brk(),
                new Clc(),
                new Cld(),
                new Cli(),
                new Clv(),
                new Cmp(),
                new Cpx(),
                new Cpy(),
                new Dec(),
                new Dex(),
                new Dey(),
                new Eor(),
                new Inc(),
                new Inx(),
                new Iny(),
                new Jmp(),
                new Jsr(),
                new Lda(),
                new Ldx(),
                new Ldy(),
                new Lsr(),
                new Nop(),
                new Ora(),
                new Pha(),
                new Php(),
                new Pla(),
                new Plp(),
                new Rol(),
                new Ror(),
                new Rti(),
                new Rts(),
                new Sbc(),
                new Sec(),
                new Sed(),
                new Sei(),
                new Sta(),
                new Stx(),
                new Sty(),
                new Tax(),
                new Tay(),
                new Tsx(),
                new Txa(),
                new Txs(),
                new Tya()
        ));
    }
}
