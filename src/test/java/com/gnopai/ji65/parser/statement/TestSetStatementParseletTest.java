package com.gnopai.ji65.parser.statement;

import com.gnopai.ji65.DirectiveType;
import com.gnopai.ji65.parser.ParseException;
import com.gnopai.ji65.parser.expression.PrimaryExpression;
import com.gnopai.ji65.test.Target;
import com.gnopai.ji65.util.ErrorHandler;
import org.junit.jupiter.api.Test;

import static com.gnopai.ji65.parser.ParserTestUtil.parse;
import static com.gnopai.ji65.parser.ParserTestUtil.token;
import static com.gnopai.ji65.scanner.TokenType.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

class TestSetStatementParseletTest {
    private final ErrorHandler errorHandler = mock(ErrorHandler.class);

    @Test
    void testSetX() {
        Statement result = parse(errorHandler,
                token(DIRECTIVE, DirectiveType.TEST_SET),
                token(X),
                token(NUMBER, 99),
                token(EOL)
        );

        TestStatement expectedResult = TestStatement.builder()
                .type(TestStatement.Type.SET)
                .target(Target.X)
                .value(new PrimaryExpression(NUMBER, 99))
                .build();
        assertEquals(expectedResult, result);
    }

    @Test
    void testSetY() {
        Statement result = parse(errorHandler,
                token(DIRECTIVE, DirectiveType.TEST_SET),
                token(Y),
                token(NUMBER, 99),
                token(EOL)
        );

        TestStatement expectedResult = TestStatement.builder()
                .type(TestStatement.Type.SET)
                .target(Target.Y)
                .value(new PrimaryExpression(NUMBER, 99))
                .build();
        assertEquals(expectedResult, result);
    }

    @Test
    void testSetA() {
        Statement result = parse(errorHandler,
                token(DIRECTIVE, DirectiveType.TEST_SET),
                token(A),
                token(NUMBER, 99),
                token(EOL)
        );

        TestStatement expectedResult = TestStatement.builder()
                .type(TestStatement.Type.SET)
                .target(Target.A)
                .value(new PrimaryExpression(NUMBER, 99))
                .build();
        assertEquals(expectedResult, result);
    }

    @Test
    void testSetMemory() {
        Statement result = parse(errorHandler,
                token(DIRECTIVE, DirectiveType.TEST_SET),
                token(NUMBER, 0x1234),
                token(NUMBER, 99),
                token(EOL)
        );

        TestStatement expectedResult = TestStatement.builder()
                .type(TestStatement.Type.SET)
                .target(Target.MEMORY)
                .targetAddress(new PrimaryExpression(NUMBER, 0x1234))
                .value(new PrimaryExpression(NUMBER, 99))
                .build();
        assertEquals(expectedResult, result);
    }

    @Test
    void testSetMemory_badExpression() {
        ParseException parseException = assertThrows(ParseException.class, () -> parse(errorHandler,
                token(DIRECTIVE, DirectiveType.TEST_SET),
                token(AND),
                token(NUMBER, 99),
                token(EOL)
        ));

        assertEquals(token(AND), parseException.getToken());
        assertEquals("Failed to parse token", parseException.getMessage());
    }

    @Test
    void testSetMemory_missingAddressExpression() {
        ParseException parseException = assertThrows(ParseException.class, () -> parse(errorHandler,
                token(DIRECTIVE, DirectiveType.TEST_SET),
                token(NUMBER, 99),
                token(EOL)
        ));

        assertEquals(token(EOL), parseException.getToken());
        assertEquals("Failed to parse token", parseException.getMessage());
    }

    @Test
    void testSetRegister_missingValue() {
        ParseException parseException = assertThrows(ParseException.class, () -> parse(errorHandler,
                token(DIRECTIVE, DirectiveType.TEST_SET),
                token(X),
                token(EOL)
        ));

        assertEquals(token(EOL), parseException.getToken());
        assertEquals("Failed to parse token", parseException.getMessage());
    }

    @Test
    void testSetRegister_missingEndOfLine() {
        ParseException parseException = assertThrows(ParseException.class, () -> parse(errorHandler,
                token(DIRECTIVE, DirectiveType.TEST_SET),
                token(X),
                token(NUMBER, 99),
                token(INSTRUCTION)
        ));

        assertEquals(token(INSTRUCTION), parseException.getToken());
        assertEquals("Expected end of line", parseException.getMessage());
    }
}