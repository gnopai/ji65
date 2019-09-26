package com.gnopai.ji65.config;

import com.gnopai.ji65.parser.ParseException;
import com.gnopai.ji65.parser.TokenStream;
import com.gnopai.ji65.scanner.Token;
import com.gnopai.ji65.scanner.TokenType;
import com.gnopai.ji65.util.ErrorHandler;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static com.gnopai.ji65.scanner.TokenType.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

class ConfigParserTest {
    private final ErrorHandler errorHandler = mock(ErrorHandler.class);

    @Test
    void testSingleBlock_singleSegment_singleAttribute() {
        List<Token> tokens = List.of(
                token(IDENTIFIER, "BLOCK1"),
                token(LEFT_BRACE),
                token(IDENTIFIER, "SEGMENT1"),
                token(COLON),
                token(IDENTIFIER, "ATTR1"),
                token(EQUAL),
                token(NUMBER, 123),
                token(SEMICOLON),
                token(RIGHT_BRACE),
                token(EOF)
        );
        TokenStream tokenStream = new TokenStream(errorHandler, tokens);

        List<ConfigBlock> blocks = new ConfigParser(tokenStream).parse();

        ConfigBlock expectedBlock = new ConfigBlock("BLOCK1", List.of(
                new ConfigSegment("SEGMENT1", Map.of("ATTR1", token(NUMBER, 123)))
        ));
        assertEquals(List.of(expectedBlock), blocks);
    }

    @Test
    void testSingleBlock_singleSegment_multipleAttributes() {
        List<Token> tokens = List.of(
                token(IDENTIFIER, "BLOCK1"),
                token(LEFT_BRACE),
                token(IDENTIFIER, "SEGMENT1"),
                token(COLON),
                token(IDENTIFIER, "ATTR1"),
                token(EQUAL),
                token(NUMBER, 123),
                token(IDENTIFIER, "ATTR2"),
                token(EQUAL),
                token(NUMBER, 456),
                token(IDENTIFIER, "ATTR3"),
                token(EQUAL),
                token(NUMBER, 789),
                token(SEMICOLON),
                token(RIGHT_BRACE),
                token(EOF)
        );
        TokenStream tokenStream = new TokenStream(errorHandler, tokens);

        List<ConfigBlock> blocks = new ConfigParser(tokenStream).parse();

        ConfigBlock expectedBlock = new ConfigBlock("BLOCK1", List.of(
                new ConfigSegment("SEGMENT1", Map.of(
                        "ATTR1", token(NUMBER, 123),
                        "ATTR2", token(NUMBER, 456),
                        "ATTR3", token(NUMBER, 789)
                ))
        ));
        assertEquals(List.of(expectedBlock), blocks);
    }

    @Test
    void testSingleBlock_singleSegment_multipleAttributes_commaDelimited() {
        List<Token> tokens = List.of(
                token(IDENTIFIER, "BLOCK1"),
                token(LEFT_BRACE),
                token(IDENTIFIER, "SEGMENT1"),
                token(COLON),
                token(IDENTIFIER, "ATTR1"),
                token(EQUAL),
                token(NUMBER, 123),
                token(COMMA),
                token(IDENTIFIER, "ATTR2"),
                token(EQUAL),
                token(NUMBER, 456),
                token(COMMA),
                token(IDENTIFIER, "ATTR3"),
                token(EQUAL),
                token(NUMBER, 789),
                token(SEMICOLON),
                token(RIGHT_BRACE),
                token(EOF)
        );
        TokenStream tokenStream = new TokenStream(errorHandler, tokens);

        List<ConfigBlock> blocks = new ConfigParser(tokenStream).parse();

        ConfigBlock expectedBlock = new ConfigBlock("BLOCK1", List.of(
                new ConfigSegment("SEGMENT1", Map.of(
                        "ATTR1", token(NUMBER, 123),
                        "ATTR2", token(NUMBER, 456),
                        "ATTR3", token(NUMBER, 789)
                ))
        ));
        assertEquals(List.of(expectedBlock), blocks);
    }

