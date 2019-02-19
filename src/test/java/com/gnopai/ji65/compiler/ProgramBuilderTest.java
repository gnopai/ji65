package com.gnopai.ji65.compiler;

import com.gnopai.ji65.Address;
import com.gnopai.ji65.Program;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ProgramBuilderTest {
    @Test
    void test() {
        Program program = new ProgramBuilder(10, 2)
                .label("zero")
                .bytes((byte) 0x15)
                .bytes(List.of((byte) 0xEF, (byte) 0x44, (byte) 0x01))
                .label("four")
                .bytes((byte) 0xBC)
                .build();

        Program expectedProgram = new Program(List.of(
                (byte) 0x15,
                (byte) 0xEF,
                (byte) 0x44,
                (byte) 0x01,
                (byte) 0xBC,
                (byte) 0x00,
                (byte) 0x00,
                (byte) 0x00,
                (byte) 0x00,
                (byte) 0x00),
                Map.of("zero", 2, "four", 6),
                new Address(2)
        );
        assertEquals(expectedProgram, program);
    }
}