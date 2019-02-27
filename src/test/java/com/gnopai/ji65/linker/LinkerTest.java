package com.gnopai.ji65.linker;

import com.gnopai.ji65.Address;
import com.gnopai.ji65.Opcode;
import com.gnopai.ji65.Program;
import com.gnopai.ji65.assembler.*;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class LinkerTest {

    @Test
    void testVisitInstructionData() {
        InstructionData instructionData = new InstructionData(Opcode.LDA_ABSOLUTE_X, new RawData((byte) 80, (byte) 4));
        AssembledSegments assembledSegments = assembledSegments(instructionData);

        Program program = new Linker().link(assembledSegments, 3, 0);

        List<Byte> expectedBytes = List.of((byte) 0xBD, (byte) 80, (byte) 4);
        assertEquals(new Program(expectedBytes), program);
    }

    @Test
    void testVisitRawData() {
        List<Byte> bytes = List.of((byte) 5, (byte) 7);
        AssembledSegments assembledSegments = assembledSegments(new RawData(bytes));

        Program program = new Linker().link(assembledSegments, 2, 0);

        assertEquals(new Program(bytes), program);
    }

    @Test
    void testVisitLabel() {
        AssembledSegments assembledSegments = assembledSegments(new Label("derp", false));

        Program program = new Linker().link(assembledSegments, 2, 0);

        Program expectedProgram = new Program(
                List.of((byte) 0, (byte) 0),
                Map.of("derp", 0),
                new Address(0)
        );
        assertEquals(expectedProgram, program);
    }

    @Test
    void testMissingCodeSegment() {
        RuntimeException runtimeException = assertThrows(RuntimeException.class, () -> new Linker().link(new AssembledSegments()));
        assertEquals("Expected at least a code segment for now", runtimeException.getMessage());
    }

    @Test
    void testLink_multipleSegmentData() {
        AssembledSegments assembledSegments = assembledSegments(
                new InstructionData(Opcode.LDX_IMMEDIATE, new RawData((byte) 77)),
                new InstructionData(Opcode.LDX_ABSOLUTE, new RawData((byte) 80, (byte) 4)),
                new InstructionData(Opcode.STX_ZERO_PAGE, new RawData((byte) 9)),
                new Label("derp", false),
                new InstructionData(Opcode.INX_IMPLICIT),
                new InstructionData(Opcode.STX_ZERO_PAGE, new RawData((byte) 10))
        );

        Program program = new Linker().link(assembledSegments, 12, 0);

        List<Byte> expectedBytes = List.of(
                (byte) 0xA2, (byte) 77,
                (byte) 0xAE, (byte) 80, (byte) 4,
                (byte) 0x86, (byte) 9,
                (byte) 0xE8,
                (byte) 0x86, (byte) 10,
                (byte) 0, (byte) 0
        );
        Map<String, Integer> expectedLabels = Map.of("derp", 7);
        assertEquals(new Program(expectedBytes, expectedLabels, new Address(0)), program);
    }

    private AssembledSegments assembledSegments(SegmentData... segmentData) {
        return new AssembledSegments(Map.of("CODE",
                new Segment("CODE", false, List.of(segmentData))
        ));
    }
}