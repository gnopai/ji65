package com.gnopai.ji65;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Operand {
    byte lowByte; // first byte or low byte
    byte highByte; // second byte or high byte
    boolean address;

    public static Operand of(Address address) {
        return builder()
                .lowByte(address.getLowByte())
                .highByte(address.getHighByte())
                .address(true)
                .build();
    }

    public Address asAddress() {
        return Address.of(highByte, lowByte);
    }
}
