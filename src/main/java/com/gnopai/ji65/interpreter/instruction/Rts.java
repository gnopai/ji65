package com.gnopai.ji65.interpreter.instruction;

import com.gnopai.ji65.Address;
import com.gnopai.ji65.Cpu;
import com.gnopai.ji65.InstructionType;
import com.gnopai.ji65.interpreter.Operand;

public class Rts implements Instruction {
    @Override
    public InstructionType getInstructionType() {
        return InstructionType.RTS;
    }

    @Override
    public void run(Cpu cpu, Operand operand) {
        byte lowAddressByte = cpu.pullFromStack();
        byte highAddressByte = cpu.pullFromStack();
        Address returnAddress = Address.of(highAddressByte, lowAddressByte).plus(1);
        cpu.setProgramCounter(returnAddress);
    }
}
