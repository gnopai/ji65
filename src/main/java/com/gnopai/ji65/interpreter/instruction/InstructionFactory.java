package com.gnopai.ji65.interpreter.instruction;

import com.gnopai.ji65.InstructionType;
import com.gnopai.ji65.util.TypeFactory;

import java.util.List;

public class InstructionFactory extends TypeFactory<InstructionType, Instruction> {
    public InstructionFactory() {
        super(InstructionType.class, Instruction::getInstructionType, List.of(
                new Clc(),
                new Cld(),
                new Cli(),
                new Clv(),
                new Jmp(),
                new Jsr(),
                new Lda(),
                new Ldx(),
                new Ldy(),
                new Rts(),
                new Sec(),
                new Sed(),
                new Sei(),
                new Sta(),
                new Stx(),
                new Sty()
        ));
    }
}
