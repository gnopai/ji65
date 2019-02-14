package com.gnopai.ji65.interpreter.instruction;

import com.gnopai.ji65.InstructionType;
import com.gnopai.ji65.util.TypeFactory;

import java.util.List;

public class InstructionFactory extends TypeFactory<InstructionType, Instruction> {
    public InstructionFactory() {
        super(Instruction::getInstructionType, List.of(
                new Lda(),
                new Ldx(),
                new Ldy(),
                new Sta(),
                new Stx(),
                new Sty()
        ));
    }
}
