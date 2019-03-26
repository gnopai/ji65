package com.gnopai.ji65.config;

import com.gnopai.ji65.scanner.Token;
import com.gnopai.ji65.scanner.TokenReader;
import com.gnopai.ji65.scanner.TokenType;
import com.gnopai.ji65.util.ErrorHandler;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.gnopai.ji65.scanner.TokenType.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class ConfigScannerTest {
    private final ErrorHandler errorHandler = mock(ErrorHandler.class);

    @Test
    void testEmptySource() {
        List<Token> tokens = runScanner("");
        List<Token> expectedTokens = List.of(
                new Token(EOF, "", null, 1)
        );
        assertEquals(expectedTokens, tokens);
    }

    @Test
    void testSingleToken_singleCharTokens() {
        testSimpleSingleToken("{", LEFT_BRACE);
        testSimpleSingleToken("}", RIGHT_BRACE);
        testSimpleSingleToken(",", COMMA);
        testSimpleSingleToken(";", SEMICOLON);
        testSimpleSingleToken(":", COLON);
        testSimpleSingleToken("=", EQUAL);
    }

    @Test
    void testSingleToken_string() {
        String text = "\"Ima String\"";
        List<Token> tokens = runScanner(text);
        List<Token> expectedTokens = List.of(
                new Token(STRING, text, "Ima String", 1),
                new Token(EOF, "", null, 1)
        );
        assertEquals(expectedTokens, tokens);
        verifyNoErrors();
    }

    @Test
    void testUnterminatedString_endOfLine() {
        String text = "\"Ima String\n;\"";
        List<Token> tokens = runScanner(text);
        List<Token> expectedTokens = List.of(
                new Token(SEMICOLON, ";", null, 2),
                new Token(EOF, "", null, 2)
        );
        assertEquals(expectedTokens, tokens);
        verify(errorHandler).handleError("Unterminated string", 1);
    }

    @Test
    void testUnterminatedString_endOfSourceText() {
        String text = "\"Ima String";
        List<Token> tokens = runScanner(text);
        List<Token> expectedTokens = List.of(
                new Token(EOF, "", null, 1)
        );
        assertEquals(expectedTokens, tokens);
        verify(errorHandler).handleError("Unterminated string", 1);
    }

    @Test
    void testSingleToken_decimalNumber() {
        String text = "1234";
        List<Token> tokens = runScanner(text);
        List<Token> expectedTokens = List.of(
                new Token(NUMBER, text, 1234, 1),
                new Token(EOF, "", null, 1)
        );
        assertEquals(expectedTokens, tokens);
        verifyNoErrors();
    }

    @Test
    void testSingleToken_decimalNumber_trailingGarbage() {
        String text = "1234Y";
        List<Token> tokens = runScanner(text);
        List<Token> expectedTokens = List.of(
                new Token(EOF, "", null, 1)
        );
        assertEquals(expectedTokens, tokens);
        verify(errorHandler).handleError("Invalid decimal number", 1);
        verifyNoMoreInteractions(errorHandler);
    }

    @Test
    void testSingleToken_hexNumber_validFullLength() {
        String text = "$2a5F";
        List<Token> tokens = runScanner(text);
        List<Token> expectedTokens = List.of(
                new Token(NUMBER, text, 10847, 1),
                new Token(EOF, "", null, 1)
        );
        assertEquals(expectedTokens, tokens);
        verifyNoErrors();
    }

    @Test
    void testSingleToken_hexNumber_validMinLength() {
        String text = "$d";
        List<Token> tokens = runScanner(text);
        List<Token> expectedTokens = List.of(
                new Token(NUMBER, text, 13, 1),
                new Token(EOF, "", null, 1)
        );
        assertEquals(expectedTokens, tokens);
        verifyNoErrors();
    }

    @Test
    void testSingleToken_hexNumber_badChar() {
        String text = "$12G3";
        List<Token> tokens = runScanner(text);
        List<Token> expectedTokens = List.of(
                new Token(EOF, "", null, 1)
        );
        assertEquals(expectedTokens, tokens);
        verify(errorHandler).handleError("Invalid hex number", 1);
        verifyNoMoreInteractions(errorHandler);
    }

    @Test
    void testSingleToken_hexNumber_tooShort() {
        String text = "$";
        List<Token> tokens = runScanner(text);
        List<Token> expectedTokens = List.of(
                new Token(EOF, "", null, 1)
        );
        assertEquals(expectedTokens, tokens);
        verify(errorHandler).handleError("Invalid hex number", 1);
        verifyNoMoreInteractions(errorHandler);
    }

    @Test
    void testSingleToken_hexNumber_tooLong() {
        String text = "$12345";
        List<Token> tokens = runScanner(text);
        List<Token> expectedTokens = List.of(
                new Token(EOF, "", null, 1)
        );
        assertEquals(expectedTokens, tokens);
        verify(errorHandler).handleError("Invalid hex number", 1);
        verifyNoMoreInteractions(errorHandler);
    }

    @Test
    void testWhitespace() {
        assertEquals(
                List.of(new Token(EOF, "", null, 1)),
                runScanner(" ")
        );
        assertEquals(
                List.of(new Token(EOF, "", null, 1)),
                runScanner("\r")
        );
        assertEquals(
                List.of(new Token(EOF, "", null, 1)),
                runScanner("\t")
        );
        assertEquals(
                List.of(
                        new Token(EOF, "", null, 2)),
                runScanner("\n")
        );
        verifyNoErrors();
    }

    @Test
    void testSingleToken_invalid() {
        List<Token> tokens = runScanner("`");
        List<Token> expectedTokens = List.of(
                new Token(EOF, "", null, 1)
        );
        assertEquals(expectedTokens, tokens);
        verify(errorHandler).handleError("Unexpected character '`'", 1);
        verifyNoMoreInteractions(errorHandler);
    }

    @Test
    void testComment_wholeLine() {
        String text = "# this is some stuff that is commented out\n;";
        List<Token> tokens = runScanner(text);
        List<Token> expectedTokens = List.of(
                new Token(SEMICOLON, ";", null, 2),
                new Token(EOF, "", null, 2)
        );
        assertEquals(expectedTokens, tokens);
        verifyNoErrors();
    }

    @Test
    void testComment_partOfLine() {
        String text = "{} # this is some stuff that is commented out\n;";
        List<Token> tokens = runScanner(text);
        List<Token> expectedTokens = List.of(
                new Token(LEFT_BRACE, "{", null, 1),
                new Token(RIGHT_BRACE, "}", null, 1),
                new Token(SEMICOLON, ";", null, 2),
                new Token(EOF, "", null, 2)
        );
        assertEquals(expectedTokens, tokens);
        verifyNoErrors();
    }

    @Test
    void testSingleToken_identifier() {
        String text = "derp";
        List<Token> tokens = runScanner(text);
        List<Token> expectedTokens = List.of(
                new Token(IDENTIFIER, text, null, 1),
                new Token(EOF, "", null, 1)
        );
        assertEquals(expectedTokens, tokens);
        verifyNoErrors();
    }

    @Test
    void testSingleToken_identifierWithPercentSign() {
        String text = "%derp";
        List<Token> tokens = runScanner(text);
        List<Token> expectedTokens = List.of(
                new Token(IDENTIFIER, text, null, 1),
                new Token(EOF, "", null, 1)
        );
        assertEquals(expectedTokens, tokens);
        verifyNoErrors();
    }

    @Test
    void testMultipleLines() {
        String text = "{}\n;\n=";
        List<Token> tokens = runScanner(text);
        List<Token> expectedTokens = List.of(
                new Token(LEFT_BRACE, "{", null, 1),
                new Token(RIGHT_BRACE, "}", null, 1),
                new Token(SEMICOLON, ";", null, 2),
                new Token(EQUAL, "=", null, 3),
                new Token(EOF, "", null, 3)
        );
        assertEquals(expectedTokens, tokens);
        verifyNoErrors();
    }

    private void testSimpleSingleToken(String text, TokenType type) {
        testSingleToken(text, new Token(type, text, null, 1));
    }

    private void testSingleToken(String text, Token expectedToken) {
        List<Token> tokens = runScanner(text);
        List<Token> expectedTokens = List.of(
                expectedToken,
                new Token(EOF, "", null, 1)
        );
        assertEquals(expectedTokens, tokens);
        verifyNoErrors();
    }

    private List<Token> runScanner(String text) {
        return new ConfigScanner(new TokenReader(errorHandler)).scan(text);
    }

    private void verifyNoErrors() {
        verify(errorHandler, never()).handleError(anyString(), anyInt());
    }

}