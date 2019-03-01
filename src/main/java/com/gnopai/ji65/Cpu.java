package com.gnopai.ji65;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.util.List;

@Data
@Builder(toBuilder = true)
public class Cpu {
    @Getter(AccessLevel.NONE)
    private final byte[] memory = new byte[65536];

    private byte accumulator;
    private byte x;
    private byte y;
    @Builder.Default
    private byte stackPointer = (byte) 0xFF;
    private int programCounter; // 2-byte value

    private boolean carryFlagSet;
    private boolean zeroFlagSet;
    private boolean interruptDisableSet;
    private boolean decimalModeSet;
    private boolean breakCommandSet;
    private boolean overflowFlagSet;
    private boolean negativeFlagSet;

    public void setMemoryValue(Address address, byte value) {
        memory[address.getValue()] = value;
    }

    public byte getMemoryValue(Address address) {
        return memory[address.getValue()];
    }

    public void copyToMemory(Address address, List<Byte> bytes) {
        int i = address.getValue();
        for (byte b : bytes) {
            memory[i++] = b;
        }
    }

    public byte pullFromStack() {
        stackPointer = (byte) (stackPointer + 1);
        return memory[getStackMemoryIndex()];
    }

    public void pushOntoStack(byte value) {
        memory[getStackMemoryIndex()] = value;
        stackPointer = (byte) (stackPointer - 1);
    }

    private int getStackMemoryIndex() {
        return 0x0100 + Byte.toUnsignedInt(stackPointer);
    }

    public void updateZeroFlag(byte value) {
        zeroFlagSet = value == 0;
    }

    public void updateNegativeFlag(byte value) {
        negativeFlagSet = Byte.toUnsignedInt(value) >= 128;
    }

    private void incrementProgramCounter() {
        programCounter = (programCounter + 1) % memory.length;
    }

    public void setProgramCounter(Address address) {
        setProgramCounter(address.getValue());
    }

    public void setProgramCounter(int value) {
        this.programCounter = value;
    }

    public byte nextProgramByte() {
        byte nextByte = memory[programCounter];
        incrementProgramCounter();
        return nextByte;
    }
}