    @Test
    void testSingleBlock_multipleSegments() {
        List<Token> tokens = List.of(
                token(IDENTIFIER, "BLOCK1"),
                token(LEFT_BRACE),
                token(IDENTIFIER, "SEGMENT1"),
                token(COLON),
                token(IDENTIFIER, "ATTR1"),
                token(EQUAL),
                token(NUMBER, 123),
                token(SEMICOLON),
                token(IDENTIFIER, "SEGMENT2"),
                token(COLON),
                token(IDENTIFIER, "ATTR1"),
                token(EQUAL),
                token(NUMBER, 124),
                token(SEMICOLON),
                token(RIGHT_BRACE),
                token(EOF)
        );
        TokenStream tokenStream = new TokenStream(errorHandler, tokens);

        List<ConfigBlock> blocks = new ConfigParser(tokenStream).parse();

        ConfigBlock expectedBlock = new ConfigBlock("BLOCK1", List.of(
                new ConfigSegment("SEGMENT1", Map.of("ATTR1", token(NUMBER, 123))),
                new ConfigSegment("SEGMENT2", Map.of("ATTR1", token(NUMBER, 124)))
        ));
        assertEquals(List.of(expectedBlock), blocks);
    }

    @Test
    void testMultipleBlocks() {
        List<Token> tokens = List.of(
                token(IDENTIFIER, "BLOCK1"),
                token(LEFT_BRACE),
                token(IDENTIFIER, "SEGMENT1"),
                token(COLON),
                token(IDENTIFIER, "ATTR1"),
                token(EQUAL),
                token(NUMBER, 123),
                token(SEMICOLON),
                token(RIGHT_BRACE),
                token(IDENTIFIER, "BLOCK2"),
                token(LEFT_BRACE),
                token(IDENTIFIER, "SEGMENT1"),
                token(COLON),
                token(IDENTIFIER, "ATTR1"),
                token(EQUAL),
                token(NUMBER, 125),
                token(SEMICOLON),
                token(RIGHT_BRACE),
                token(EOF)
        );
        TokenStream tokenStream = new TokenStream(errorHandler, tokens);

        List<ConfigBlock> blocks = new ConfigParser(tokenStream).parse();

        ConfigBlock expectedBlock1 = new ConfigBlock("BLOCK1", List.of(
                new ConfigSegment("SEGMENT1", Map.of("ATTR1", token(NUMBER, 123)))
        ));
        ConfigBlock expectedBlock2 = new ConfigBlock("BLOCK2", List.of(
                new ConfigSegment("SEGMENT1", Map.of("ATTR1", token(NUMBER, 125)))
        ));
        assertEquals(List.of(expectedBlock1, expectedBlock2), blocks);
    }

    @Test
    void testBadBlock_missingIdentifier() {
        List<Token> tokens = List.of(
                token(LEFT_BRACE),
                token(IDENTIFIER, "SEGMENT1"),
                token(COLON),
                token(IDENTIFIER, "ATTR1"),
                token(EQUAL),
                token(NUMBER, 125),
                token(SEMICOLON),
                token(RIGHT_BRACE),
                token(EOF)
        );
        TokenStream tokenStream = new TokenStream(errorHandler, tokens);

        ParseException parseException = assertThrows(ParseException.class, () -> new ConfigParser(tokenStream).parse());
        assertEquals(LEFT_BRACE, parseException.getTokenType());
        assertEquals("Expected block identifier", parseException.getMessage());
    }

    @Test
    void testBadBlock_missingStartBrace() {
        List<Token> tokens = List.of(
                token(IDENTIFIER, "BLOCK1"),
                token(IDENTIFIER, "SEGMENT1"),
                token(COLON),
                token(IDENTIFIER, "ATTR1"),
                token(EQUAL),
                token(NUMBER, 125),
                token(SEMICOLON),
                token(RIGHT_BRACE),
                token(EOF)
        );
        TokenStream tokenStream = new TokenStream(errorHandler, tokens);

        ParseException parseException = assertThrows(ParseException.class, () -> new ConfigParser(tokenStream).parse());
        assertEquals(IDENTIFIER, parseException.getTokenType());
        assertEquals("Expected block starting brace", parseException.getMessage());
    }

    @Test
    void testBadBlock_missingEndBrace() {
        List<Token> tokens = List.of(
                token(IDENTIFIER, "BLOCK1"),
                token(LEFT_BRACE),
                token(IDENTIFIER, "SEGMENT1"),
                token(COLON),
                token(IDENTIFIER, "ATTR1"),
                token(EQUAL),
                token(NUMBER, 125),
                token(SEMICOLON),
                token(EOF)
        );
        TokenStream tokenStream = new TokenStream(errorHandler, tokens);

        ParseException parseException = assertThrows(ParseException.class, () -> new ConfigParser(tokenStream).parse());
        assertEquals(EOF, parseException.getTokenType());
        assertEquals("Expected segment identifier", parseException.getMessage());
    }

