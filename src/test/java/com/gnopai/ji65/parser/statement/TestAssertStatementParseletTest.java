package com.gnopai.ji65.parser.statement;

import com.gnopai.ji65.DirectiveType;
import com.gnopai.ji65.parser.ParseException;
import com.gnopai.ji65.parser.expression.PrimaryExpression;
import com.gnopai.ji65.test.Target;
import com.gnopai.ji65.test.TestStepType;
import com.gnopai.ji65.util.ErrorHandler;
import org.junit.jupiter.api.Test;

import static com.gnopai.ji65.parser.ParserTestUtil.parse;
import static com.gnopai.ji65.parser.ParserTestUtil.token;
import static com.gnopai.ji65.scanner.TokenType.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

class TestAssertStatementParseletTest {
    private final ErrorHandler errorHandler = mock(ErrorHandler.class);

    @Test
    void testAssertX() {
        Statement result = parse(errorHandler,
                token(DIRECTIVE, DirectiveType.TEST_ASSERT),
                token(X),
                token(NUMBER, 99),
                token(EOL)
        );

        TestStatement expectedResult = TestStatement.builder()
                .type(TestStepType.ASSERT)
                .target(Target.X)
                .value(new PrimaryExpression(NUMBER, 99))
                .build();
        assertEquals(expectedResult, result);
    }

    @Test
    void testAssertY() {
        Statement result = parse(errorHandler,
                token(DIRECTIVE, DirectiveType.TEST_ASSERT),
                token(Y),
                token(NUMBER, 99),
                token(EOL)
        );

        TestStatement expectedResult = TestStatement.builder()
                .type(TestStepType.ASSERT)
                .target(Target.Y)
                .value(new PrimaryExpression(NUMBER, 99))
                .build();
        assertEquals(expectedResult, result);
    }

    @Test
    void testAssertA() {
        Statement result = parse(errorHandler,
                token(DIRECTIVE, DirectiveType.TEST_ASSERT),
                token(A),
                token(NUMBER, 99),
                token(EOL)
        );

        TestStatement expectedResult = TestStatement.builder()
                .type(TestStepType.ASSERT)
                .target(Target.A)
                .value(new PrimaryExpression(NUMBER, 99))
                .build();
        assertEquals(expectedResult, result);
    }

    @Test
    void testAssertRegister_withMessage() {
        Statement result = parse(errorHandler,
                token(DIRECTIVE, DirectiveType.TEST_ASSERT),
                token(A),
                token(NUMBER, 99),
                token(CHAR, (int) 'W'),
                token(CHAR, (int) 'h'),
                token(CHAR, (int) 'e'),
                token(CHAR, (int) 'e'),
                token(CHAR, (int) '!'),
                token(EOL)
        );

        TestStatement expectedResult = TestStatement.builder()
                .type(TestStepType.ASSERT)
                .target(Target.A)
                .value(new PrimaryExpression(NUMBER, 99))
                .message("Whee!")
                .build();
        assertEquals(expectedResult, result);
    }

    @Test
    void testAssertMemory() {
        Statement result = parse(errorHandler,
                token(DIRECTIVE, DirectiveType.TEST_ASSERT),
                token(NUMBER, 0x1234),
                token(NUMBER, 99),
                token(EOL)
        );

        TestStatement expectedResult = TestStatement.builder()
                .type(TestStepType.ASSERT)
                .target(Target.MEMORY)
                .targetAddress(new PrimaryExpression(NUMBER, 0x1234))
                .value(new PrimaryExpression(NUMBER, 99))
                .build();
        assertEquals(expectedResult, result);
    }

    @Test
    void testAssertMemory_withMessage() {
        Statement result = parse(errorHandler,
                token(DIRECTIVE, DirectiveType.TEST_ASSERT),
                token(NUMBER, 0x1234),
                token(NUMBER, 99),
                token(CHAR, (int) 'W'),
                token(CHAR, (int) 'h'),
                token(CHAR, (int) 'e'),
                token(CHAR, (int) 'e'),
                token(CHAR, (int) '!'),
                token(EOL)
        );

        TestStatement expectedResult = TestStatement.builder()
                .type(TestStepType.ASSERT)
                .target(Target.MEMORY)
                .targetAddress(new PrimaryExpression(NUMBER, 0x1234))
                .value(new PrimaryExpression(NUMBER, 99))
                .message("Whee!")
                .build();
        assertEquals(expectedResult, result);
    }

    @Test
    void testAssertMemory_badExpression() {
        ParseException parseException = assertThrows(ParseException.class, () -> parse(errorHandler,
                token(DIRECTIVE, DirectiveType.TEST_ASSERT),
                token(AND),
                token(NUMBER, 99),
                token(EOL)
        ));

        assertEquals(token(AND), parseException.getToken());
        assertEquals("Failed to parse token", parseException.getMessage());
    }

    @Test
    void testAssertMemory_missingAddressExpression() {
        ParseException parseException = assertThrows(ParseException.class, () -> parse(errorHandler,
                token(DIRECTIVE, DirectiveType.TEST_ASSERT),
                token(NUMBER, 99),
                token(EOL)
        ));

        assertEquals(token(EOL), parseException.getToken());
        assertEquals("Failed to parse token", parseException.getMessage());
    }

    @Test
    void testAssertRegister_missingValue() {
        ParseException parseException = assertThrows(ParseException.class, () -> parse(errorHandler,
                token(DIRECTIVE, DirectiveType.TEST_ASSERT),
                token(X),
                token(EOL)
        ));

        assertEquals(token(EOL), parseException.getToken());
        assertEquals("Failed to parse token", parseException.getMessage());
    }

    @Test
    void testAssertRegister_missingEndOfLine() {
        ParseException parseException = assertThrows(ParseException.class, () -> parse(errorHandler,
                token(DIRECTIVE, DirectiveType.TEST_ASSERT),
                token(X),
                token(NUMBER, 99),
                token(INSTRUCTION)
        ));

        assertEquals(token(INSTRUCTION), parseException.getToken());
        assertEquals("Expected end of line", parseException.getMessage());
    }
}