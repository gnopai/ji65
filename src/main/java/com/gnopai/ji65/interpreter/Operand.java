package com.gnopai.ji65.interpreter;

import com.gnopai.ji65.Address;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Operand {
    byte highByte;
    byte lowByte;
    boolean address;

    public static Operand of(Address address) {
        return new Operand(address.getHighByte(), address.getLowByte(), true);
    }

    public Address asAddress() {
        return Address.of(highByte, lowByte);
    }
}
