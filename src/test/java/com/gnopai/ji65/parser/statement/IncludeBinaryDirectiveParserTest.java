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

class IncludeBinaryDirectiveParserTest {
    private final ErrorHandler errorHandler = mock(ErrorHandler.class);

    @Test
    void testHappyPath() {
        Statement result = parse(errorHandler,
                token(DIRECTIVE, DirectiveType.INCLUDE_BINARY),
                token(CHAR, (int) 'w'),
                token(CHAR, (int) 'h'),
                token(CHAR, (int) 'e'),
                token(CHAR, (int) 'e'),
                token(CHAR, (int) '.'),
                token(CHAR, (int) 'c'),
                token(CHAR, (int) 'h'),
                token(CHAR, (int) 'r'),
                token(EOL)
        );

        DirectiveStatement expectedResult = DirectiveStatement.builder()
                .type(DirectiveType.INCLUDE_BINARY)
                .name("whee.chr")
                .build();
        assertEquals(expectedResult, result);
    }

    @Test
    void testMissingFileName() {
        ParseException exception = assertThrows(ParseException.class, () -> parse(errorHandler,
                token(DIRECTIVE, DirectiveType.INCLUDE_BINARY),
                token(EOL)
        ));
        assertEquals(token(EOL), exception.getToken());
        assertEquals("Expected file name", exception.getMessage());
    }

    @Test
    void testMissingEndOfLine() {
        ParseException exception = assertThrows(ParseException.class, () -> parse(errorHandler,
                token(DIRECTIVE, DirectiveType.INCLUDE_BINARY),
                token(CHAR, (int) 'w'),
                token(CHAR, (int) 'h'),
                token(CHAR, (int) 'e'),
                token(CHAR, (int) 'e'),
                token(CHAR, (int) '.'),
                token(CHAR, (int) 'c'),
                token(CHAR, (int) 'h'),
                token(CHAR, (int) 'r'),
                token(EOF)
        ));
        assertEquals(token(EOF), exception.getToken());
        assertEquals("Expected end of line", exception.getMessage());
    }
}