package com.gnopai.ji65.interpreter.instruction;

import com.gnopai.ji65.Cpu;
import com.gnopai.ji65.interpreter.Operand;

import java.util.Optional;

class OperandResolver {
    public byte resolveOperand(Cpu cpu, Operand operand) {
        return Optional.of(operand)
                .filter(Operand::isAddress)
                .map(Operand::asAddress)
                .map(cpu::getMemoryValue)
                .orElse(operand.getLowByte());
    }
}
