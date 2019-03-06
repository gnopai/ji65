package com.gnopai.ji65;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;

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

    @ParameterizedTest
    @MethodSource("setProcessorStatusArgumentsProvider")
    void testSetProcessorStatus(int initialStatus, Consumer<Cpu> action, int expectedStatus) {
        Cpu cpu = Cpu.builder()
                .processorStatus((byte) initialStatus)
                .build();
        action.accept(cpu);
        assertEquals((byte) expectedStatus, cpu.getProcessorStatus());
    }

    static Stream<Arguments> setProcessorStatusArgumentsProvider() {
        return Stream.of(
                arguments(0b00000000, (Consumer<Cpu>) cpu -> {
                }, 0b00000000),
                arguments(0b00000000, (Consumer<Cpu>) cpu -> cpu.setCarryFlag(true), 0b10000000),
                arguments(0b00000000, (Consumer<Cpu>) cpu -> cpu.setZeroFlag(true), 0b01000000),
                arguments(0b00000000, (Consumer<Cpu>) cpu -> cpu.setInterruptDisable(true), 0b00100000),
                arguments(0b00000000, (Consumer<Cpu>) cpu -> cpu.setDecimalMode(true), 0b00010000),
                arguments(0b00000000, (Consumer<Cpu>) cpu -> cpu.setBreakCommand(true), 0b00001000),
                arguments(0b00000000, (Consumer<Cpu>) cpu -> cpu.setOverflowFlag(true), 0b00000100),
                arguments(0b00000000, (Consumer<Cpu>) cpu -> cpu.setNegativeFlag(true), 0b00000010),
                arguments(0b11111110, (Consumer<Cpu>) cpu -> {
                }, 0b11111110),
                arguments(0b11111110, (Consumer<Cpu>) cpu -> cpu.setCarryFlag(false), 0b01111110),
                arguments(0b11111110, (Consumer<Cpu>) cpu -> cpu.setZeroFlag(false), 0b10111110),
                arguments(0b11111110, (Consumer<Cpu>) cpu -> cpu.setInterruptDisable(false), 0b11011110),
                arguments(0b11111110, (Consumer<Cpu>) cpu -> cpu.setDecimalMode(false), 0b11101110),
                arguments(0b11111110, (Consumer<Cpu>) cpu -> cpu.setBreakCommand(false), 0b11110110),
                arguments(0b11111110, (Consumer<Cpu>) cpu -> cpu.setOverflowFlag(false), 0b11111010),
                arguments(0b11111110, (Consumer<Cpu>) cpu -> cpu.setNegativeFlag(false), 0b11111100),
                arguments(0b00000000, (Consumer<Cpu>) cpu -> {
                    cpu.setCarryFlag(true);
                    cpu.setZeroFlag(true);
                    cpu.setInterruptDisable(true);
                    cpu.setDecimalMode(true);
                    cpu.setBreakCommand(true);
                    cpu.setOverflowFlag(true);
                    cpu.setNegativeFlag(true);
                }, 0b11111110),
                arguments(0b11111110, (Consumer<Cpu>) cpu -> {
                    cpu.setCarryFlag(false);
                    cpu.setZeroFlag(false);
                    cpu.setInterruptDisable(false);
                    cpu.setDecimalMode(false);
                    cpu.setBreakCommand(false);
                    cpu.setOverflowFlag(false);
                    cpu.setNegativeFlag(false);
                }, 0b00000000)
        );
    }

    @Test
    void testGetProcessorStatus_noFlagsSet() {
        Cpu cpu = Cpu.builder().processorStatus((byte) 0b00000000).build();
        assertFalse(cpu.isCarryFlagSet());
        assertFalse(cpu.isZeroFlagSet());
        assertFalse(cpu.isInterruptDisableSet());
        assertFalse(cpu.isDecimalModeSet());
        assertFalse(cpu.isBreakCommandSet());
        assertFalse(cpu.isOverflowFlagSet());
        assertFalse(cpu.isNegativeFlagSet());
    }

    @Test
    void testGetProcessorStatus_allFlagsSet() {
        Cpu cpu = Cpu.builder().processorStatus((byte) 0b11111110).build();
        assertTrue(cpu.isCarryFlagSet());
        assertTrue(cpu.isZeroFlagSet());
        assertTrue(cpu.isInterruptDisableSet());
        assertTrue(cpu.isDecimalModeSet());
        assertTrue(cpu.isBreakCommandSet());
        assertTrue(cpu.isOverflowFlagSet());
        assertTrue(cpu.isNegativeFlagSet());
    }

    @Test
    void testGetProcessorStatus_carryFlag() {
        Cpu cpu = Cpu.builder().processorStatus((byte) 0b10000000).build();
        assertTrue(cpu.isCarryFlagSet());
        assertFalse(cpu.isZeroFlagSet());
        assertFalse(cpu.isInterruptDisableSet());
        assertFalse(cpu.isDecimalModeSet());
        assertFalse(cpu.isBreakCommandSet());
        assertFalse(cpu.isOverflowFlagSet());
        assertFalse(cpu.isNegativeFlagSet());
    }

    @Test
    void testGetProcessorStatus_zeroFlag() {
        Cpu cpu = Cpu.builder().processorStatus((byte) 0b01000000).build();
        assertFalse(cpu.isCarryFlagSet());
        assertTrue(cpu.isZeroFlagSet());
        assertFalse(cpu.isInterruptDisableSet());
        assertFalse(cpu.isDecimalModeSet());
        assertFalse(cpu.isBreakCommandSet());
        assertFalse(cpu.isOverflowFlagSet());
        assertFalse(cpu.isNegativeFlagSet());
    }

    @Test
    void testGetProcessorStatus_interruptDisable() {
        Cpu cpu = Cpu.builder().processorStatus((byte) 0b00100000).build();
        assertFalse(cpu.isCarryFlagSet());
        assertFalse(cpu.isZeroFlagSet());
        assertTrue(cpu.isInterruptDisableSet());
        assertFalse(cpu.isDecimalModeSet());
        assertFalse(cpu.isBreakCommandSet());
        assertFalse(cpu.isOverflowFlagSet());
        assertFalse(cpu.isNegativeFlagSet());
    }

    @Test
    void testGetProcessorStatus_decimalMode() {
        Cpu cpu = Cpu.builder().processorStatus((byte) 0b00010000).build();
        assertFalse(cpu.isCarryFlagSet());
        assertFalse(cpu.isZeroFlagSet());
        assertFalse(cpu.isInterruptDisableSet());
        assertTrue(cpu.isDecimalModeSet());
        assertFalse(cpu.isBreakCommandSet());
        assertFalse(cpu.isOverflowFlagSet());
        assertFalse(cpu.isNegativeFlagSet());
    }

    @Test
    void testGetProcessorStatus_breakCommand() {
        Cpu cpu = Cpu.builder().processorStatus((byte) 0b00001000).build();
        assertFalse(cpu.isCarryFlagSet());
        assertFalse(cpu.isZeroFlagSet());
        assertFalse(cpu.isInterruptDisableSet());
        assertFalse(cpu.isDecimalModeSet());
        assertTrue(cpu.isBreakCommandSet());
        assertFalse(cpu.isOverflowFlagSet());
        assertFalse(cpu.isNegativeFlagSet());
    }

    @Test
    void testGetProcessorStatus_overflowFlag() {
        Cpu cpu = Cpu.builder().processorStatus((byte) 0b00000100).build();
        assertFalse(cpu.isCarryFlagSet());
        assertFalse(cpu.isZeroFlagSet());
        assertFalse(cpu.isInterruptDisableSet());
        assertFalse(cpu.isDecimalModeSet());
        assertFalse(cpu.isBreakCommandSet());
        assertTrue(cpu.isOverflowFlagSet());
        assertFalse(cpu.isNegativeFlagSet());
    }

    @Test
    void testGetProcessorStatus_negativeFlag() {
        Cpu cpu = Cpu.builder().processorStatus((byte) 0b00000010).build();
        assertFalse(cpu.isCarryFlagSet());
        assertFalse(cpu.isZeroFlagSet());
        assertFalse(cpu.isInterruptDisableSet());
        assertFalse(cpu.isDecimalModeSet());
        assertFalse(cpu.isBreakCommandSet());
        assertFalse(cpu.isOverflowFlagSet());
        assertTrue(cpu.isNegativeFlagSet());
    }
}
