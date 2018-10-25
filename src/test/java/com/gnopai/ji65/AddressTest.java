package com.gnopai.ji65;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AddressTest {

    @Test
    public void testOf() {
        Address address = Address.of((byte) 0xA2, (byte) 0x55);
        assertEquals(0xA255, address.getValue());
    }

    @Test
    public void testIsZeroPage() {
        assertTrue(new Address(0x0000).isZeroPage());
        assertTrue(new Address(0x00FF).isZeroPage());
        assertFalse(new Address(0x0100).isZeroPage());
        assertFalse(new Address(0xFFFF).isZeroPage());
    }

    @Test
    public void testGetHighByte() {
        assertEquals((byte) 0x00, new Address(0x0000).getHighByte());
        assertEquals((byte) 0xFF, new Address(0xFF00).getHighByte());
        assertEquals((byte) 0x12, new Address(0x1234).getHighByte());
    }

    @Test
    public void testGetLowByte() {
        assertEquals((byte) 0x00, new Address(0x0000).getLowByte());
        assertEquals((byte) 0xFF, new Address(0x00FF).getLowByte());
        assertEquals((byte) 0x34, new Address(0x1234).getLowByte());
    }

    @Test
    public void testPlus_noCarry() {
        Address address = new Address(0x55);
        Address newAddress = address.plus(0x25);
        assertEquals(new Address(0x7A), newAddress);
    }

    @Test
    public void testPlus_withCarry() {
        Address address = new Address(0x12F0);
        Address newAddress = address.plus(0x35);
        assertEquals(new Address(0x1325), newAddress);
    }
}
