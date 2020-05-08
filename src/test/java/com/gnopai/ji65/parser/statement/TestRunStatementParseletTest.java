package com.gnopai.ji65.parser.statement;

import com.gnopai.ji65.DirectiveType;
import com.gnopai.ji65.parser.ParseException;
import com.gnopai.ji65.parser.expression.PrimaryExpression;
import com.gnopai.ji65.test.TestStepType;
import com.gnopai.ji65.util.ErrorHandler;
import org.junit.jupiter.api.Test;

import static com.gnopai.ji65.parser.ParserTestUtil.parse;
import static com.gnopai.ji65.parser.ParserTestUtil.token;
import static com.gnopai.ji65.scanner.TokenType.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

class TestRunStatementParseletTest {
    private final ErrorHandler errorHandler = mock(ErrorHandler.class);

    @Test
    void testHappyPath() {
        Statement result = parse(errorHandler,
                token(DIRECTIVE, DirectiveType.TEST_RUN),
                token(NUMBER, 0x1234),
                token(EOL)
        );

        TestStatement expectedResult = TestStatement.builder()
                .type(TestStepType.RUN)
                .targetAddress(new PrimaryExpression(NUMBER, 0x1234))
                .build();
        assertEquals(expectedResult, result);
    }

    @Test
    void testBadTargetAddress() {
        ParseException parseException = assertThrows(ParseException.class, () -> parse(errorHandler,
                token(DIRECTIVE, DirectiveType.TEST_RUN),
                token(AND),
                token(NUMBER, 99),
                token(EOL)
        ));

        assertEquals(token(AND), parseException.getToken());
        assertEquals("Failed to parse token", parseException.getMessage());
    }

    @Test
    void testMissingTargetAddress() {
        ParseException parseException = assertThrows(ParseException.class, () -> parse(errorHandler,
                token(DIRECTIVE, DirectiveType.TEST_RUN),
                token(EOL)
        ));

        assertEquals(token(EOL), parseException.getToken());
        assertEquals("Failed to parse token", parseException.getMessage());
    }

    @Test
    void testMissingEndOfLine() {
        ParseException parseException = assertThrows(ParseException.class, () -> parse(errorHandler,
                token(DIRECTIVE, DirectiveType.TEST_RUN),
                token(NUMBER, 99),
                token(INSTRUCTION)
        ));

        assertEquals(token(INSTRUCTION), parseException.getToken());
        assertEquals("Expected end of line", parseException.getMessage());
    }
}