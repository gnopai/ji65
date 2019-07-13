package com.gnopai.ji65;

import com.gnopai.ji65.parser.ParseException;
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
                arguments("<$1234", 0x34),
                arguments(">$1234", 0x12),
                arguments("~$F0F0", 0x0F0F),
                arguments("(15)", 15),
                arguments("(!15)", 0),
                arguments("(!0)", 1),
                arguments("(15 + 2)", 17),
                arguments("(15 - 3)", 12),
                arguments("(7 * 2)", 14),
                arguments("(12 / 4)", 3),
                arguments("(3 = 2)", 0),
                arguments("(3 = 3)", 1),
                arguments("(3 <> 3)", 0),
                arguments("(3 <> 2)", 1),
                arguments("(3 > 2)", 1),
                arguments("(3 > 3)", 0),
                arguments("(2 > 3)", 0),
                arguments("(3 >= 2)", 1),
                arguments("(3 >= 3)", 1),
                arguments("(2 >= 3)", 0),
                arguments("(3 < 2)", 0),
                arguments("(3 < 3)", 0),
                arguments("(2 < 3)", 1),
                arguments("(3 <= 2)", 0),
                arguments("(3 <= 3)", 1),
                arguments("(2 <= 3)", 1),
                arguments("(2 && 3)", 1),
                arguments("(2 && 0)", 0),
                arguments("(0 && 0)", 0),
                arguments("(2 || 3)", 1),
                arguments("(2 || 0)", 1),
                arguments("(0 || 0)", 0),
                arguments("($0F & $14)", 0x04),
                arguments("($0F | $F0)", 0xFF),
                arguments("($FF ^ $F0)", 0x0F),
                arguments("%00011000 << 3", 0b11000000),
                arguments("%00011000 >> 2", 0b00000110),
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
}