    @Test
    void testBadSegment_missingIdentifier() {
        List<Token> tokens = List.of(
                token(IDENTIFIER, "BLOCK1"),
                token(LEFT_BRACE),
                token(COLON),
                token(IDENTIFIER, "ATTR1"),
                token(EQUAL),
                token(NUMBER, 125),
                token(SEMICOLON),
                token(RIGHT_BRACE),
                token(EOF)
        );
        TokenStream tokenStream = new TokenStream(errorHandler, tokens);

        ParseException parseException = assertThrows(ParseException.class, () -> new ConfigParser(tokenStream).parse());
        assertEquals(COLON, parseException.getTokenType());
        assertEquals("Expected segment identifier", parseException.getMessage());
    }

    @Test
    void testBadSegment_missingColon() {
        List<Token> tokens = List.of(
                token(IDENTIFIER, "BLOCK1"),
                token(LEFT_BRACE),
                token(IDENTIFIER, "SEGMENT1"),
                token(IDENTIFIER, "ATTR1"),
                token(EQUAL),
                token(NUMBER, 125),
                token(SEMICOLON),
                token(RIGHT_BRACE),
                token(EOF)
        );
        TokenStream tokenStream = new TokenStream(errorHandler, tokens);

        ParseException parseException = assertThrows(ParseException.class, () -> new ConfigParser(tokenStream).parse());
        assertEquals(IDENTIFIER, parseException.getTokenType());
        assertEquals("Expected colon", parseException.getMessage());
    }

    @Test
    void testBadSegment_noAttributes() {
        List<Token> tokens = List.of(
                token(IDENTIFIER, "BLOCK1"),
                token(LEFT_BRACE),
                token(IDENTIFIER, "SEGMENT1"),
                token(COLON),
                token(SEMICOLON),
                token(RIGHT_BRACE),
                token(EOF)
        );
        TokenStream tokenStream = new TokenStream(errorHandler, tokens);

        ParseException parseException = assertThrows(ParseException.class, () -> new ConfigParser(tokenStream).parse());
        assertEquals(SEMICOLON, parseException.getTokenType());
        assertEquals("Expected attribute identifier", parseException.getMessage());
    }

    @Test
    void testBadAttribute_missingIdentifier() {
        List<Token> tokens = List.of(
                token(IDENTIFIER, "BLOCK1"),
                token(LEFT_BRACE),
                token(IDENTIFIER, "SEGMENT1"),
                token(COLON),
                token(EQUAL),
                token(NUMBER, 125),
                token(SEMICOLON),
                token(RIGHT_BRACE),
                token(EOF)
        );
        TokenStream tokenStream = new TokenStream(errorHandler, tokens);

        ParseException parseException = assertThrows(ParseException.class, () -> new ConfigParser(tokenStream).parse());
        assertEquals(EQUAL, parseException.getTokenType());
        assertEquals("Expected attribute identifier", parseException.getMessage());
    }

    @Test
    void testBadAttribute_missingEqual() {
        List<Token> tokens = List.of(
                token(IDENTIFIER, "BLOCK1"),
                token(LEFT_BRACE),
                token(IDENTIFIER, "SEGMENT1"),
                token(COLON),
                token(IDENTIFIER, "ATTR1"),
                token(NUMBER, 125),
                token(SEMICOLON),
                token(RIGHT_BRACE),
                token(EOF)
        );
        TokenStream tokenStream = new TokenStream(errorHandler, tokens);

        ParseException parseException = assertThrows(ParseException.class, () -> new ConfigParser(tokenStream).parse());
        assertEquals(NUMBER, parseException.getTokenType());
        assertEquals("Expected equal sign", parseException.getMessage());
    }

    @Test
    void testBadAttribute_badValue() {
        List<Token> tokens = List.of(
                token(IDENTIFIER, "BLOCK1"),
                token(LEFT_BRACE),
                token(IDENTIFIER, "SEGMENT1"),
                token(COLON),
                token(IDENTIFIER, "ATTR1"),
                token(EQUAL),
                token(SEMICOLON),
                token(RIGHT_BRACE),
                token(EOF)
        );
        TokenStream tokenStream = new TokenStream(errorHandler, tokens);

        ParseException parseException = assertThrows(ParseException.class, () -> new ConfigParser(tokenStream).parse());
        assertEquals(SEMICOLON, parseException.getTokenType());
        assertEquals("Invalid attribute value", parseException.getMessage());
    }

    private Token token(TokenType type, String value) {
        return new Token(type, value, value, 0);
    }

    private Token token(TokenType type, int value) {
        return new Token(type, String.valueOf(value), value, 0);
    }

    private Token token(TokenType type) {
        return new Token(type, null, null, 0);
    }

}