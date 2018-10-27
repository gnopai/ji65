package com.gnopai.ji65.address;

import com.gnopai.ji65.BytesValue;
import com.gnopai.ji65.Cpu;
import com.gnopai.ji65.Operand;

public class ImmediateAddressingMode implements AddressingMode {

    @Override
    public AddressingModeType getType() {
        return AddressingModeType.IMMEDIATE;
    }

    @Override
    public Operand determineRuntimeOperand(Cpu cpu, BytesValue argument) {
        return Operand.builder()
                .value(argument)
                .address(false)
                .build();
    }
}
