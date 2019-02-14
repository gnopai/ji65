package com.gnopai.ji65.address;

import com.gnopai.ji65.Cpu;
import com.gnopai.ji65.Operand;

public class ImmediateAddressingMode implements AddressingMode {

    @Override
    public AddressingModeType getType() {
        return AddressingModeType.IMMEDIATE;
    }

    @Override
    public Operand determineRuntimeOperand(Cpu cpu) {
        return Operand.builder()
                .lowByte(cpu.nextProgramByte())
                .address(false)
                .build();
    }
}
