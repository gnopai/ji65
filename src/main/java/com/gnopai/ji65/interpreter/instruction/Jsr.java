package com.gnopai.ji65.interpreter.instruction;

import com.gnopai.ji65.Address;
import com.gnopai.ji65.Cpu;
import com.gnopai.ji65.InstructionType;
import com.gnopai.ji65.interpreter.Operand;

public class Jsr implements Instruction {
    @Override
    public InstructionType getInstructionType() {
        return InstructionType.JSR;
    }

    @Override
    public void run(Cpu cpu, Operand operand) {
        Address returnAddress = new Address(cpu.getProgramCounter() - 1);
        cpu.pushOntoStack(returnAddress.getHighByte());
        cpu.pushOntoStack(returnAddress.getLowByte());
        cpu.setProgramCounter(operand.asAddress());
    }
}
