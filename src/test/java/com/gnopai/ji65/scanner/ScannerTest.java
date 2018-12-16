package com.gnopai.ji65.scanner;

import com.gnopai.ji65.directive.DirectiveType;
import com.gnopai.ji65.instruction.InstructionType;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.gnopai.ji65.scanner.TokenType.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class ScannerTest {
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
        testSimpleSingleToken("(", LEFT_PAREN);
        testSimpleSingleToken(")", RIGHT_PAREN);
        testSimpleSingleToken(",", COMMA);
        testSimpleSingleToken("-", MINUS);
        testSimpleSingleToken("+", PLUS);
        testSimpleSingleToken("/", SLASH);
        testSimpleSingleToken("*", STAR);
        testSimpleSingleToken("|", PIPE);
        testSimpleSingleToken("&", AMPERSAND);
        testSimpleSingleToken("@", AT_SIGN);
        testSimpleSingleToken(":", COLON);
        testSimpleSingleToken("#", POUND);
        testSimpleSingleToken("=", EQUAL);
        testSimpleSingleToken("^", CARET);
        testSimpleSingleToken("~", TILDE);
        testSimpleSingleToken(">", GREATER_THAN);
        testSimpleSingleToken("<", LESS_THAN);
    }

    @Test
    void testSingleToken_multiCharTokens() {
        testSimpleSingleToken("<<", SHIFT_LEFT);
        testSimpleSingleToken(">>", SHIFT_RIGHT);
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
        String text = "\"Ima String\n+\"";
        List<Token> tokens = runScanner(text);
        List<Token> expectedTokens = List.of(
                new Token(PLUS, "+", null, 2),
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
    void testSingleToken_binaryNumber_valid() {
        String text = "%00011010";
        List<Token> tokens = runScanner(text);
        List<Token> expectedTokens = List.of(
                new Token(NUMBER, text, 26, 1),
                new Token(EOF, "", null, 1)
        );
        assertEquals(expectedTokens, tokens);
        verifyNoErrors();
    }

    @Test
    void testSingleToken_binaryNumber_badChar() {
        String text = "%00011061";
        List<Token> tokens = runScanner(text);
        List<Token> expectedTokens = List.of(
                new Token(EOF, "", null, 1)
        );
        assertEquals(expectedTokens, tokens);
        verify(errorHandler).handleError("Invalid binary number", 1);
        verifyNoMoreInteractions(errorHandler);
    }

    @Test
    void testSingleToken_binaryNumber_tooShort() {
        String text = "%0001110";
        List<Token> tokens = runScanner(text);
        List<Token> expectedTokens = List.of(
                new Token(EOF, "", null, 1)
        );
        assertEquals(expectedTokens, tokens);
        verify(errorHandler).handleError("Invalid binary number", 1);
        verifyNoMoreInteractions(errorHandler);
    }

    @Test
    void testSingleToken_binaryNumber_noValue() {
        String text = "%";
        List<Token> tokens = runScanner(text);
        List<Token> expectedTokens = List.of(
                new Token(EOF, "", null, 1)
        );
        assertEquals(expectedTokens, tokens);
        verify(errorHandler).handleError("Invalid binary number", 1);
        verifyNoMoreInteractions(errorHandler);
    }

    @Test
    void testSingleToken_binaryNumber_tooLong() {
        String text = "%000111001";
        List<Token> tokens = runScanner(text);
        List<Token> expectedTokens = List.of(
                new Token(EOF, "", null, 1)
        );
        assertEquals(expectedTokens, tokens);
        verify(errorHandler).handleError("Invalid binary number", 1);
        verifyNoMoreInteractions(errorHandler);
    }

    @Test
    void testSingleToken_binaryNumber_trailingGarbage() {
        String text = "%00011100222";
        List<Token> tokens = runScanner(text);
        List<Token> expectedTokens = List.of(
                new Token(EOF, "", null, 1)
        );
        assertEquals(expectedTokens, tokens);
        verify(errorHandler).handleError("Invalid binary number", 1);
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
                List.of(new Token(EOF, "", null, 2)),
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
        String text = "; this is some stuff that is commented out\n+";
        List<Token> tokens = runScanner(text);
        List<Token> expectedTokens = List.of(
                new Token(PLUS, "+", null, 2),
                new Token(EOF, "", null, 2)
        );
        assertEquals(expectedTokens, tokens);
        verifyNoErrors();
    }

    @Test
    void testComment_partOfLine() {
        String text = "() ; this is some stuff that is commented out\n+";
        List<Token> tokens = runScanner(text);
        List<Token> expectedTokens = List.of(
                new Token(LEFT_PAREN, "(", null, 1),
                new Token(RIGHT_PAREN, ")", null, 1),
                new Token(PLUS, "+", null, 2),
                new Token(EOF, "", null, 2)
        );
        assertEquals(expectedTokens, tokens);
        verifyNoErrors();
    }

    @Test
    void testSingleToken_directive_valid() {
        String text = ".byte";
        List<Token> tokens = runScanner(text);
        List<Token> expectedTokens = List.of(
                new Token(DIRECTIVE, text, DirectiveType.BYTE, 1),
                new Token(EOF, "", null, 1)
        );
        assertEquals(expectedTokens, tokens);
        verifyNoErrors();
    }

    @Test
    void testSingleToken_directive_invalid() {
        String text = ".derp";
        List<Token> tokens = runScanner(text);
        List<Token> expectedTokens = List.of(
                new Token(EOF, "", null, 1)
        );
        assertEquals(expectedTokens, tokens);
        verify(errorHandler).handleError("Invalid directive", 1);
        verifyNoMoreInteractions(errorHandler);
    }

    @Test
    void testSingleToken_instruction() {
        String text = "LDA";
        List<Token> tokens = runScanner(text);
        List<Token> expectedTokens = List.of(
                new Token(INSTRUCTION, text, InstructionType.LDA, 1),
                new Token(EOF, "", null, 1)
        );
        assertEquals(expectedTokens, tokens);
        verifyNoErrors();
    }

    @Test
    void testSingleToken_registerX() {
        String text = "X";
        List<Token> tokens = runScanner(text);
        List<Token> expectedTokens = List.of(
                new Token(X, text, null, 1),
                new Token(EOF, "", null, 1)
        );
        assertEquals(expectedTokens, tokens);
        verifyNoErrors();
    }

    @Test
    void testSingleToken_registerY() {
        String text = "Y";
        List<Token> tokens = runScanner(text);
        List<Token> expectedTokens = List.of(
                new Token(Y, text, null, 1),
                new Token(EOF, "", null, 1)
        );
        assertEquals(expectedTokens, tokens);
        verifyNoErrors();
    }

    @Test
    void testSingleToken_registerA() {
        String text = "A";
        List<Token> tokens = runScanner(text);
        List<Token> expectedTokens = List.of(
                new Token(A, text, null, 1),
                new Token(EOF, "", null, 1)
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
    void testMultipleLines() {
        String text = "()\n+\n-";
        List<Token> tokens = runScanner(text);
        List<Token> expectedTokens = List.of(
                new Token(LEFT_PAREN, "(", null, 1),
                new Token(RIGHT_PAREN, ")", null, 1),
                new Token(PLUS, "+", null, 2),
                new Token(MINUS, "-", null, 3),
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
        return new Scanner(errorHandler).scan(text);
    }

    private void verifyNoErrors() {
        verify(errorHandler, never()).handleError(anyString(), anyInt());
    }
}