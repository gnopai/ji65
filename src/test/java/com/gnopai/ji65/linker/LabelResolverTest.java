package com.gnopai.ji65.linker;

import com.gnopai.ji65.Address;
import com.gnopai.ji65.Opcode;
import com.gnopai.ji65.assembler.*;
import com.gnopai.ji65.config.SegmentConfig;
import com.gnopai.ji65.parser.expression.Expression;
import com.gnopai.ji65.parser.expression.PrimaryExpression;
import com.gnopai.ji65.scanner.TokenType;
import org.junit.jupiter.api.Test;

import java.util.List;
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
                new Label("two-and-a-half", false),
                new ReservedData(14),
                new Label("three", false),
                new InstructionData(Opcode.SEI_IMPLICIT),
                new InstructionData(Opcode.SEI_IMPLICIT),
                new InstructionData(Opcode.SEI_IMPLICIT),
                new InstructionData(Opcode.SEI_IMPLICIT),
                new InstructionData(Opcode.SEI_IMPLICIT),
                new Label("four", false)
        );
        Segment codeSegment = new Segment(
                SegmentConfig.builder().segmentName("CODE").build(),
                segmentData);

        Environment environment = new Environment();
        MappedSegments mappedSegments = new MappedSegments(List.of(
                new MappedSegment(codeSegment, new Address(0x7000))
        ));

        new LabelResolver().resolve(mappedSegments, environment);

        assertEquals(expectedExpression(0x7000), environment.get("one"));
        assertEquals(expectedExpression(0x7007), environment.get("two"));
        assertEquals(expectedExpression(0x7007), environment.get("two-and-a-half"));
        assertEquals(expectedExpression(0x7015), environment.get("three"));
        assertEquals(expectedExpression(0x701A), environment.get("four"));
    }

    private Optional<Expression> expectedExpression(int value) {
        return Optional.of(new PrimaryExpression(TokenType.NUMBER, value));
    }
}