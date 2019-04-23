package com.gnopai.ji65.interpreter.address;

import com.gnopai.ji65.AddressingModeType;
import com.gnopai.ji65.Cpu;
import com.gnopai.ji65.interpreter.Operand;

public class RelativeAddressingMode implements AddressingMode {
    @Override
    public AddressingModeType getType() {
        return AddressingModeType.RELATIVE;
    }

    @Override
    public Operand determineRuntimeOperand(Cpu cpu) {
        return Operand.builder()
                .lowByte(cpu.nextProgramByte())
                .address(false)
                .build();
    }
}
