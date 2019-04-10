package com.gnopai.ji65.scanner;

import com.gnopai.ji65.util.ErrorHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import static com.gnopai.ji65.scanner.TokenType.EOF;
import static com.gnopai.ji65.scanner.TokenType.NUMBER;

public class TokenReader {
    private final ErrorHandler errorHandler;
    private String source;
    private List<Token> tokens;
    private int line;
    private int start;
    private int current;

    public TokenReader(ErrorHandler errorHandler) {
        this.errorHandler = errorHandler;
    }

    public List<Token> read(String source, Consumer<Character> consumer) {
        this.source = source;
        tokens = new ArrayList<>();
        line = 1;
        current = 0;

        while (!isAtEnd()) {
            start = current;
            char c = advance();
            consumer.accept(c);
        }

        tokens.add(new Token(EOF, "", null, line));

        return tokens;
    }

    public char advance() {
        current++;
        return source.charAt(current - 1);
    }

    public void advanceUntilNextLine() {
        while (peek() != '\n' && !isAtEnd()) {
            advance();
        }
    }

    public void advanceUntilCharOrEndOfLine(char c) {
        char nextChar = peek();
        while (nextChar != c && nextChar != '\n' && !isAtEnd()) {
            advance();
            nextChar = peek();
        }
    }

    public String getAlphaNumericValue() {
        return getAlphaNumericValueWithOffset(0);
    }

    public String getAlphaNumericValueWithOffset(int offset) {
        advanceWhileAlphaNumeric();
        return source.substring(start + offset, current);
    }

    public void advanceWhileAlphaNumeric() {
        char nextChar = peek();
        while (isAlphaNumeric(nextChar)) {
            advance();
            nextChar = peek();
        }
    }

    public char peek() {
        return isAtEnd() ? '\0' : source.charAt(current);
    }

    public boolean match(char expected) {
        if (isAtEnd()) {
            return false;
        }

        if (source.charAt(current) != expected) {
            return false;
        }

        current++;
        return true;
    }

    public void addToken(TokenType type) {
        addToken(type, null);
    }

    public void addToken(TokenType type, Object literal) {
        String lexeme = source.substring(start, current);
        Token token = new Token(type, lexeme, literal, line);
        tokens.add(token);
    }

    public void decimalNumber() {
        String value = getAlphaNumericValue();
        addNumberToken(value, 10, "decimal");
    }

    public void binaryNumber() {
        String value = getAlphaNumericValueWithOffset(1);

        if (value.length() != 8) {
            error("Invalid binary number");
            return;
        }
        addNumberToken(value, 2, "binary");
    }

    public void hexNumber() {
        String value = getAlphaNumericValueWithOffset(1);

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

    private boolean isAtEnd() {
        return current >= source.length();
    }

    public void error(String message) {
        errorHandler.handleError(message, line);
    }

    public Optional<String> string() {
        advanceUntilCharOrEndOfLine('"');

        if (peek() == '\n' || isAtEnd()) {
            error("Unterminated string");
            return Optional.empty();
        }
        advance(); // closing '"'

        return Optional.of(source.substring(start + 1, current - 1));
    }

    public void incrementLine() {
        line++;
    }

    private boolean isAlphaNumeric(char c) {
        return isLetter(c) || isNumeric(c) || c == '_';
    }

    public boolean isLetter(char c) {
        return (c >= 'a' && c <= 'z') ||
                (c >= 'A' && c <= 'Z');
    }

    private boolean isNumeric(char c) {
        return (c >= '0' && c <= '9');
    }

}
