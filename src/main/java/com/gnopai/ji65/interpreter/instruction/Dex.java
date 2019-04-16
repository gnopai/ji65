package com.gnopai.ji65.interpreter.instruction;

import com.gnopai.ji65.Cpu;
import com.gnopai.ji65.InstructionType;
import com.gnopai.ji65.interpreter.Operand;

public class Dex implements Instruction {
    @Override
    public InstructionType getInstructionType() {
        return InstructionType.DEX;
    }

    @Override
    public void run(Cpu cpu, Operand operand) {
        byte value = (byte) (cpu.getX() - 1);
        cpu.setX(value);
        cpu.updateZeroFlag(value);
        cpu.updateNegativeFlag(value);
    }
}
