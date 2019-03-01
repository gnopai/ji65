package com.gnopai.ji65;


import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CpuTest {

    @Test
    void testMemoryOperations() {
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
    void testCopyToMemory() {
        Cpu cpu = Cpu.builder().build();

        List<Byte> bytes = List.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5);
        Address address = new Address(0x1234);

        cpu.copyToMemory(address, bytes);

        assertEquals((byte) 0, cpu.getMemoryValue(new Address(0x1233)));
        assertEquals((byte) 1, cpu.getMemoryValue(new Address(0x1234)));
        assertEquals((byte) 2, cpu.getMemoryValue(new Address(0x1235)));
        assertEquals((byte) 3, cpu.getMemoryValue(new Address(0x1236)));
        assertEquals((byte) 4, cpu.getMemoryValue(new Address(0x1237)));
        assertEquals((byte) 5, cpu.getMemoryValue(new Address(0x1238)));
        assertEquals((byte) 0, cpu.getMemoryValue(new Address(0x1239)));
    }

    @Test
    void testStackOperations() {
        // initial state
        Cpu cpu = Cpu.builder().build();
        assertEquals((byte) 0xFF, cpu.getStackPointer());

        // push stuff
        byte value1 = 0x55;
        cpu.pushOntoStack(value1);
        assertEquals((byte) 0xFE, cpu.getStackPointer());
        assertEquals(value1, cpu.getMemoryValue(new Address(0x01FF)));

        byte value2 = 0x77;
        cpu.pushOntoStack(value2);
        assertEquals((byte) 0xFD, cpu.getStackPointer());
        assertEquals(value2, cpu.getMemoryValue(new Address(0x01FE)));

        // pull stuff
        assertEquals(value2, cpu.pullFromStack());
        assertEquals((byte) 0xFE, cpu.getStackPointer());
        assertEquals(value1, cpu.pullFromStack());
        assertEquals((byte) 0xFF, cpu.getStackPointer());
    }
}
