package com.gnopai.ji65.interpreter.address;

import com.gnopai.ji65.Address;
import com.gnopai.ji65.AddressingModeType;
import com.gnopai.ji65.Cpu;
import com.gnopai.ji65.interpreter.Operand;

public class IndirectAddressingMode implements AddressingMode {
    @Override
    public AddressingModeType getType() {
        return AddressingModeType.INDIRECT;
    }

    @Override
    public Operand determineRuntimeOperand(Cpu cpu) {
        byte indirectAddressLowByte = cpu.nextProgramByte();
        byte indirectAddressHighByte = cpu.nextProgramByte();
        Address indirectAddress = Address.of(indirectAddressHighByte, indirectAddressLowByte);
        byte actualAddressLowByte = cpu.getMemoryValue(indirectAddress);
        byte actualAddressHighByte = cpu.getMemoryValue(indirectAddress.plus(1));
        return Operand.of(Address.of(actualAddressHighByte, actualAddressLowByte));
    }
}
