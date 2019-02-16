package com.gnopai.ji65.interpreter.address;

import com.gnopai.ji65.Address;
import com.gnopai.ji65.AddressingModeType;
import com.gnopai.ji65.Cpu;
import com.gnopai.ji65.interpreter.Operand;

public class IndirectIndexedAddressingMode implements AddressingMode {
    @Override
    public AddressingModeType getType() {
        return AddressingModeType.INDIRECT_INDEXED;
    }

    @Override
    public Operand determineRuntimeOperand(Cpu cpu) {
        // lda ($45),Y
        Address indirectAddress = new Address(cpu.nextProgramByte());
        byte baseAddressLowByte = cpu.getMemoryValue(indirectAddress);
        byte baseAddressHighByte = cpu.getMemoryValue(indirectAddress.plus(1));
        Address baseAddress = Address.of(baseAddressHighByte, baseAddressLowByte);
        Address indexedAddress = baseAddress.plus(cpu.getY());
        return Operand.of(indexedAddress);
    }
}
