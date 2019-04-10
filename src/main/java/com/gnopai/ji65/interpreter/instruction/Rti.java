package com.gnopai.ji65.interpreter.instruction;

import com.gnopai.ji65.Address;
import com.gnopai.ji65.Cpu;
import com.gnopai.ji65.InstructionType;
import com.gnopai.ji65.interpreter.Operand;

public class Rti implements Instruction {
    @Override
    public InstructionType getInstructionType() {
        return InstructionType.RTI;
    }

    @Override
    public void run(Cpu cpu, Operand operand) {
        byte processorStatus = cpu.pullFromStack();
        cpu.setProcessorStatus(processorStatus);

        byte lowAddressByte = cpu.pullFromStack();
        byte highAddressByte = cpu.pullFromStack();
        Address returnAddress = Address.of(highAddressByte, lowAddressByte);
        cpu.setProgramCounter(returnAddress);
    }
}
