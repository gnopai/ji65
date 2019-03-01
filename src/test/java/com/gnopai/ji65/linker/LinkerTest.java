package com.gnopai.ji65.linker;

import com.gnopai.ji65.Address;
import com.gnopai.ji65.Opcode;
import com.gnopai.ji65.Program;
import com.gnopai.ji65.assembler.*;
import com.gnopai.ji65.parser.expression.ExpressionEvaluator;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class LinkerTest {
    private final LabelResolver labelResolver = mock(LabelResolver.class);
    private final ExpressionEvaluator expressionEvaluator = mock(ExpressionEvaluator.class);

    @Test
    void testVisitInstructionData() {
        InstructionData instructionData = new InstructionData(Opcode.LDA_ABSOLUTE_X, new RawData((byte) 80, (byte) 4));
        AssembledSegments assembledSegments = assembledSegments(new Environment(), instructionData);

        Program program = new Linker(labelResolver, expressionEvaluator).link(assembledSegments, 3, 0);

        List<Byte> expectedBytes = List.of((byte) 0xBD, (byte) 80, (byte) 4);
        assertEquals(new Program(expectedBytes), program);
    }

    @Test
    void testVisitRawData() {
        List<Byte> bytes = List.of((byte) 5, (byte) 7);
        AssembledSegments assembledSegments = assembledSegments(new Environment(), new RawData(bytes));

        Program program = new Linker(labelResolver, expressionEvaluator).link(assembledSegments, 2, 0);

        assertEquals(new Program(bytes), program);
    }

    @Test
    void testVisitLabel() {
        Environment environment = new Environment();
        Label label = new Label("derp", false);
        environment.define("derp", label);
        when(expressionEvaluator.evaluate(label, environment)).thenReturn(17);
        AssembledSegments assembledSegments = assembledSegments(environment, label);

        Program program = new Linker(labelResolver, expressionEvaluator).link(assembledSegments, 2, 0);

        Program expectedProgram = new Program(
                List.of((byte) 0, (byte) 0),
                Map.of("derp", 17),
                new Address(0)
        );
        assertEquals(expectedProgram, program);
    }

    @Test
    void testMissingCodeSegment() {
        AssembledSegments assembledSegments = new AssembledSegments(new Environment());
        RuntimeException runtimeException = assertThrows(RuntimeException.class, () -> new Linker(labelResolver, expressionEvaluator).link(assembledSegments));
        assertEquals("Expected at least a code segment for now", runtimeException.getMessage());
    }

    @Test
    void testLink_multipleSegmentData() {
        Environment environment = new Environment();
        Label label = new Label("derp", false);
        environment.define("derp", label);
        when(expressionEvaluator.evaluate(label, environment)).thenReturn(7);

        AssembledSegments assembledSegments = assembledSegments(environment,
                new InstructionData(Opcode.LDX_IMMEDIATE, new RawData((byte) 77)),
                new InstructionData(Opcode.LDX_ABSOLUTE, new RawData((byte) 80, (byte) 4)),
                new InstructionData(Opcode.STX_ZERO_PAGE, new RawData((byte) 9)),
                label,
                new InstructionData(Opcode.INX_IMPLICIT),
                new InstructionData(Opcode.STX_ZERO_PAGE, new RawData((byte) 10))
        );

        Program program = new Linker(labelResolver, expressionEvaluator).link(assembledSegments, 12, 0);

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

    private AssembledSegments assembledSegments(Environment environment, SegmentData... segmentData) {
        return new AssembledSegments(Map.of("CODE",
                new Segment("CODE", false, List.of(segmentData))
        ), environment);
    }
}