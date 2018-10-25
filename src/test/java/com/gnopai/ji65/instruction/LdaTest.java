package com.gnopai.ji65.instruction;

import com.gnopai.ji65.Address;
import com.gnopai.ji65.Cpu;
import com.gnopai.ji65.Operand;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class LdaTest {

    @Test
    public void testImmediate() {
        Cpu cpu = Cpu.builder().build();
        byte value = (byte) 0xFF;
        Operand operand = Operand.builder()
                .lowByte(value)
                .address(false)
                .build();
        Lda testClass = new Lda();

        testClass.run(cpu, operand);

        assertEquals(value, cpu.getAccumulator());
    }

    @Test
    public void testAddress() {
        Address address = new Address(0x1234);
        byte value = (byte) 0xB4;
        Cpu cpu = Cpu.builder().build();
        cpu.setMemoryValue(address, value);
        Operand operand = Operand.of(address);
        Lda testClass = new Lda();

        testClass.run(cpu, operand);

        assertEquals(value, cpu.getAccumulator());
    }
}
