package com.gnopai.ji65.address;

import com.gnopai.ji65.Cpu;
import com.gnopai.ji65.Operand;

public class ZeroPageAddressingMode implements AddressingMode {
    @Override
    public AddressingModeType getType() {
        return AddressingModeType.ZERO_PAGE;
    }

    @Override
    public Operand determineRuntimeOperand(Cpu cpu, Operand operand) {
        return Operand.builder()
                .lowByte(operand.getLowByte())
                .highByte((byte) 0x00)
                .address(true)
                .build();
    }
}
