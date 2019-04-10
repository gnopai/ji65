package com.gnopai.ji65.interpreter;

import com.gnopai.ji65.Address;
import com.gnopai.ji65.Cpu;

public class EndProgramAtAddress implements ProgramEndStrategy {
    private final Address endAddress;

    public EndProgramAtAddress(Address endAddress) {
        this.endAddress = endAddress;
    }

    @Override
    public boolean shouldEndProgram(Cpu cpu) {
        Address currentAddress = new Address(cpu.getProgramCounter());
        return currentAddress.equals(endAddress);
    }
}
