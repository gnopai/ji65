package com.gnopai.ji65.parser.statement;

import com.gnopai.ji65.parser.ParseException;
import com.gnopai.ji65.parser.expression.BinaryOperatorExpression;
import com.gnopai.ji65.parser.expression.IdentifierExpression;
import com.gnopai.ji65.parser.expression.PrimaryExpression;
import com.gnopai.ji65.util.ErrorHandler;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.gnopai.ji65.parser.ParserTestUtil.parse;
import static com.gnopai.ji65.parser.ParserTestUtil.token;
import static com.gnopai.ji65.scanner.TokenType.*;
import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

class MacroStatementParseletTest {
    private final ErrorHandler errorHandler = mock(ErrorHandler.class);

    @Test
    void testNoArguments() {
        Statement result = parse(errorHandler,
                token(IDENTIFIER, "Whee"),
                token(EOL)
        );

        MacroStatement expectedStatement = new MacroStatement("Whee", emptyList());
        assertEquals(expectedStatement, result);
    }

    @Test
    void testSingleArgument() {
        Statement result = parse(errorHandler,
                token(IDENTIFIER, "Whee"),
                token(IDENTIFIER, "abc"),
                token(EOL)
        );

        MacroStatement expectedStatement = new MacroStatement("Whee", List.of(
                new IdentifierExpression("abc")
        ));
        assertEquals(expectedStatement, result);
    }

    @Test
    void testMultipleArguments() {
        Statement result = parse(errorHandler,
                token(IDENTIFIER, "Whee"),
                token(IDENTIFIER, "abc"),
                token(COMMA),
                token(NUMBER, 5),
                token(PLUS),
                token(NUMBER, 3),
                token(EOL)
        );

        MacroStatement expectedStatement = new MacroStatement("Whee", List.of(
                new IdentifierExpression("abc"),
                new BinaryOperatorExpression(new PrimaryExpression(NUMBER, 5), PLUS, new PrimaryExpression(NUMBER, 3))
        ));
        assertEquals(expectedStatement, result);
    }

    @Test
    void testMultipleArguments_danglingComma() {
        ParseException parseException = assertThrows(ParseException.class, () -> parse(errorHandler,
                token(IDENTIFIER, "Whee"),
                token(IDENTIFIER, "abc"),
                token(COMMA),
                token(EOL)
        ));

        assertEquals(token(EOL), parseException.getToken());
        assertEquals("Failed to parse token", parseException.getMessage());
    }

    @Test
    void testSingleArgument_missingEndOfLine() {
        ParseException parseException = assertThrows(ParseException.class, () -> parse(errorHandler,
                token(IDENTIFIER, "Whee"),
                token(IDENTIFIER, "abc"),
                token(EOF)
        ));

        assertEquals(token(EOF), parseException.getToken());
        assertEquals("Expected end of line", parseException.getMessage());
    }
}