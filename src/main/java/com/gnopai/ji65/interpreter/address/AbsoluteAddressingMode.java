package com.gnopai.ji65.interpreter.address;

import com.gnopai.ji65.AddressingModeType;
import com.gnopai.ji65.Cpu;
import com.gnopai.ji65.interpreter.Operand;

public class AbsoluteAddressingMode implements AddressingMode {
    @Override
    public AddressingModeType getType() {
        return AddressingModeType.ABSOLUTE;
    }

    @Override
    public Operand determineRuntimeOperand(Cpu cpu) {
        byte lowByte = cpu.nextProgramByte();
        byte highByte = cpu.nextProgramByte();
        return Operand.builder()
                .lowByte(lowByte)
                .highByte(highByte)
                .address(true)
                .build();
    }
}
