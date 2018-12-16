package com.gnopai.ji65.scanner;

import com.gnopai.ji65.directive.DirectiveType;
import com.gnopai.ji65.instruction.InstructionType;

import java.util.ArrayList;
import java.util.List;

import static com.gnopai.ji65.scanner.TokenType.*;

public class Scanner {
    private final ErrorHandler errorHandler;
    private String source;
    private List<Token> tokens;
    private int line;
    private int start;
    private int current;

    public Scanner(ErrorHandler errorHandler) {
        this.errorHandler = errorHandler;
    }

    public List<Token> scan(String source) {
        this.source = source;
        tokens = new ArrayList<>();
        line = 1;
        current = 0;

        while (!isAtEnd()) {
            start = current;
            scanToken();
        }

        tokens.add(new Token(EOF, "", null, line));

        return tokens;
    }

    private void scanToken() {
        char c = advance();
        switch (c) {
            case '(':
                addToken(LEFT_PAREN);
                break;
            case ')':
                addToken(RIGHT_PAREN);
                break;
            case ',':
                addToken(COMMA);
                break;
            case '-':
                addToken(MINUS);
                break;
            case '+':
                addToken(PLUS);
                break;
            case '/':
                addToken(SLASH);
                break;
            case '*':
                addToken(STAR);
                break;
            case '|':
                addToken(PIPE);
                break;
            case '&':
                addToken(AMPERSAND);
                break;
            case '@':
                addToken(AT_SIGN);
                break;
            case ':':
                addToken(COLON);
                break;
            case '#':
                addToken(POUND);
                break;
            case '=':
                addToken(EQUAL);
                break;
            case '^':
                addToken(CARET);
                break;
            case '~':
                addToken(TILDE);
                break;
            case '>':
                addToken(match('>') ? SHIFT_RIGHT : GREATER_THAN);
                break;
            case '<':
                addToken(match('<') ? SHIFT_LEFT : LESS_THAN);
                break;
            case ';':
                advanceUntilNextLine();
                break;
            case '"':
                string();
                break;
            case '0':
            case '1':
            case '2':
            case '3':
            case '4':
            case '5':
            case '6':
            case '7':
            case '8':
            case '9':
                decimalNumber();
                break;
            case '%':
                binaryNumber();
                break;
            case '$':
                hexNumber();
                break;
            case '.':
                directive();
                break;
            case ' ':
            case '\r':
            case '\t':
                // ignore most whitespace
                break;
            case '\n':
                line++;
                break;
            default:
                if (isLetter(c)) {
                    identifier();
                } else {
                    error("Unexpected character '" + c + "'");
                }
        }
    }

    private char advance() {
        current++;
        return source.charAt(current - 1);
    }

    private void advanceUntilNextLine() {
        while (peek() != '\n' && !isAtEnd()) {
            advance();
        }
    }

    private void advanceUntilCharOrEndOfLine(char c) {
        char nextChar = peek();
        while (nextChar != c && nextChar != '\n' && !isAtEnd()) {
            advance();
            nextChar = peek();
        }
    }

    private void advanceWhileAlphaNumeric() {
        char nextChar = peek();
        while (isAlphaNumeric(nextChar)) {
            advance();
            nextChar = peek();
        }
    }

    private char peek() {
        return isAtEnd() ? '\0' : source.charAt(current);
    }

    private boolean match(char expected) {
        if (isAtEnd()) {
            return false;
        }

        if (source.charAt(current) != expected) {
            return false;
        }

        current++;
        return true;
    }

    private void addToken(TokenType type) {
        addToken(type, null);
    }

    private void addToken(TokenType type, Object literal) {
        String lexeme = source.substring(start, current);
        Token token = new Token(type, lexeme, literal, line);
        tokens.add(token);
    }

    private boolean isAtEnd() {
        return current >= source.length();
    }

    private void error(String message) {
        errorHandler.handleError(message, line);
    }

    private void string() {
        advanceUntilCharOrEndOfLine('"');

        if (peek() == '\n' || isAtEnd()) {
            error("Unterminated string");
            return;
        }
        advance(); // closing '"'

        String value = source.substring(start + 1, current - 1);
        addToken(STRING, value);
    }

    private void decimalNumber() {
        advanceWhileAlphaNumeric();
        String value = source.substring(start, current);
        addNumberToken(value, 10, "decimal");
    }

    private void binaryNumber() {
        advanceWhileAlphaNumeric();
        String value = source.substring(start + 1, current);

        if (value.length() != 8) {
            error("Invalid binary number");
            return;
        }
        addNumberToken(value, 2, "binary");
    }

    private void hexNumber() {
        advanceWhileAlphaNumeric();
        String value = source.substring(start + 1, current);

        if (value.length() < 1 || value.length() > 4) {
            error("Invalid hex number");
            return;
        }
        addNumberToken(value, 16, "hex");
    }

    private void addNumberToken(String value, int radix, String numberType) {
        try {
            addToken(NUMBER, Integer.parseInt(value, radix));
        } catch (NumberFormatException e) {
            error("Invalid " + numberType + " number");
        }
    }

    private void directive() {
        advanceWhileAlphaNumeric();
        DirectiveType.fromIdentifier(source.substring(start + 1, current))
                .ifPresentOrElse(
                        directiveType -> addToken(DIRECTIVE, directiveType),
                        () -> error("Invalid directive")
                );
    }

    private void identifier() {
        advanceWhileAlphaNumeric();
        String string = source.substring(start, current);

        if (string.equalsIgnoreCase("X")) {
            addToken(X);
            return;
        } else if (string.equalsIgnoreCase("Y")) {
            addToken(Y);
            return;
        } else if (string.equalsIgnoreCase("A")) {
            addToken(A);
            return;
        }

        InstructionType.fromIdentifier(string)
                .ifPresentOrElse(
                        instructionType -> addToken(INSTRUCTION, instructionType),
                        () -> addToken(IDENTIFIER)
                );
    }

    private boolean isAlphaNumeric(char c) {
        return isLetter(c) ||
                (c == '_') ||
                (c >= '0' && c <= '9');
    }

    private boolean isLetter(char c) {
        return (c >= 'a' && c <= 'z') ||
                (c >= 'A' && c <= 'Z');
    }
}
