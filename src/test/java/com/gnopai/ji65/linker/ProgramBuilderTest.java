package com.gnopai.ji65.linker;

import com.gnopai.ji65.Address;
import com.gnopai.ji65.Program;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ProgramBuilderTest {

    @Test
    void testSingleChunk() {
        Program program = new ProgramBuilder()
                .segment(mockSegment(2, 10))
                .label("start", 0x02)
                .bytes((byte) 0x15)
                .bytes(List.of((byte) 0xEF, (byte) 0x44, (byte) 0x01))
                .label("four", 0x06)
                .bytes((byte) 0xBC)
                .build();

        Program expectedProgram = new Program(List.of(
                new Program.Chunk(new Address(2), List.of(
                        (byte) 0x15,
                        (byte) 0xEF,
                        (byte) 0x44,
                        (byte) 0x01,
                        (byte) 0xBC,
                        (byte) 0x00,
                        (byte) 0x00,
                        (byte) 0x00,
                        (byte) 0x00,
                        (byte) 0x00)
                )),
                Map.of("start", 2, "four", 6)
        );
        assertEquals(expectedProgram, program);
    }

    @Test
    void testMultipleChunks() {
        Program program = new ProgramBuilder()
                .segment(mockSegment(2, 4))
                .label("derp", 0x02)
                .bytes((byte) 0x15)
                .bytes(List.of((byte) 0xEF, (byte) 0x44, (byte) 0x01))
                .segment(mockSegment(8, 7))
                .label("start", 0x06)
                .bytes((byte) 0xBC)
                .build();

        Program expectedProgram = new Program(List.of(
                new Program.Chunk(new Address(2), List.of(
                        (byte) 0x15,
                        (byte) 0xEF,
                        (byte) 0x44,
                        (byte) 0x01)),
                new Program.Chunk(new Address(8), List.of(
                        (byte) 0xBC,
                        (byte) 0x00,
                        (byte) 0x00,
                        (byte) 0x00,
                        (byte) 0x00,
                        (byte) 0x00,
                        (byte) 0x00))
        ),
                Map.of("derp", 2, "start", 6)
        );
        assertEquals(expectedProgram, program);
    }

    private MappedSegment mockSegment(int startAddress, int size) {
        MappedSegment mappedSegment = mock(MappedSegment.class);
        when(mappedSegment.getStartAddress()).thenReturn(new Address(startAddress));
        when(mappedSegment.getSize()).thenReturn(size);
        return mappedSegment;
    }
}