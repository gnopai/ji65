package com.gnopai.ji65.linker;

import com.gnopai.ji65.NewProgram;
import com.gnopai.ji65.Opcode;
import com.gnopai.ji65.compiler.*;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class LinkerTest {

    @Test
    void testVisitInstructionData() {
        InstructionData instructionData = new InstructionData(Opcode.LDA_ABSOLUTE_X, new RawData((byte) 80, (byte) 4));
        CompiledSegments compiledSegments = compiledSegments(instructionData);

        NewProgram program = new Linker().link(compiledSegments);

        List<Byte> expectedBytes = List.of((byte) 0xBD, (byte) 80, (byte) 4);
        assertEquals(new NewProgram(expectedBytes), program);
    }

    @Test
    void testVisitRawData() {
        List<Byte> bytes = List.of((byte) 5, (byte) 7);
        CompiledSegments compiledSegments = compiledSegments(new RawData(bytes));

        NewProgram program = new Linker().link(compiledSegments);

        assertEquals(new NewProgram(bytes), program);
    }

    @Test
    void testVisitLabel() {
        CompiledSegments compiledSegments = compiledSegments(new Label("derp"));

        NewProgram program = new Linker().link(compiledSegments);

        assertEquals(new NewProgram(List.of()), program);
    }

    @Test
    void testMissingCodeSegment() {
        RuntimeException runtimeException = assertThrows(RuntimeException.class, () -> new Linker().link(new CompiledSegments()));
        assertEquals("Expected at least a code segment for now", runtimeException.getMessage());
    }

    @Test
    void testLink_multipleSegmentData() {
        CompiledSegments compiledSegments = compiledSegments(
                new Label("derp"),
                new InstructionData(Opcode.LDX_IMMEDIATE, new RawData((byte) 77)),
                new InstructionData(Opcode.LDX_ABSOLUTE, new RawData((byte) 80, (byte) 4)),
                new InstructionData(Opcode.STX_ZERO_PAGE, new RawData((byte) 9)),
                new InstructionData(Opcode.INX_IMPLICIT),
                new InstructionData(Opcode.STX_ZERO_PAGE, new RawData((byte) 10))
        );

        NewProgram program = new Linker().link(compiledSegments);

        List<Byte> expectedBytes = List.of(
                (byte) 0xA2, (byte) 77,
                (byte) 0xAE, (byte) 80, (byte) 4,
                (byte) 0x86, (byte) 9,
                (byte) 0xE8,
                (byte) 0x86, (byte) 10
        );
        assertEquals(new NewProgram(expectedBytes), program);
    }

    private CompiledSegments compiledSegments(SegmentData... segmentData) {
        return new CompiledSegments(Map.of("CODE",
                new Segment("CODE", List.of(segmentData))
        ));
    }
}