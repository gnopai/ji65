package com.gnopai.ji65;

import com.gnopai.ji65.parser.ParseException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static com.gnopai.ji65.TestUtil.assembleAndRun;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class ExpressionFunctionalTest {

    @ParameterizedTest
    @MethodSource("validExpressionProvider")
    void testExpression(String programText, int expectedValue) {
        Cpu cpu = Cpu.builder().build();
        assembleAndRun(cpu, "five = 5", "seven = 7", "lda #" + programText);
        assertEquals((byte) expectedValue, cpu.getAccumulator());
    }

    @ParameterizedTest
    @MethodSource("validExpressionProvider")
    void testAssignment(String programText, int expectedValue) {
        Cpu cpu = Cpu.builder().build();
        assembleAndRun(cpu, "five = 5", "seven = 7", "testy = " + programText, "lda #testy");
        assertEquals((byte) expectedValue, cpu.getAccumulator());
    }

    static Stream<Arguments> validExpressionProvider() {
        return Stream.of(
                arguments("15", 15),
                arguments("-15", -15),
                arguments("(15)", 15),
                arguments("(15 + 2)", 17),
                arguments("(15 - 3)", 12),
                arguments("(7 * 2)", 14),
                arguments("(12 / 4)", 3),
                arguments("(-(-5))", 5),
                arguments("(5 + 2 * 3)", 11),
                arguments("((5 + 2) * 3)", 21),
                arguments("5 + 2", 7),
                arguments("(2 * (3 * (13 - (3 + 6))) - 1)", 23),
                arguments("five", 5),
                arguments("(five + 4)", 9),
                arguments("(seven - five)", 2)
        );
    }

    @ParameterizedTest
    @MethodSource("invalidExpressionProvider")
    void testInvalidExpressions(String programText) {
        Cpu cpu = Cpu.builder().build();
        ParseException exception = assertThrows(ParseException.class, () ->
                assembleAndRun(cpu, "lda " + programText)
        );
        assertNotNull(exception.getMessage());
        assertNotNull(exception.getToken());
    }

    static Stream<Arguments> invalidExpressionProvider() {
        return Stream.of(
                arguments("#(15"),
                arguments("#("),
                arguments("#(5 + 2) * 4 )")
        );
    }

    @Test
    void testDuplicateAssignment() {
        Cpu cpu = Cpu.builder().build();
        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                assembleAndRun(cpu, "derp = 5", "derp = 7")
        );
        assertEquals("Duplicate declaration of \"derp\"", exception.getMessage());
    }
}
