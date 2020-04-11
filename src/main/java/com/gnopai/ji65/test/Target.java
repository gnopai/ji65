package com.gnopai.ji65.test;

import com.gnopai.ji65.Cpu;

import java.util.function.BiConsumer;
import java.util.function.Function;

public enum Target {
    X(Cpu::getX, Cpu::setX),
    Y(Cpu::getY, Cpu::setY),
    A(Cpu::getAccumulator, Cpu::setAccumulator),
    MEMORY(null, null),
    ;

    private final Function<Cpu, Byte> getValue;
    private final BiConsumer<Cpu, Byte> setValue;

    Target(Function<Cpu, Byte> getValue, BiConsumer<Cpu, Byte> setValue) {
        this.getValue = getValue;
        this.setValue = setValue;
    }

    public Byte getValue(Cpu cpu) {
        return this.getValue.apply(cpu);
    }

    public void setValue(Cpu cpu, int value) {
        this.setValue.accept(cpu, (byte) value);
    }
}
