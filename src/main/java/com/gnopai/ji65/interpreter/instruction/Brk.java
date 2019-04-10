package com.gnopai.ji65.interpreter.instruction;

import com.gnopai.ji65.Address;
import com.gnopai.ji65.Cpu;
import com.gnopai.ji65.InstructionType;
import com.gnopai.ji65.interpreter.Operand;

public class Brk implements Instruction {
    @Override
    public InstructionType getInstructionType() {
        return InstructionType.BRK;
    }

    @Override
    public void run(Cpu cpu, Operand operand) {
        Address currentProgramCounter = new Address(cpu.getProgramCounter());
        cpu.pushOntoStack(currentProgramCounter.getHighByte());
        cpu.pushOntoStack(currentProgramCounter.getLowByte());
        cpu.pushOntoStack(cpu.getProcessorStatus());

        byte interruptVectorLowByte = cpu.getMemoryValue(new Address(0xFFFE));
        byte interruptVectorHighByte = cpu.getMemoryValue(new Address(0xFFFF));
        Address interruptVector = Address.of(interruptVectorHighByte, interruptVectorLowByte);
        cpu.setProgramCounter(interruptVector);
        cpu.setBreakCommand(true);
    }
}
