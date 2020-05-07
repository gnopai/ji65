package com.gnopai.ji65.parser.statement;

import com.gnopai.ji65.DirectiveType;
import com.gnopai.ji65.parser.ParseException;
import com.gnopai.ji65.parser.expression.PrimaryExpression;
import com.gnopai.ji65.util.ErrorHandler;
import org.junit.jupiter.api.Test;

import static com.gnopai.ji65.parser.ParserTestUtil.parse;
import static com.gnopai.ji65.parser.ParserTestUtil.token;
import static com.gnopai.ji65.scanner.TokenType.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

class TestVerifyReadStatementParseletTest {
    private final ErrorHandler errorHandler = mock(ErrorHandler.class);

    @Test
    void testZeroPageAddress_noCount() {
        Statement result = parse(errorHandler,
                token(DIRECTIVE, DirectiveType.TEST_VERIFY_READ),
                token(NUMBER, 0x28),
                token(EOL)
        );

        TestStatement expectedResult = TestStatement.builder()
                .type(TestStatement.Type.VERIFY_READ)
                .targetAddress(new PrimaryExpression(NUMBER, 0x28))
                .value(new PrimaryExpression(NUMBER, 1))
                .build();
        assertEquals(expectedResult, result);
    }

    @Test
    void testAbsoluteAddress_noCount() {
        Statement result = parse(errorHandler,
                token(DIRECTIVE, DirectiveType.TEST_VERIFY_READ),
                token(NUMBER, 0x2088),
                token(EOL)
        );

        TestStatement expectedResult = TestStatement.builder()
                .type(TestStatement.Type.VERIFY_READ)
                .targetAddress(new PrimaryExpression(NUMBER, 0x2088))
                .value(new PrimaryExpression(NUMBER, 1))
                .build();
        assertEquals(expectedResult, result);
    }

    @Test
    void testAbsoluteAddress_withCount() {
        Statement result = parse(errorHandler,
                token(DIRECTIVE, DirectiveType.TEST_VERIFY_READ),
                token(NUMBER, 0x2088),
                token(NUMBER, 3),
                token(EOL)
        );

        TestStatement expectedResult = TestStatement.builder()
                .type(TestStatement.Type.VERIFY_READ)
                .targetAddress(new PrimaryExpression(NUMBER, 0x2088))
                .value(new PrimaryExpression(NUMBER, 3))
                .build();
        assertEquals(expectedResult, result);
    }

    @Test
    void testAbsoluteAddress_extraJunkAfterCount() {
        ParseException exception = assertThrows(ParseException.class, () -> parse(errorHandler,
                token(DIRECTIVE, DirectiveType.TEST_VERIFY_READ),
                token(NUMBER, 0x2088),
                token(NUMBER, 6),
                token(COMMA),
                token(NUMBER, 9),
                token(EOL)
        ));

        assertEquals(token(COMMA), exception.getToken());
        assertEquals("Expected end of line", exception.getMessage());
    }
}