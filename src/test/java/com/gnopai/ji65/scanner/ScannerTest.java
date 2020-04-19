package com.gnopai.ji65.scanner;

import com.gnopai.ji65.DirectiveType;
import com.gnopai.ji65.InstructionType;
import com.gnopai.ji65.util.ErrorHandler;
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
        SourceFile sourceFile = new SourceFile(null, "");
        List<Token> tokens = runScanner(sourceFile);
        List<Token> expectedTokens = List.of(
                new Token(EOF, "", null, 1, sourceFile)
        );
        assertEquals(expectedTokens, tokens);
    }

    @Test
    void testSingleToken_singleCharTokens() {
        testSimpleSingleToken("(", LEFT_PAREN);
        testSimpleSingleToken(")", RIGHT_PAREN);
        testSimpleSingleToken("{", LEFT_BRACE);
        testSimpleSingleToken("}", RIGHT_BRACE);
        testSimpleSingleToken(",", COMMA);
        testSimpleSingleToken("-", MINUS);
        testSimpleSingleToken("+", PLUS);
        testSimpleSingleToken("/", SLASH);
        testSimpleSingleToken("*", STAR);
        testSimpleSingleToken("|", PIPE);
        testSimpleSingleToken("&", AMPERSAND);
        testSimpleSingleToken(":", COLON);
        testSimpleSingleToken("#", POUND);
        testSimpleSingleToken("=", EQUAL);
        testSimpleSingleToken("!", BANG);
        testSimpleSingleToken("^", CARET);
        testSimpleSingleToken("~", TILDE);
        testSimpleSingleToken(">", GREATER_THAN);
        testSimpleSingleToken("<", LESS_THAN);
    }

    @Test
    void testSingleToken_multiCharTokens() {
        testSimpleSingleToken("<<", SHIFT_LEFT);
        testSimpleSingleToken(">>", SHIFT_RIGHT);
        testSimpleSingleToken(">=", GREATER_OR_EQUAL_THAN);
        testSimpleSingleToken("<=", LESS_OR_EQUAL_THAN);
        testSimpleSingleToken("<>", NOT_EQUAL);
        testSimpleSingleToken("&&", AND);
        testSimpleSingleToken("||", OR);
    }

    @Test
    void testSingleToken_char() {
        String text = "'X'";
        SourceFile sourceFile = new SourceFile(null, text);
        List<Token> tokens = runScanner(sourceFile);
        List<Token> expectedTokens = List.of(
                new Token(CHAR, text, (int) 'X', 1, sourceFile),
                new Token(EOF, "", null, 1, sourceFile)
        );
        assertEquals(expectedTokens, tokens);
        verifyNoErrors();
    }

    @Test
    void testCharWithMoreThanOneCharacter() {
        String text = "'XY'";
        SourceFile sourceFile = new SourceFile(null, text);
        List<Token> tokens = runScanner(sourceFile);
        List<Token> expectedTokens = List.of(
                new Token(EOF, "", null, 1, sourceFile)
        );
        assertEquals(expectedTokens, tokens);
        verify(errorHandler).handleError("Invalid char", sourceFile, 1);
    }

    @Test
    void testCharWithNoCharacters() {
        String text = "''";
        SourceFile sourceFile = new SourceFile(null, text);
        List<Token> tokens = runScanner(sourceFile);
        List<Token> expectedTokens = List.of(
                new Token(EOF, "", null, 1, sourceFile)
        );
        assertEquals(expectedTokens, tokens);
        verify(errorHandler).handleError("Invalid char", sourceFile, 1);
    }

    @Test
    void testUnterminatedChar_endOfLine() {
        String text = "'X\n+";
        SourceFile sourceFile = new SourceFile(null, text);
        List<Token> tokens = runScanner(sourceFile);
        List<Token> expectedTokens = List.of(
                new Token(EOL, "\n", null, 1, sourceFile),
                new Token(PLUS, "+", null, 2, sourceFile),
                new Token(EOF, "", null, 2, sourceFile)
        );
        assertEquals(expectedTokens, tokens);
        verify(errorHandler).handleError("Unterminated char", sourceFile, 1);
    }

    @Test
    void testUnterminatedChar_endOfSourceText() {
        String text = "'X";
        SourceFile sourceFile = new SourceFile(null, text);
        List<Token> tokens = runScanner(sourceFile);
        List<Token> expectedTokens = List.of(
                new Token(EOF, "", null, 1, sourceFile)
        );
        assertEquals(expectedTokens, tokens);
        verify(errorHandler).handleError("Unterminated char", sourceFile, 1);
    }

    @Test
    void testSingleToken_string() {
        String text = "\"Ima String\"";
        SourceFile sourceFile = new SourceFile(null, text);
        List<Token> tokens = runScanner(sourceFile);
        List<Token> expectedTokens = List.of(
                new Token(CHAR, text, (int) 'I', 1, sourceFile),
                new Token(CHAR, text, (int) 'm', 1, sourceFile),
                new Token(CHAR, text, (int) 'a', 1, sourceFile),
                new Token(CHAR, text, (int) ' ', 1, sourceFile),
                new Token(CHAR, text, (int) 'S', 1, sourceFile),
                new Token(CHAR, text, (int) 't', 1, sourceFile),
                new Token(CHAR, text, (int) 'r', 1, sourceFile),
                new Token(CHAR, text, (int) 'i', 1, sourceFile),
                new Token(CHAR, text, (int) 'n', 1, sourceFile),
                new Token(CHAR, text, (int) 'g', 1, sourceFile),
                new Token(EOF, "", null, 1, sourceFile)
        );
        assertEquals(expectedTokens, tokens);
        verifyNoErrors();
    }

    @Test
    void testUnterminatedString_endOfLine() {
        String text = "\"Ima String\n+\"";
        SourceFile sourceFile = new SourceFile(null, text);
        List<Token> tokens = runScanner(sourceFile);
        List<Token> expectedTokens = List.of(
                new Token(EOL, "\n", null, 1, sourceFile),
                new Token(PLUS, "+", null, 2, sourceFile),
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
    void testSingleToken_binaryNumber_valid() {
        String text = "%00011010";
        SourceFile sourceFile = new SourceFile(null, text);
        List<Token> tokens = runScanner(sourceFile);
        List<Token> expectedTokens = List.of(
                new Token(NUMBER, text, 26, 1, sourceFile),
                new Token(EOF, "", null, 1, sourceFile)
        );
        assertEquals(expectedTokens, tokens);
        verifyNoErrors();
    }

    @Test
    void testSingleToken_binaryNumber_badChar() {
        String text = "%00011061";
        SourceFile sourceFile = new SourceFile(null, text);
        List<Token> tokens = runScanner(sourceFile);
        List<Token> expectedTokens = List.of(
                new Token(EOF, "", null, 1, sourceFile)
        );
        assertEquals(expectedTokens, tokens);
        verify(errorHandler).handleError("Invalid binary number", sourceFile, 1);
        verifyNoMoreInteractions(errorHandler);
    }

    @Test
    void testSingleToken_binaryNumber_tooShort() {
        String text = "%0001110";
        SourceFile sourceFile = new SourceFile(null, text);
        List<Token> tokens = runScanner(sourceFile);
        List<Token> expectedTokens = List.of(
                new Token(EOF, "", null, 1, sourceFile)
        );
        assertEquals(expectedTokens, tokens);
        verify(errorHandler).handleError("Invalid binary number", sourceFile, 1);
        verifyNoMoreInteractions(errorHandler);
    }

    @Test
    void testSingleToken_binaryNumber_noValue() {
        String text = "%";
        SourceFile sourceFile = new SourceFile(null, text);
        List<Token> tokens = runScanner(sourceFile);
        List<Token> expectedTokens = List.of(
                new Token(EOF, "", null, 1, sourceFile)
        );
        assertEquals(expectedTokens, tokens);
        verify(errorHandler).handleError("Invalid binary number", sourceFile, 1);
        verifyNoMoreInteractions(errorHandler);
    }

    @Test
    void testSingleToken_binaryNumber_tooLong() {
        String text = "%000111001";
        SourceFile sourceFile = new SourceFile(null, text);
        List<Token> tokens = runScanner(sourceFile);
        List<Token> expectedTokens = List.of(
                new Token(EOF, "", null, 1, sourceFile)
        );
        assertEquals(expectedTokens, tokens);
        verify(errorHandler).handleError("Invalid binary number", sourceFile, 1);
        verifyNoMoreInteractions(errorHandler);
    }

    @Test
    void testSingleToken_binaryNumber_trailingGarbage() {
        String text = "%00011100222";
        SourceFile sourceFile = new SourceFile(null, text);
        List<Token> tokens = runScanner(sourceFile);
        List<Token> expectedTokens = List.of(
                new Token(EOF, "", null, 1, sourceFile)
        );
        assertEquals(expectedTokens, tokens);
        verify(errorHandler).handleError("Invalid binary number", sourceFile, 1);
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
        testWhitespaceIgnored(" ");
        testWhitespaceIgnored("\r");
        testWhitespaceIgnored("\t");

        SourceFile sourceFile = new SourceFile(null, "\n");
        assertEquals(
                List.of(
                        new Token(EOL, "\n", null, 1, sourceFile),
                        new Token(EOF, "", null, 2, sourceFile)),
                runScanner(sourceFile)
        );
        verifyNoErrors();
    }

    private void testWhitespaceIgnored(String text) {
        SourceFile sourceFile = new SourceFile(null, text);
        Token expectedToken = new Token(EOF, "", null, 1, sourceFile);
        assertEquals(List.of(expectedToken), runScanner(sourceFile));
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
        String text = "; this is some stuff that is commented out\n+";
        SourceFile sourceFile = new SourceFile(null, text);
        List<Token> tokens = runScanner(sourceFile);
        List<Token> expectedTokens = List.of(
                new Token(EOL, "\n", null, 1, sourceFile),
                new Token(PLUS, "+", null, 2, sourceFile),
                new Token(EOF, "", null, 2, sourceFile)
        );
        assertEquals(expectedTokens, tokens);
        verifyNoErrors();
    }

    @Test
    void testComment_partOfLine() {
        String text = "() ; this is some stuff that is commented out\n+";
        SourceFile sourceFile = new SourceFile(null, text);
        List<Token> tokens = runScanner(sourceFile);
        List<Token> expectedTokens = List.of(
                new Token(LEFT_PAREN, "(", null, 1, sourceFile),
                new Token(RIGHT_PAREN, ")", null, 1, sourceFile),
                new Token(EOL, "\n", null, 1, sourceFile),
                new Token(PLUS, "+", null, 2, sourceFile),
                new Token(EOF, "", null, 2, sourceFile)
        );
        assertEquals(expectedTokens, tokens);
        verifyNoErrors();
    }

    @Test
    void testSingleToken_directive_valid() {
        String text = ".byte";
        SourceFile sourceFile = new SourceFile(null, text);
        List<Token> tokens = runScanner(sourceFile);
        List<Token> expectedTokens = List.of(
                new Token(DIRECTIVE, text, DirectiveType.BYTE, 1, sourceFile),
                new Token(EOF, "", null, 1, sourceFile)
        );
        assertEquals(expectedTokens, tokens);
        verifyNoErrors();
    }

    @Test
    void testSingleToken_directive_invalid() {
        String text = ".derp";
        SourceFile sourceFile = new SourceFile(null, text);
        List<Token> tokens = runScanner(sourceFile);
        List<Token> expectedTokens = List.of(
                new Token(EOF, "", null, 1, sourceFile)
        );
        assertEquals(expectedTokens, tokens);
        verify(errorHandler).handleError("Invalid directive", sourceFile, 1);
        verifyNoMoreInteractions(errorHandler);
    }

    @Test
    void testSingleToken_instruction() {
        String text = "LDA";
        SourceFile sourceFile = new SourceFile(null, text);
        List<Token> tokens = runScanner(sourceFile);
        List<Token> expectedTokens = List.of(
                new Token(INSTRUCTION, text, InstructionType.LDA, 1, sourceFile),
                new Token(EOF, "", null, 1, sourceFile)
        );
        assertEquals(expectedTokens, tokens);
        verifyNoErrors();
    }

    @Test
    void testSingleToken_registerX() {
        String text = "X";
        SourceFile sourceFile = new SourceFile(null, text);
        List<Token> tokens = runScanner(sourceFile);
        List<Token> expectedTokens = List.of(
                new Token(X, text, null, 1, sourceFile),
                new Token(EOF, "", null, 1, sourceFile)
        );
        assertEquals(expectedTokens, tokens);
        verifyNoErrors();
    }

    @Test
    void testSingleToken_registerY() {
        String text = "Y";
        SourceFile sourceFile = new SourceFile(null, text);
        List<Token> tokens = runScanner(sourceFile);
        List<Token> expectedTokens = List.of(
                new Token(Y, text, null, 1, sourceFile),
                new Token(EOF, "", null, 1, sourceFile)
        );
        assertEquals(expectedTokens, tokens);
        verifyNoErrors();
    }

    @Test
    void testSingleToken_registerA() {
        String text = "A";
        SourceFile sourceFile = new SourceFile(null, text);
        List<Token> tokens = runScanner(sourceFile);
        List<Token> expectedTokens = List.of(
                new Token(A, text, null, 1, sourceFile),
                new Token(EOF, "", null, 1, sourceFile)
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
    void testSingleToken_identifierWithAtSign() {
        String text = "@derp";
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
    void testSingleToken_identifierWithAtSignThatLooksLikeSomethingElse() {
        String text = "@x";
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
        String text = "()\n+\n-";
        SourceFile sourceFile = new SourceFile(null, text);
        List<Token> tokens = runScanner(sourceFile);
        List<Token> expectedTokens = List.of(
                new Token(LEFT_PAREN, "(", null, 1, sourceFile),
                new Token(RIGHT_PAREN, ")", null, 1, sourceFile),
                new Token(EOL, "\n", null, 1, sourceFile),
                new Token(PLUS, "+", null, 2, sourceFile),
                new Token(EOL, "\n", null, 2, sourceFile),
                new Token(MINUS, "-", null, 3, sourceFile),
                new Token(EOF, "", null, 3, sourceFile)
        );
        assertEquals(expectedTokens, tokens);
        verifyNoErrors();
    }

    private void testSimpleSingleToken(String text, TokenType type) {
        SourceFile sourceFile = new SourceFile(null, text);
        List<Token> tokens = runScanner(sourceFile);
        List<Token> expectedTokens = List.of(
                new Token(type, text, null, 1, sourceFile),
                new Token(EOF, "", null, 1, sourceFile)
        );
        assertEquals(expectedTokens, tokens);
        verifyNoErrors();
    }

    private List<Token> runScanner(SourceFile sourceFile) {
        return new Scanner(new TokenReader(errorHandler)).scan(sourceFile);
    }

    private void verifyNoErrors() {
        verify(errorHandler, never()).handleError(anyString(), any(SourceFile.class), anyInt());
    }
}