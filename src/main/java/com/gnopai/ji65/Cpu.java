package com.gnopai.ji65;

import lombok.*;

import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

@Data
@Builder(toBuilder = true)
public class Cpu {
    @Getter(AccessLevel.NONE)
    private final Map<Address, Byte> memory = new HashMap<>();

    @Getter(AccessLevel.NONE)
    private final Deque<Byte> stack = new LinkedList<>();

    private byte accumulator;
    private byte x;
    private byte y;
    @Builder.Default
    private byte stackPointer = (byte) 0xFF;
    private byte programCounter;

    private boolean carryFlagSet;
    private boolean zeroFlagSet;
    private boolean interruptDisableSet;
    private boolean decimalModeSet;
    private boolean breakCommandSet;
    private boolean overflowFlagSet;
    private boolean negativeFlagSet;

    public void setMemoryValue(Address address, byte value) {
        memory.put(address, value);
    }

    public byte getMemoryValue(Address address) {
        return memory.getOrDefault(address, (byte) 0x00);
    }

    public byte pullFromStack() {
        byte value = stack.pop();
        stackPointer = (byte) (stackPointer + 1);
        return value;
    }

    public void pushOntoStack(byte value) {
        stack.push(value);
        stackPointer = (byte) (stackPointer - 1);
    }

    public void updateZeroFlag(byte value) {
        zeroFlagSet = value == 0;
    }

    public void updateNegativeFlag(byte value) {
        negativeFlagSet = Byte.toUnsignedInt(value) >= 128;
    }
}
