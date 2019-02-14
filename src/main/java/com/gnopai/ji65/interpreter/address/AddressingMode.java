package com.gnopai.ji65.interpreter.address;

import com.gnopai.ji65.AddressingModeType;
import com.gnopai.ji65.Cpu;
import com.gnopai.ji65.interpreter.Operand;

public interface AddressingMode {
    AddressingModeType getType();

    Operand determineRuntimeOperand(Cpu cpu);
}
