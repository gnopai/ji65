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
    private byte processorStatus;

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
        setZeroFlag(value == 0);
    }

    public void updateNegativeFlag(byte value) {
        setNegativeFlag(Byte.toUnsignedInt(value) >= 128);
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

    public void setCarryFlag(boolean value) {
        updateProcessorStatus(value, 0b10000000);
    }

    public void setZeroFlag(boolean value) {
        updateProcessorStatus(value, 0b01000000);
    }

    public void setInterruptDisable(boolean value) {
        updateProcessorStatus(value, 0b00100000);
    }

    public void setDecimalMode(boolean value) {
        updateProcessorStatus(value, 0b00010000);
    }

    public void setBreakCommand(boolean value) {
        updateProcessorStatus(value, 0b00001000);
    }

    public void setOverflowFlag(boolean value) {
        updateProcessorStatus(value, 0b00000100);
    }

    public void setNegativeFlag(boolean value) {
        updateProcessorStatus(value, 0b00000010);
    }

    public boolean isCarryFlagSet() {
        return getProcessorStatus(0b10000000);
    }

    public boolean isCarryFlagClear() {
        return !isCarryFlagSet();
    }

    public boolean isZeroFlagSet() {
        return getProcessorStatus(0b01000000);
    }

    public boolean isZeroFlagClear() {
        return !isZeroFlagSet();
    }

    public boolean isInterruptDisableSet() {
        return getProcessorStatus(0b00100000);
    }

    public boolean isDecimalModeSet() {
        return getProcessorStatus(0b00010000);
    }

    public boolean isBreakCommandSet() {
        return getProcessorStatus(0b00001000);
    }

    public boolean isOverflowFlagSet() {
        return getProcessorStatus(0b00000100);
    }

    public boolean isOverflowFlagClear() {
        return !isOverflowFlagSet();
    }

    public boolean isNegativeFlagSet() {
        return getProcessorStatus(0b00000010);
    }

    public boolean isNegativeFlagClear() {
        return !isNegativeFlagSet();
    }

    private boolean getProcessorStatus(int mask) {
        return (processorStatus & mask) > 0;
    }

    private void updateProcessorStatus(boolean value, int mask) {
        if (value) {
            processorStatus |= mask;
        } else {
            processorStatus &= ~mask;
        }
    }
}
