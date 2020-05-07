package com.gnopai.ji65.parser.statement;

import com.gnopai.ji65.DirectiveType;
import com.gnopai.ji65.parser.ParseException;
import com.gnopai.ji65.parser.expression.PrimaryExpression;
import com.gnopai.ji65.util.ErrorHandler;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.gnopai.ji65.parser.ParserTestUtil.parse;
import static com.gnopai.ji65.parser.ParserTestUtil.token;
import static com.gnopai.ji65.scanner.TokenType.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

class TestVerifyWriteStatementParseletTest {
    private final ErrorHandler errorHandler = mock(ErrorHandler.class);

    @Test
    void testZeroPageAddress_singleValue() {
        Statement result = parse(errorHandler,
                token(DIRECTIVE, DirectiveType.TEST_VERIFY_WRITE),
                token(NUMBER, 0x28),
                token(NUMBER, 0xEF),
                token(EOL)
        );

        TestStatement expectedResult = TestStatement.builder()
                .type(TestStatement.Type.VERIFY_WRITE)
                .targetAddress(new PrimaryExpression(NUMBER, 0x28))
                .value(new PrimaryExpression(NUMBER, 0xEF))
                .build();
        assertEquals(expectedResult, result);
    }

    @Test
    void testAbsoluteAddress_singleValue() {
        Statement result = parse(errorHandler,
                token(DIRECTIVE, DirectiveType.TEST_VERIFY_WRITE),
                token(NUMBER, 0x2088),
                token(NUMBER, 0xFF),
                token(EOL)
        );

        TestStatement expectedResult = TestStatement.builder()
                .type(TestStatement.Type.VERIFY_WRITE)
                .targetAddress(new PrimaryExpression(NUMBER, 0x2088))
                .value(new PrimaryExpression(NUMBER, 0xFF))
                .build();
        assertEquals(expectedResult, result);
    }

    @Test
    void testAbsoluteAddress_mutipleValues() {
        Statement result = parse(errorHandler,
                token(DIRECTIVE, DirectiveType.TEST_VERIFY_WRITE),
                token(NUMBER, 0x2088),
                token(NUMBER, 0xFF),
                token(COMMA),
                token(NUMBER, 0x94),
                token(COMMA),
                token(NUMBER, 0x01),
                token(EOL)
        );

        TestStatement expectedResult = TestStatement.builder()
                .type(TestStatement.Type.VERIFY_WRITE)
                .targetAddress(new PrimaryExpression(NUMBER, 0x2088))
                .values(List.of(
                        new PrimaryExpression(NUMBER, 0xFF),
                        new PrimaryExpression(NUMBER, 0x94),
                        new PrimaryExpression(NUMBER, 0x01)
                ))
                .build();
        assertEquals(expectedResult, result);
    }

    @Test
    void testAbsoluteAddress_mutipleValuesButMissingComma() {
        ParseException exception = assertThrows(ParseException.class, () -> parse(errorHandler,
                token(DIRECTIVE, DirectiveType.TEST_VERIFY_WRITE),
                token(NUMBER, 0x2088),
                token(NUMBER, 0xFF),
                token(COMMA),
                token(NUMBER, 0x94),
                token(NUMBER, 0x01),
                token(EOL)
        ));

        assertEquals(token(NUMBER, 0x01), exception.getToken());
        assertEquals("Expected end of line", exception.getMessage());
    }

    @Test
    void testAbsoluteAddress_mutipleValuesButMissingEndOfLine() {
        ParseException exception = assertThrows(ParseException.class, () -> parse(errorHandler,
                token(DIRECTIVE, DirectiveType.TEST_VERIFY_WRITE),
                token(NUMBER, 0x2088),
                token(NUMBER, 0xFF),
                token(COMMA),
                token(NUMBER, 0x94),
                token(COMMA),
                token(NUMBER, 0x01),
                token(STRING, "foo")
        ));

        assertEquals(token(STRING, "foo"), exception.getToken());
        assertEquals("Expected end of line", exception.getMessage());
    }

    @Test
    void testAbsoluteAddress_missingValues() {
        ParseException exception = assertThrows(ParseException.class, () -> parse(errorHandler,
                token(DIRECTIVE, DirectiveType.TEST_VERIFY_WRITE),
                token(NUMBER, 0x2088),
                token(EOL)
        ));

        assertEquals(token(EOL), exception.getToken());
        assertEquals("Failed to parse token", exception.getMessage());
    }
}