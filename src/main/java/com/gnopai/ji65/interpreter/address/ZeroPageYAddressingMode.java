package com.gnopai.ji65.interpreter.address;

import com.gnopai.ji65.Address;
import com.gnopai.ji65.AddressingModeType;
import com.gnopai.ji65.Cpu;
import com.gnopai.ji65.interpreter.Operand;

public class ZeroPageYAddressingMode implements AddressingMode {
    @Override
    public AddressingModeType getType() {
        return AddressingModeType.ZERO_PAGE_Y;
    }

    @Override
    public Operand determineRuntimeOperand(Cpu cpu) {
        Address baseAddress = new Address(cpu.nextProgramByte());
        Address indexedAddress = baseAddress.plus(cpu.getY());
        return Operand.of(indexedAddress);
    }
}
