package com.gnopai.ji65.address;

import com.gnopai.ji65.Cpu;
import com.gnopai.ji65.Operand;

public interface AddressingMode {
    AddressingModeType getType();
    Operand determineRuntimeOperand(Cpu cpu, Operand operand);
}
