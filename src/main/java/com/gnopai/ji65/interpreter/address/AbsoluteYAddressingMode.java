package com.gnopai.ji65.interpreter.address;

import com.gnopai.ji65.Address;
import com.gnopai.ji65.AddressingModeType;
import com.gnopai.ji65.Cpu;
import com.gnopai.ji65.interpreter.Operand;

public class AbsoluteYAddressingMode implements AddressingMode {
    @Override
    public AddressingModeType getType() {
        return AddressingModeType.ABSOLUTE_Y;
    }

    @Override
    public Operand determineRuntimeOperand(Cpu cpu) {
        byte lowByte = cpu.nextProgramByte();
        byte highByte = cpu.nextProgramByte();
        Address baseAddress = Address.of(highByte, lowByte);
        Address indexedAddress = baseAddress.plus(cpu.getY());
        return Operand.of(indexedAddress);
    }
}
