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

class TestDirectiveParserTest {
    private final ErrorHandler errorHandler = mock(ErrorHandler.class);

    @Test
    void testHappyPath_noTestStatements() {
        Statement result = parse(errorHandler,
                token(DIRECTIVE, DirectiveType.TEST),
                token(IDENTIFIER, "test_test"),
                token(EOL),
                token(DIRECTIVE, DirectiveType.TEST_END),
                token(EOL)
        );

        DirectiveStatement expectedResult = DirectiveStatement.builder()
                .type(DirectiveType.TEST)
                .name("test_test")
                .statements(List.of())
                .build();
        assertEquals(expectedResult, result);
    }

    @Test
    void testHappyPath_multipleTestStatements() {
        Statement result = parse(errorHandler,
                token(DIRECTIVE, DirectiveType.TEST),
                token(IDENTIFIER, "test_test"),
                token(EOL),
                token(DIRECTIVE, DirectiveType.TEST_SET),
                token(X),
                token(NUMBER, 666),
                token(EOL),
                token(DIRECTIVE, DirectiveType.TEST_RUN),
                token(NUMBER, 0x1234),
                token(EOL),
                token(DIRECTIVE, DirectiveType.TEST_ASSERT),
                token(X),
                token(NUMBER, 777),
                token(EOL),
                token(DIRECTIVE, DirectiveType.TEST_END),
                token(EOL)
        );

        DirectiveStatement expectedResult = DirectiveStatement.builder()
                .type(DirectiveType.TEST)
                .name("test_test")
                .statements(List.of(
                        TestStatement.builder()
                                .type(TestStatement.Type.SET)
                                .target(TestStatement.Target.X)
                                .value(new PrimaryExpression(NUMBER, 666))
                                .build(),
                        TestStatement.builder()
                                .type(TestStatement.Type.RUN)
                                .targetAddress(new PrimaryExpression(NUMBER, 0x1234))
                                .build(),
                        TestStatement.builder()
                                .type(TestStatement.Type.ASSERT)
                                .target(TestStatement.Target.X)
                                .value(new PrimaryExpression(NUMBER, 777))
                                .build()
                        ))
                .build();
        assertEquals(expectedResult, result);
    }

    @Test
    void testNonTestStatementInTestBlock() {
        ParseException parseException = assertThrows(ParseException.class, () -> parse(errorHandler,
                token(DIRECTIVE, DirectiveType.TEST),
                token(IDENTIFIER, "test_test"),
                token(EOL),
                token(DIRECTIVE, DirectiveType.TEST_RUN),
                token(NUMBER, 0x1234),
                token(EOL),
                token(DIRECTIVE, DirectiveType.RESERVE),
                token(NUMBER, 32),
                token(EOL),
                token(DIRECTIVE, DirectiveType.TEST_END),
                token(EOL)
        ));

        assertEquals("Only test statements are allowed in test blocks", parseException.getMessage());
    }

    @Test
    void testMissingTestIdentifier() {
        ParseException parseException = assertThrows(ParseException.class, () -> parse(errorHandler,
                token(DIRECTIVE, DirectiveType.TEST),
                token(EOL),
                token(DIRECTIVE, DirectiveType.TEST_END),
                token(EOL)
        ));

        assertEquals(token(EOL), parseException.getToken());
        assertEquals("Expected test identifier", parseException.getMessage());
    }

    @Test
    void testMissingTestEnd() {
        ParseException parseException = assertThrows(ParseException.class, () -> parse(errorHandler,
                token(DIRECTIVE, DirectiveType.TEST),
                token(IDENTIFIER, "test_test"),
                token(EOL),
                token(EOF)
        ));

        assertEquals(token(EOF), parseException.getToken());
        assertEquals("Failed to parse token", parseException.getMessage());
    }

    @Test
    void testMissingTestEndOfLine() {
        ParseException parseException = assertThrows(ParseException.class, () -> parse(errorHandler,
                token(DIRECTIVE, DirectiveType.TEST),
                token(IDENTIFIER, "test_test"),
                token(DIRECTIVE, DirectiveType.TEST_END),
                token(EOL),
                token(EOF)
        ));

        assertEquals(token(DIRECTIVE, DirectiveType.TEST_END), parseException.getToken());
        assertEquals("Expected end of line", parseException.getMessage());
    }

    @Test
    void testMissingTestEndEndOfLine() {
        ParseException parseException = assertThrows(ParseException.class, () -> parse(errorHandler,
                token(DIRECTIVE, DirectiveType.TEST),
                token(IDENTIFIER, "test_test"),
                token(EOL),
                token(DIRECTIVE, DirectiveType.TEST_END),
                token(AND),
                token(EOF)
        ));

        assertEquals(token(AND), parseException.getToken());
        assertEquals("Expected end of line", parseException.getMessage());
    }
}