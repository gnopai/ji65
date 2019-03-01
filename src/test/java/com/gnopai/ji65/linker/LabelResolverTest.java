package com.gnopai.ji65.linker;

import com.gnopai.ji65.Opcode;
import com.gnopai.ji65.assembler.*;
import com.gnopai.ji65.parser.expression.Expression;
import com.gnopai.ji65.parser.expression.PrimaryExpression;
import com.gnopai.ji65.scanner.TokenType;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LabelResolverTest {

    @Test
    void test() {
        List<SegmentData> segmentData = List.of(
                new Label("one", false),
                new InstructionData(Opcode.LDA_ABSOLUTE, (byte) 0x00, (byte) 0x01),
                new RawData(List.of((byte) 0x01, (byte) 0x02, (byte) 0x03, (byte) 0x04)),
                new Label("two", false),
                new Label("three", false),
                new InstructionData(Opcode.SEI_IMPLICIT),
                new InstructionData(Opcode.SEI_IMPLICIT),
                new InstructionData(Opcode.SEI_IMPLICIT),
                new InstructionData(Opcode.SEI_IMPLICIT),
                new InstructionData(Opcode.SEI_IMPLICIT),
                new Label("four", false)
        );
        Segment codeSegment = new Segment("CODE", false, segmentData);

        Environment environment = new Environment();
        AssembledSegments assembledSegments = new AssembledSegments(Map.of("CODE", codeSegment), environment);

        new LabelResolver().resolve(assembledSegments, 0x7000);

        assertEquals(expectedExpression(0x7000), environment.get("one"));
        assertEquals(expectedExpression(0x7007), environment.get("two"));
        assertEquals(expectedExpression(0x7007), environment.get("three"));
        assertEquals(expectedExpression(0x700C), environment.get("four"));
    }

    private Optional<Expression> expectedExpression(int value) {
        return Optional.of(new PrimaryExpression(TokenType.NUMBER, value));
    }
}