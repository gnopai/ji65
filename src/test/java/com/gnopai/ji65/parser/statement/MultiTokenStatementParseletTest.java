package com.gnopai.ji65.parser.statement;

import com.gnopai.ji65.parser.Parser;
import com.gnopai.ji65.scanner.Token;
import com.gnopai.ji65.scanner.TokenType;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static com.gnopai.ji65.scanner.TokenType.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class MultiTokenStatementParseletTest {

    @Test
    void testValidToken() {
        Parser parser = mock(Parser.class);

        StatementParselet parselet1 = mock(StatementParselet.class);
        Statement statement1 = mock(Statement.class);
        when(parselet1.parse(token(IDENTIFIER), parser)).thenReturn(statement1);

        StatementParselet parselet2 = mock(StatementParselet.class);
        Statement statement2 = mock(Statement.class);
        when(parselet2.parse(token(IDENTIFIER), parser)).thenReturn(statement2);

        StatementParselet defaultParselet = mock(StatementParselet.class);


        MultiTokenStatementParselet testClass = new MultiTokenStatementParselet(Map.of(
                COLON, parselet1,
                EQUAL, parselet2
        ), defaultParselet);

        when(parser.peekNextTokenType()).thenReturn(EQUAL);

        Statement statement = testClass.parse(token(IDENTIFIER), parser);
        assertEquals(statement2, statement);
    }

    @Test
    void testUnmatchedTokenUsesDefault() {
        Parser parser = mock(Parser.class);

        StatementParselet defaultParselet = mock(StatementParselet.class);
        Statement expectedStatement = mock(Statement.class);
        when(defaultParselet.parse(token(IDENTIFIER), parser)).thenReturn(expectedStatement);

        MultiTokenStatementParselet testClass = new MultiTokenStatementParselet(Map.of(), defaultParselet);

        when(parser.peekNextTokenType()).thenReturn(EQUAL);

        Statement statement = testClass.parse(token(IDENTIFIER), parser);
        assertEquals(expectedStatement, statement);
    }

    private Token token(TokenType type) {
        return new Token(type, "", null, 0);
    }
}