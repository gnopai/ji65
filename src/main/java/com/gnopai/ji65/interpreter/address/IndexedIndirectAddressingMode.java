package com.gnopai.ji65.interpreter.address;

import com.gnopai.ji65.Address;
import com.gnopai.ji65.AddressingModeType;
import com.gnopai.ji65.Cpu;
import com.gnopai.ji65.interpreter.Operand;

public class IndexedIndirectAddressingMode implements AddressingMode {
    @Override
    public AddressingModeType getType() {
        return AddressingModeType.INDEXED_INDIRECT;
    }

    @Override
    public Operand determineRuntimeOperand(Cpu cpu) {
        // lda ($45,X)
        Address baseIndirectAddress = new Address(cpu.nextProgramByte());
        Address indexedIndirectAddress = baseIndirectAddress.plus(cpu.getX());
        byte actualAddressLowByte = cpu.getMemoryValue(indexedIndirectAddress);
        byte actualAddressHighByte = cpu.getMemoryValue(indexedIndirectAddress.plus(1));
        return Operand.of(Address.of(actualAddressHighByte, actualAddressLowByte));
    }
}
