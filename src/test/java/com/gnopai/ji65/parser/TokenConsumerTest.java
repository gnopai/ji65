package com.gnopai.ji65.parser;

import com.gnopai.ji65.scanner.Token;
import com.gnopai.ji65.scanner.TokenType;
import com.gnopai.ji65.util.ErrorHandler;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.gnopai.ji65.scanner.TokenType.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class TokenConsumerTest {
    private final ErrorHandler errorHandler = mock(ErrorHandler.class);

    @Test
    void testConsume_noTokens() {
        List<Token> tokens = List.of();
        TokenConsumer consumer = new TokenConsumer(errorHandler, tokens);
        assertFalse(consumer.hasMore());
    }

    @Test
    void testHasMore_eofOnly() {
        List<Token> tokens = List.of(token(EOF));
        TokenConsumer consumer = new TokenConsumer(errorHandler, tokens);
        assertFalse(consumer.hasMore());
    }

    @Test
    void testHasMore_tokensWithEof() {
        List<Token> tokens = List.of(token(NUMBER), token(EOF));
        TokenConsumer consumer = new TokenConsumer(errorHandler, tokens);

        assertTrue(consumer.hasMore());
        consumer.consume(NUMBER, "Expected number token");

        assertFalse(consumer.hasMore());
    }

    @Test
    void testHasMore_tokensWithoutEof() {
        List<Token> tokens = List.of(token(NUMBER), token(NUMBER));
        TokenConsumer consumer = new TokenConsumer(errorHandler, tokens);

        assertTrue(consumer.hasMore());
        consumer.consume(NUMBER, "Expected number token");

        assertTrue(consumer.hasMore());
        consumer.consume(NUMBER, "Expected number token");

        assertFalse(consumer.hasMore());
    }

    @Test
    void testConsume() {
        List<Token> tokens = List.of(
                token(LEFT_PAREN),
                token(NUMBER),
                token(RIGHT_PAREN),
                token(EOF)
        );
        TokenConsumer consumer = new TokenConsumer(errorHandler, tokens);

        assertEquals(token(LEFT_PAREN), consumer.consume());
        assertEquals(token(NUMBER), consumer.consume());
        assertEquals(token(RIGHT_PAREN), consumer.consume());
        assertEquals(token(EOF), consumer.consume());
        assertNull(consumer.consume());
        assertFalse(consumer.hasError());
    }

    @Test
    void testConsume_byType() {
        List<Token> tokens = List.of(
                token(LEFT_PAREN),
                token(NUMBER),
                token(RIGHT_PAREN),
                token(EOF)
        );
        TokenConsumer consumer = new TokenConsumer(errorHandler, tokens);

        assertEquals(token(LEFT_PAREN), consumer.consume(LEFT_PAREN, "expected left paren"));
        assertEquals(token(NUMBER), consumer.consume(NUMBER, "expected number"));
        assertEquals(token(RIGHT_PAREN), consumer.consume(RIGHT_PAREN, "expected right paren"));
        assertEquals(token(EOF), consumer.consume(EOF, "expected EOF"));
        assertNull(consumer.consume());
        assertFalse(consumer.hasError());
    }

    @Test
    void testConsume_byType_noMatch() {
        List<Token> tokens = List.of(
                token(PLUS),
                token(EOF)
        );
        TokenConsumer consumer = new TokenConsumer(errorHandler, tokens);

        String errorMessage = "expected a plus";
        ParseException parseException = assertThrows(ParseException.class, () -> consumer.consume(NUMBER, errorMessage));
        assertEquals(token(PLUS), parseException.getToken());
        assertTrue(consumer.hasError());
    }

    @Test
    void testMatch_true() {
        List<Token> tokens = List.of(
                token(NUMBER),
                token(PLUS),
                token(NUMBER),
                token(EOF)
        );
        TokenConsumer consumer = new TokenConsumer(errorHandler, tokens);

        assertTrue(consumer.match(NUMBER));
        assertTrue(consumer.match(PLUS));
        assertTrue(consumer.match(NUMBER));
    }

    @Test
    void testMatch_false() {
        List<Token> tokens = List.of(
                token(PLUS),
                token(EOF)
        );
        TokenConsumer consumer = new TokenConsumer(errorHandler, tokens);

        assertFalse(consumer.match(NUMBER));
    }

    @Test
    void testMultipleTokensAndOperations() {
        List<Token> tokens = List.of(
                token(LEFT_PAREN),
                token(NUMBER),
                token(PLUS),
                token(NUMBER),
                token(RIGHT_PAREN),
                token(EOF)
        );
        TokenConsumer consumer = new TokenConsumer(errorHandler, tokens);

        assertFalse(consumer.match(NUMBER));
        assertTrue(consumer.match(LEFT_PAREN));
        assertTrue(consumer.hasMore());

        assertEquals(token(NUMBER), consumer.consume());
        assertTrue(consumer.hasMore());
        assertFalse(consumer.match(MINUS));
        assertTrue(consumer.match(PLUS));

        assertEquals(token(NUMBER), consumer.consume());
        assertTrue(consumer.hasMore());

        assertEquals(token(RIGHT_PAREN), consumer.consume(RIGHT_PAREN, "expected right paren"));
        assertFalse(consumer.hasMore());
        assertFalse(consumer.hasError());
    }

    private Token token(TokenType type) {
        return new Token(type, null, null, 0);
    }
}