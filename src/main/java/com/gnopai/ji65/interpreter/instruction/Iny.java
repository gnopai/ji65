package com.gnopai.ji65.interpreter.instruction;

import com.gnopai.ji65.Cpu;
import com.gnopai.ji65.InstructionType;
import com.gnopai.ji65.interpreter.Operand;

public class Iny implements Instruction {
    @Override
    public InstructionType getInstructionType() {
        return InstructionType.INY;
    }

    @Override
    public void run(Cpu cpu, Operand operand) {
        byte value = (byte) (cpu.getY() + 1);
        cpu.setY(value);
        cpu.updateZeroFlag(value);
        cpu.updateNegativeFlag(value);
    }
}
