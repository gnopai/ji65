package com.gnopai.ji65.parser.statement;

import com.gnopai.ji65.DirectiveType;
import com.gnopai.ji65.parser.ParseException;
import com.gnopai.ji65.util.ErrorHandler;
import org.junit.jupiter.api.Test;

import static com.gnopai.ji65.parser.ParserTestUtil.parse;
import static com.gnopai.ji65.parser.ParserTestUtil.token;
import static com.gnopai.ji65.scanner.TokenType.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

class SegmentDirectiveParserTest {
    private final ErrorHandler errorHandler = mock(ErrorHandler.class);

    @Test
    void testHappyPath() {
        Statement result = parse(errorHandler,
                token(DIRECTIVE, DirectiveType.SEGMENT),
                token(STRING, "CODE"),
                token(EOL)
        );

        DirectiveStatement expectedResult = DirectiveStatement.builder()
                .type(DirectiveType.SEGMENT)
                .name("CODE")
                .build();
        assertEquals(expectedResult, result);
    }

    @Test
    void testNoSegmentName() {
        ParseException exception = assertThrows(ParseException.class, () -> parse(errorHandler,
                token(DIRECTIVE, DirectiveType.SEGMENT),
                token(NUMBER, 5),
                token(EOL)
        ));
        assertEquals(NUMBER, exception.getTokenType());
        assertEquals("Expected string with segment name", exception.getMessage());
    }

    @Test
    void testNoEndOfLine() {
        ParseException exception = assertThrows(ParseException.class, () -> parse(errorHandler,
                token(DIRECTIVE, DirectiveType.SEGMENT),
                token(STRING, "CODE"),
                token(SLASH)
        ));
        assertEquals(SLASH, exception.getTokenType());
        assertEquals("Expected end of line", exception.getMessage());
    }
}