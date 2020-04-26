package com.gnopai.ji65.parser.statement;

import com.gnopai.ji65.DirectiveType;
import com.gnopai.ji65.parser.ParseException;
import com.gnopai.ji65.parser.ParsingService;
import com.gnopai.ji65.util.ErrorHandler;
import org.junit.jupiter.api.Test;

import java.nio.file.Paths;

import static com.gnopai.ji65.parser.ParserTestUtil.parse;
import static com.gnopai.ji65.parser.ParserTestUtil.token;
import static com.gnopai.ji65.scanner.TokenType.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class IncludeBinaryDirectiveParserTest {
    private final ErrorHandler errorHandler = mock(ErrorHandler.class);
    private final ParsingService parsingService = mock(ParsingService.class);

    @Test
    void testHappyPath() {
        when(parsingService.resolveFilePath("whee.chr"))
                .thenReturn(Paths.get("/some/dir/whee.chr"));

        Statement result = parse(errorHandler, parsingService,
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
                .name("/some/dir/whee.chr")
                .build();
        assertEquals(expectedResult, result);
    }

    @Test
    void testMissingFileName() {
        ParseException exception = assertThrows(ParseException.class, () -> parse(errorHandler, parsingService,
                token(DIRECTIVE, DirectiveType.INCLUDE_BINARY),
                token(EOL)
        ));
        assertEquals(token(EOL), exception.getToken());
        assertEquals("Expected file name", exception.getMessage());
    }

    @Test
    void testMissingEndOfLine() {
        when(parsingService.resolveFilePath("whee.chr"))
                .thenReturn(Paths.get("/some/dir/whee.chr"));

        ParseException exception = assertThrows(ParseException.class, () -> parse(errorHandler, parsingService,
                token(DIRECTIVE, DirectiveType.INCLUDE_BINARY),
                token(CHAR, (int) 'w'),
                token(CHAR, (int) 'h'),
                token(CHAR, (int) 'e'),
                token(CHAR, (int) 'e'),
                token(CHAR, (int) '.'),
                token(CHAR, (int) 'c'),
                token(CHAR, (int) 'h'),
                token(CHAR, (int) 'r'),
                token(PLUS),
                token(EOF)
        ));
        assertEquals(token(PLUS), exception.getToken());
        assertEquals("Expected end of line", exception.getMessage());
    }
}