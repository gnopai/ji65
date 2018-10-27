package com.gnopai.ji65;

import lombok.Value;

@Value
public class BytesValue {
    byte lowByte; // first byte or low byte
    byte highByte; // second byte or high byte

    public BytesValue(byte lowByte) {
        this(lowByte, (byte) 0x00);
    }

    public BytesValue(byte lowByte, byte highByte) {
        this.lowByte = lowByte;
        this.highByte = highByte;
    }

    public static BytesValue of(Address address) {
        return new BytesValue(address.getLowByte(), address.getHighByte());
    }

    public Address asAddress() {
        return Address.of(highByte, lowByte);
    }
}
