package com.gnopai.ji65.address;

import com.gnopai.ji65.Address;
import com.gnopai.ji65.Cpu;
import com.gnopai.ji65.Operand;

public class ZeroPageXAddressingMode implements AddressingMode {
    @Override
    public AddressingModeType getType() {
        return AddressingModeType.ZERO_PAGE_X;
    }

    @Override
    public Operand determineRuntimeOperand(Cpu cpu) {
        Address baseAddress = new Address(cpu.nextProgramByte());
        Address indexedAddress = baseAddress.plus(cpu.getX());
        return Operand.of(indexedAddress);
    }
}
