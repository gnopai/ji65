package com.gnopai.ji65;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Operand {
    BytesValue value;
    boolean address;

    public static Operand of(Address address) {
        return new Operand(BytesValue.of(address), true);
    }

    public Address asAddress() {
        return value.asAddress();
    }

    public byte getLowByte() {
        return value.getLowByte();
    }
}
