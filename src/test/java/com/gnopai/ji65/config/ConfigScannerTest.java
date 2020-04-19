package com.gnopai.ji65.config;

import com.gnopai.ji65.scanner.SourceFile;
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
        SourceFile sourceFile = new SourceFile(null, "");
        List<Token> tokens = runScanner(sourceFile);
        List<Token> expectedTokens = List.of(
                new Token(EOF, "", null, 1, sourceFile)
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
        SourceFile sourceFile = new SourceFile(null, text);
        List<Token> tokens = runScanner(sourceFile);
        List<Token> expectedTokens = List.of(
                new Token(STRING, text, "Ima String", 1, sourceFile),
                new Token(EOF, "", null, 1, sourceFile)
        );
        assertEquals(expectedTokens, tokens);
        verifyNoErrors();
    }

    @Test
    void testUnterminatedString_endOfLine() {
        String text = "\"Ima String\n;\"";
        SourceFile sourceFile = new SourceFile(null, text);
        List<Token> tokens = runScanner(sourceFile);
        List<Token> expectedTokens = List.of(
                new Token(SEMICOLON, ";", null, 2, sourceFile),
                new Token(EOF, "", null, 2, sourceFile)
        );
        assertEquals(expectedTokens, tokens);
        verify(errorHandler).handleError("Unterminated string", sourceFile, 1);
    }

    @Test
    void testUnterminatedString_endOfSourceText() {
        String text = "\"Ima String";
        SourceFile sourceFile = new SourceFile(null, text);
        List<Token> tokens = runScanner(sourceFile);
        List<Token> expectedTokens = List.of(
                new Token(EOF, "", null, 1, sourceFile)
        );
        assertEquals(expectedTokens, tokens);
        verify(errorHandler).handleError("Unterminated string", sourceFile, 1);
    }

    @Test
    void testSingleToken_decimalNumber() {
        String text = "1234";
        SourceFile sourceFile = new SourceFile(null, text);
        List<Token> tokens = runScanner(sourceFile);
        List<Token> expectedTokens = List.of(
                new Token(NUMBER, text, 1234, 1, sourceFile),
                new Token(EOF, "", null, 1, sourceFile)
        );
        assertEquals(expectedTokens, tokens);
        verifyNoErrors();
    }

    @Test
    void testSingleToken_decimalNumber_trailingGarbage() {
        String text = "1234Y";
        SourceFile sourceFile = new SourceFile(null, text);
        List<Token> tokens = runScanner(sourceFile);
        List<Token> expectedTokens = List.of(
                new Token(EOF, "", null, 1, sourceFile)
        );
        assertEquals(expectedTokens, tokens);
        verify(errorHandler).handleError("Invalid decimal number", sourceFile, 1);
        verifyNoMoreInteractions(errorHandler);
    }

    @Test
    void testSingleToken_hexNumber_validFullLength() {
        String text = "$2a5F";
        SourceFile sourceFile = new SourceFile(null, text);
        List<Token> tokens = runScanner(sourceFile);
        List<Token> expectedTokens = List.of(
                new Token(NUMBER, text, 10847, 1, sourceFile),
                new Token(EOF, "", null, 1, sourceFile)
        );
        assertEquals(expectedTokens, tokens);
        verifyNoErrors();
    }

    @Test
    void testSingleToken_hexNumber_validMinLength() {
        String text = "$d";
        SourceFile sourceFile = new SourceFile(null, text);
        List<Token> tokens = runScanner(sourceFile);
        List<Token> expectedTokens = List.of(
                new Token(NUMBER, text, 13, 1, sourceFile),
                new Token(EOF, "", null, 1, sourceFile)
        );
        assertEquals(expectedTokens, tokens);
        verifyNoErrors();
    }

    @Test
    void testSingleToken_hexNumber_badChar() {
        String text = "$12G3";
        SourceFile sourceFile = new SourceFile(null, text);
        List<Token> tokens = runScanner(sourceFile);
        List<Token> expectedTokens = List.of(
                new Token(EOF, "", null, 1, sourceFile)
        );
        assertEquals(expectedTokens, tokens);
        verify(errorHandler).handleError("Invalid hex number", sourceFile, 1);
        verifyNoMoreInteractions(errorHandler);
    }

    @Test
    void testSingleToken_hexNumber_tooShort() {
        String text = "$";
        SourceFile sourceFile = new SourceFile(null, text);
        List<Token> tokens = runScanner(sourceFile);
        List<Token> expectedTokens = List.of(
                new Token(EOF, "", null, 1, sourceFile)
        );
        assertEquals(expectedTokens, tokens);
        verify(errorHandler).handleError("Invalid hex number", sourceFile, 1);
        verifyNoMoreInteractions(errorHandler);
    }

    @Test
    void testSingleToken_hexNumber_tooLong() {
        String text = "$12345";
        SourceFile sourceFile = new SourceFile(null, text);
        List<Token> tokens = runScanner(sourceFile);
        List<Token> expectedTokens = List.of(
                new Token(EOF, "", null, 1, sourceFile)
        );
        assertEquals(expectedTokens, tokens);
        verify(errorHandler).handleError("Invalid hex number", sourceFile, 1);
        verifyNoMoreInteractions(errorHandler);
    }

    @Test
    void testWhitespace() {
        testWhitespaceIgnored(" ", 1);
        testWhitespaceIgnored("\r", 1);
        testWhitespaceIgnored("\t", 1);
        testWhitespaceIgnored("\n", 2);
        verifyNoErrors();
    }

    private void testWhitespaceIgnored(String text, int lineCount) {
        SourceFile sourceFile = new SourceFile(null, text);
        List<Token> tokens = runScanner(sourceFile);
        Token expectedToken = new Token(EOF, "", null, lineCount, sourceFile);
        assertEquals(List.of(expectedToken), tokens);
    }

    @Test
    void testSingleToken_invalid() {
        SourceFile sourceFile = new SourceFile(null, "`");
        List<Token> tokens = runScanner(sourceFile);
        List<Token> expectedTokens = List.of(
                new Token(EOF, "", null, 1, sourceFile)
        );
        assertEquals(expectedTokens, tokens);
        verify(errorHandler).handleError("Unexpected character '`'", sourceFile, 1);
        verifyNoMoreInteractions(errorHandler);
    }

    @Test
    void testComment_wholeLine() {
        String text = "# this is some stuff that is commented out\n;";
        SourceFile sourceFile = new SourceFile(null, text);
        List<Token> tokens = runScanner(sourceFile);
        List<Token> expectedTokens = List.of(
                new Token(SEMICOLON, ";", null, 2, sourceFile),
                new Token(EOF, "", null, 2, sourceFile)
        );
        assertEquals(expectedTokens, tokens);
        verifyNoErrors();
    }

    @Test
    void testComment_partOfLine() {
        String text = "{} # this is some stuff that is commented out\n;";
        SourceFile sourceFile = new SourceFile(null, text);
        List<Token> tokens = runScanner(sourceFile);
        List<Token> expectedTokens = List.of(
                new Token(LEFT_BRACE, "{", null, 1, sourceFile),
                new Token(RIGHT_BRACE, "}", null, 1, sourceFile),
                new Token(SEMICOLON, ";", null, 2, sourceFile),
                new Token(EOF, "", null, 2, sourceFile)
        );
        assertEquals(expectedTokens, tokens);
        verifyNoErrors();
    }

    @Test
    void testSingleToken_identifier() {
        String text = "derp";
        SourceFile sourceFile = new SourceFile(null, text);
        List<Token> tokens = runScanner(sourceFile);
        List<Token> expectedTokens = List.of(
                new Token(IDENTIFIER, text, null, 1, sourceFile),
                new Token(EOF, "", null, 1, sourceFile)
        );
        assertEquals(expectedTokens, tokens);
        verifyNoErrors();
    }

    @Test
    void testSingleToken_identifierWithPercentSign() {
        String text = "%derp";
        SourceFile sourceFile = new SourceFile(null, text);
        List<Token> tokens = runScanner(sourceFile);
        List<Token> expectedTokens = List.of(
                new Token(IDENTIFIER, text, null, 1, sourceFile),
                new Token(EOF, "", null, 1, sourceFile)
        );
        assertEquals(expectedTokens, tokens);
        verifyNoErrors();
    }

    @Test
    void testMultipleLines() {
        String text = "{}\n;\n=";
        SourceFile sourceFile = new SourceFile(null, text);
        List<Token> tokens = runScanner(sourceFile);
        List<Token> expectedTokens = List.of(
                new Token(LEFT_BRACE, "{", null, 1, sourceFile),
                new Token(RIGHT_BRACE, "}", null, 1, sourceFile),
                new Token(SEMICOLON, ";", null, 2, sourceFile),
                new Token(EQUAL, "=", null, 3, sourceFile),
                new Token(EOF, "", null, 3, sourceFile)
        );
        assertEquals(expectedTokens, tokens);
        verifyNoErrors();
    }

    private void testSimpleSingleToken(String text, TokenType expectedTokenType) {
        SourceFile sourceFile = new SourceFile(null, text);
        List<Token> tokens = runScanner(sourceFile);
        List<Token> expectedTokens = List.of(
                new Token(expectedTokenType, text, null, 1, sourceFile),
                new Token(EOF, "", null, 1, sourceFile)
        );
        assertEquals(expectedTokens, tokens);
        verifyNoErrors();
    }

    private List<Token> runScanner(SourceFile sourceFile) {
        return new ConfigScanner(new TokenReader(errorHandler)).scan(sourceFile);
    }

    private void verifyNoErrors() {
        verify(errorHandler, never()).handleError(anyString(), any(SourceFile.class), anyInt());
    }

}