package com.gnopai.ji65.parser.statement;

import com.gnopai.ji65.parser.ParseException;
import com.gnopai.ji65.parser.Parser;
import com.gnopai.ji65.scanner.Token;
import com.gnopai.ji65.scanner.TokenType;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static com.gnopai.ji65.scanner.TokenType.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class MultiTokenStatementParseletTest {

    @Test
    void testValidToken() {
        Parser parser = mock(Parser.class);

        StatementParselet parselet1 = mock(StatementParselet.class);
        Statement statement1 = mock(Statement.class);
        when(parselet1.parse(token(COLON), parser)).thenReturn(statement1);

        StatementParselet parselet2 = mock(StatementParselet.class);
        Statement statement2 = mock(Statement.class);
        when(parselet2.parse(token(EQUAL), parser)).thenReturn(statement2);

        MultiTokenStatementParselet testClass = new MultiTokenStatementParselet(Map.of(
                COLON, parselet1,
                EQUAL, parselet2
        ));

        when(parser.consume()).thenReturn(token(EQUAL));

        Statement statement = testClass.parse(token(IDENTIFIER), parser);
        assertEquals(statement2, statement);
    }

    @Test
    void testInvalidToken() {
        Parser parser = mock(Parser.class);

        MultiTokenStatementParselet testClass = new MultiTokenStatementParselet(Map.of());

        when(parser.consume()).thenReturn(token(EQUAL));
        ParseException expectedException = new ParseException(token(EQUAL), "ohno");
        when(parser.error(anyString())).thenReturn(expectedException);

        ParseException exception = assertThrows(ParseException.class, () -> testClass.parse(token(IDENTIFIER), parser));
        assertEquals(expectedException, exception);
    }

    private Token token(TokenType type) {
        return new Token(type, "", null, 0);
    }
}