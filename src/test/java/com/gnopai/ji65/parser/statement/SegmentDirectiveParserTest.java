package com.gnopai.ji65.parser.statement;

import com.gnopai.ji65.directive.DirectiveType;
import com.gnopai.ji65.parser.ParseException;
import com.gnopai.ji65.parser.ParseletFactory;
import com.gnopai.ji65.parser.Parser;
import com.gnopai.ji65.parser.TokenConsumer;
import com.gnopai.ji65.scanner.Token;
import com.gnopai.ji65.scanner.TokenType;
import com.gnopai.ji65.util.ErrorHandler;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.gnopai.ji65.scanner.TokenType.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

class SegmentDirectiveParserTest {
    private final ErrorHandler errorHandler = mock(ErrorHandler.class);

    @Test
    void testHappyPath() {
        Statement result = parse(
                token(DIRECTIVE, DirectiveType.SEGMENT),
                token(STRING, "CODE")
        );

        DirectiveStatement expectedResult = DirectiveStatement.builder()
                .type(DirectiveType.SEGMENT)
                .name("CODE")
                .build();
        assertEquals(expectedResult, result);
    }

    @Test
    void testNoSegmentName() {
        ParseException exception = assertThrows(ParseException.class, () -> parse(
                token(DIRECTIVE, DirectiveType.SEGMENT),
                token(NUMBER, 5)
        ));
        assertEquals(NUMBER, exception.getTokenType());
        assertEquals("Expected string with segment name", exception.getMessage());
    }

    private Token token(TokenType type, Object value) {
        return new Token(type, "", value, 0);
    }

    private Statement parse(Token... tokens) {
        Parser parser = new Parser(
                new ParseletFactory(),
                new TokenConsumer(errorHandler, List.of(tokens))
        );
        return parser.statement();
    }
}