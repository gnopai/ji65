package com.gnopai.ji65.parser.statement;

import com.gnopai.ji65.util.ErrorHandler;
import org.junit.jupiter.api.Test;

import static com.gnopai.ji65.parser.ParserTestUtil.parse;
import static com.gnopai.ji65.parser.ParserTestUtil.token;
import static com.gnopai.ji65.scanner.TokenType.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

class LabelStatementParseletTest {
    private final ErrorHandler errorHandler = mock(ErrorHandler.class);

    @Test
    void testLabelStatement_named() {
        Statement result = parse(errorHandler,
                token(IDENTIFIER, "whee"),
                token(COLON),
                token(EOL)
        );

        LabelStatement expectedResult = new LabelStatement("whee");
        assertEquals(expectedResult, result);
    }

    @Test
    void testLabelStatement_named_noEndOfLine() {
        Statement result = parse(errorHandler,
                token(IDENTIFIER, "whee"),
                token(COLON),
                token(EOF)
        );

        LabelStatement expectedResult = new LabelStatement("whee");
        assertEquals(expectedResult, result);
    }

    @Test
    void testLocalLabelStatement() {
        Statement result = parse(errorHandler,
                token(IDENTIFIER, "@whee"),
                token(COLON),
                token(EOL)
        );

        LocalLabelStatement expectedResult = new LocalLabelStatement("@whee");
        assertEquals(expectedResult, result);
    }

    @Test
    void testLabelStatement_unnamed() {
        Statement result = parse(errorHandler,
                token(COLON),
                token(EOL)
        );

        assertEquals(new UnnamedLabelStatement(), result);
    }

    @Test
    void testLabelStatement_unnamed_noEndOfLine() {
        Statement result = parse(errorHandler,
                token(COLON),
                token(EOF)
        );

        assertEquals(new UnnamedLabelStatement(), result);
    }
}