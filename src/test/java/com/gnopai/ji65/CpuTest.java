package com.gnopai.ji65;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CpuTest {

    @Test
    public void testMemoryOperations() {
        Cpu cpu = Cpu.builder().build();
        Address address1 = new Address(0x1234);
        Address address2 = new Address(0x1235);

        // initial state
        assertEquals((byte) 0x00, cpu.getMemoryValue(address1));
        assertEquals((byte) 0x00, cpu.getMemoryValue(address2));

        // set value at first address
        byte value1 = 0x55;
        byte value2 = 0x77;
        cpu.setMemoryValue(address1, value1);
        assertEquals(value1, cpu.getMemoryValue(address1));
        cpu.setMemoryValue(address1, value2);
        assertEquals(value2, cpu.getMemoryValue(address1));
        assertEquals((byte) 0x00, cpu.getMemoryValue(address2));

        // set value at second address
        cpu.setMemoryValue(address2, value1);
        assertEquals(value2, cpu.getMemoryValue(address1));
        assertEquals(value1, cpu.getMemoryValue(address2));
    }

    @Test
    public void testStackOperations() {
        // initial state
        Cpu cpu = Cpu.builder().build();
        assertEquals((byte) 0xFF, cpu.getStackPointer());

        // push stuff
        byte value1 = 0x55;
        cpu.pushOntoStack(value1);
        assertEquals((byte) 0xFE, cpu.getStackPointer());

        byte value2 = 0x77;
        cpu.pushOntoStack(value2);
        assertEquals((byte) 0xFD, cpu.getStackPointer());

        // pull stuff
        assertEquals(value2, cpu.pullFromStack());
        assertEquals((byte) 0xFE, cpu.getStackPointer());
        assertEquals(value1, cpu.pullFromStack());
        assertEquals((byte) 0xFF, cpu.getStackPointer());
    }
}
