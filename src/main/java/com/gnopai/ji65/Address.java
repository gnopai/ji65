package com.gnopai.ji65;

import lombok.Value;

@Value
public class Address {
    int value;

    public Address(int value) {
        this.value = value % (256 * 256);
    }

    public static Address of(byte highByte, byte lowByte) {
        int value = Byte.toUnsignedInt(highByte) * 256 + Byte.toUnsignedInt(lowByte);
        return new Address(value);
    }

    public boolean isZeroPage() {
        return value < 256;
    }

    public Address plus(int value) {
        return new Address(this.value + value);
    }

    public byte getHighByte() {
        return (byte) (value / 256);
    }

    public byte getLowByte() {
        return (byte) (value % 256);
    }

    @Override
    public String toString() {
        return String.format("$%04X", value);
    }
}
