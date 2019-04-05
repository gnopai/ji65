package com.gnopai.ji65.parser.statement;

import com.gnopai.ji65.directive.DirectiveType;
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

class ReserveDirectiveParserTest {
    private final ErrorHandler errorHandler = mock(ErrorHandler.class);

    @Test
    void testHappyPath() {
        Statement result = parse(errorHandler,
                token(DIRECTIVE, DirectiveType.RESERVE),
                token(NUMBER, 32),
                token(EOL)
        );

        DirectiveStatement expectedResult = DirectiveStatement.builder()
                .type(DirectiveType.RESERVE)
                .expression(new PrimaryExpression(NUMBER, 32))
                .build();
        assertEquals(expectedResult, result);
    }

    @Test
    void testMissingSizeExpression() {
        ParseException exception = assertThrows(ParseException.class, () -> parse(errorHandler,
                token(DIRECTIVE, DirectiveType.RESERVE),
                token(EOL)
        ));
        assertEquals(EOL, exception.getTokenType());
        assertEquals("Failed to parse token", exception.getMessage());
    }

    @Test
    void testNoEndOfLine() {
        ParseException exception = assertThrows(ParseException.class, () -> parse(errorHandler,
                token(DIRECTIVE, DirectiveType.RESERVE),
                token(NUMBER, 32),
                token(INSTRUCTION)
        ));
        assertEquals(INSTRUCTION, exception.getTokenType());
        assertEquals("Expected end of line", exception.getMessage());
    }
}