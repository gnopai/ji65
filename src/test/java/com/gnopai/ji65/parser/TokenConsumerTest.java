package com.gnopai.ji65.parser;

import com.gnopai.ji65.DirectiveType;
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
    void testConsumeString() {
        List<Token> tokens = List.of(
                token(CHAR, (int) 'W'),
                token(CHAR, (int) 'h'),
                token(CHAR, (int) 'e'),
                token(CHAR, (int) 'e'),
                token(EOF)
        );
        TokenConsumer consumer = new TokenConsumer(errorHandler, tokens);

        String string = consumer.consumeString("expected a string");
        assertEquals("Whee", string);
        assertFalse(consumer.hasError());
    }

    @Test
    void testConsumeString_noMatch() {
        List<Token> tokens = List.of(
                token(NUMBER, 5),
                token(EOF)
        );
        TokenConsumer consumer = new TokenConsumer(errorHandler, tokens);

        String errorMessage = "expected a string";
        ParseException parseException = assertThrows(ParseException.class, () -> consumer.consumeString(errorMessage));
        assertEquals(NUMBER, parseException.getTokenType());
        assertEquals(errorMessage, parseException.getMessage());
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
    public void testMatchWithValue_true() {
        List<Token> tokens = List.of(
                token(NUMBER, 5),
                token(STRING, "whee"),
                token(DIRECTIVE, DirectiveType.WORD),
                token(EOF)
        );
        TokenConsumer consumer = new TokenConsumer(errorHandler, tokens);

        assertTrue(consumer.match(NUMBER, 5));
        assertTrue(consumer.match(STRING, "whee"));
        assertTrue(consumer.match(DIRECTIVE, DirectiveType.WORD));
    }

    @Test
    public void testMatchWithValue_false() {
        List<Token> tokens = List.of(
                token(NUMBER, 5),
                token(EOF)
        );
        TokenConsumer consumer = new TokenConsumer(errorHandler, tokens);

        assertFalse(consumer.match(NUMBER, 77));
        assertFalse(consumer.match(STRING, 5));
        assertFalse(consumer.match(STRING, "nope"));
    }

    @Test
    void testPrevious() {
        List<Token> tokens = List.of(
                token(LEFT_PAREN),
                token(NUMBER),
                token(RIGHT_PAREN),
                token(EOF)
        );
        TokenConsumer consumer = new TokenConsumer(errorHandler, tokens);

        assertEquals(token(LEFT_PAREN), consumer.consume());
        assertNull(consumer.getPrevious());

        assertEquals(token(NUMBER), consumer.consume());
        assertEquals(token(LEFT_PAREN), consumer.getPrevious());

        assertEquals(token(RIGHT_PAREN), consumer.consume());
        assertEquals(token(NUMBER), consumer.getPrevious());

        assertEquals(token(EOF), consumer.consume());
        assertEquals(token(RIGHT_PAREN), consumer.getPrevious());

        assertNull(consumer.consume());
        assertEquals(token(EOF), consumer.getPrevious());

        assertFalse(consumer.hasError());
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

    private Token token(TokenType type, Object value) {
        return new Token(type, value.toString(), value, 0);
    }
}