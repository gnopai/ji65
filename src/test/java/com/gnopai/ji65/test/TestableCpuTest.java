package com.gnopai.ji65.test;

import com.gnopai.ji65.Address;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TestableCpuTest {

    @Test
    void testWatchReadCount() {
        Address address = new Address(0x54FE);
        TestableCpu testClass = new TestableCpu();
        testClass.addWatch(address);

        assertEquals(0, testClass.getWatchReadCount(address));

        testClass.getMemoryValue(address);
        assertEquals(1, testClass.getWatchReadCount(address));

        testClass.getMemoryValue(address);
        testClass.getMemoryValue(address);
        assertEquals(3, testClass.getWatchReadCount(address));
    }

    @Test
    void testWatchBytesWritten() {
        Address address = new Address(0x54FE);
        TestableCpu testClass = new TestableCpu();
        testClass.addWatch(address);

        assertEquals(0, testClass.getMemoryValue(address));
        assertEquals(List.of(), testClass.getWatchBytesWritten(address));

        byte value1 = (byte) 0x88;
        testClass.setMemoryValue(address, value1);
        assertEquals(value1, testClass.getMemoryValue(address));
        assertEquals(List.of(value1), testClass.getWatchBytesWritten(address));

        byte value2 = (byte) 0xED;
        testClass.setMemoryValue(address, value2);
        assertEquals(value2, testClass.getMemoryValue(address));
        assertEquals(List.of(value1, value2), testClass.getWatchBytesWritten(address));
    }

    @Test
    void testWatchReadsAndWritesTogether() {
        Address address = new Address(0x54FE);
        TestableCpu testClass = new TestableCpu();
        testClass.addWatch(address);

        assertEquals(0, testClass.getWatchReadCount(address));
        assertEquals(List.of(), testClass.getWatchBytesWritten(address));
        assertEquals(0, testClass.getMemoryValue(address));

        byte value1 = (byte) 0x88;
        testClass.setMemoryValue(address, value1);
        assertEquals(value1, testClass.getMemoryValue(address));

        byte value2 = (byte) 0xED;
        testClass.setMemoryValue(address, value2);
        assertEquals(value2, testClass.getMemoryValue(address));

        assertEquals(3, testClass.getWatchReadCount(address));
        assertEquals(List.of(value1, value2), testClass.getWatchBytesWritten(address));
    }

    @Test
    void testWatchNotPresent() {
        TestableCpu testClass = new TestableCpu();
        Address badAddress = new Address(0x9987);
        String expectedErrorMessage = "No watch defined at address $9987";
        RuntimeException exception;

        exception = assertThrows(RuntimeException.class, () -> testClass.getWatchReadCount(badAddress));
        assertEquals(expectedErrorMessage, exception.getMessage());

        exception = assertThrows(RuntimeException.class, () -> testClass.getWatchBytesWritten(badAddress));
        assertEquals(expectedErrorMessage, exception.getMessage());
    }

    @Test
    void testMockMemoryValues() {
        TestableCpu testClass = new TestableCpu();
        Address address = new Address(0x9987);
        byte value1 = (byte) 0x5F;
        byte value2 = (byte) 0xDE;
        byte value3 = (byte) 0x01;

        testClass.mockMemoryValues(address, value1, value2, value3);

        assertEquals(value1, testClass.getMemoryValue(address));
        assertEquals(value2, testClass.getMemoryValue(address));
        assertEquals(value3, testClass.getMemoryValue(address));
        assertEquals(value3, testClass.getMemoryValue(address));
    }

    @Test
    void testGetMemoryValue_notMocked() {
        TestableCpu testClass = new TestableCpu();
        Address address = new Address(0x9987);
        byte value = (byte) 0x44;

        assertEquals(0, testClass.getMemoryValue(address));
        testClass.setMemoryValue(address, value);
        assertEquals(value, testClass.getMemoryValue(address));
    }
}