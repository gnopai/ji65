package com.gnopai.ji65.scanner;

import com.gnopai.ji65.util.ErrorHandler;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import static com.gnopai.ji65.scanner.TokenType.EOF;
import static com.gnopai.ji65.scanner.TokenType.NUMBER;

public class TokenReader {
    private final ErrorHandler errorHandler;
    private SourceFile sourceFile;
    private List<Token> tokens;
    private int line;
    private int start;
    private int current;

    @Inject
    public TokenReader(ErrorHandler errorHandler) {
        this.errorHandler = errorHandler;
    }

    public List<Token> read(SourceFile sourceFile, Consumer<Character> consumer) {
        this.sourceFile = sourceFile;
        tokens = new ArrayList<>();
        line = 1;
        current = 0;

        while (!isAtEnd()) {
            start = current;
            char c = advance();
            consumer.accept(c);
        }

        tokens.add(new Token(EOF, "", null, line, sourceFile));

        return tokens;
    }

    public char advance() {
        current++;
        return sourceText().charAt(current - 1);
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
        return sourceText().substring(start + offset, current);
    }

    public void advanceWhileAlphaNumeric() {
        char nextChar = peek();
        while (isAlphaNumeric(nextChar)) {
            advance();
            nextChar = peek();
        }
    }

    public char peek() {
        return isAtEnd() ? '\0' : sourceText().charAt(current);
    }

    public boolean match(char expected) {
        if (isAtEnd()) {
            return false;
        }

        if (sourceText().charAt(current) != expected) {
            return false;
        }

        current++;
        return true;
    }

    public void addToken(TokenType type) {
        addToken(type, null);
    }

    public void addToken(TokenType type, Object literal) {
        String lexeme = sourceText().substring(start, current);
        Token token = new Token(type, lexeme, literal, line, sourceFile);
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
        return current >= sourceText().length();
    }

    public void error(String message) {
        errorHandler.handleError(message, sourceFile, line);
    }

    public Optional<Character> character() {
        Optional<String> string = string('\'', "char");

        if (string.isPresent() && string.map(String::length).get() == 1) {
            return string.map(s -> s.charAt(0));
        }

        error("Invalid char");
        return Optional.empty();
    }

    public Optional<String> string() {
        return string('"', "string");
    }

    private Optional<String> string(char delimiter, String typeName) {
        advanceUntilCharOrEndOfLine(delimiter);

        if (peek() == '\n' || isAtEnd()) {
            error("Unterminated " + typeName);
            return Optional.empty();
        }
        advance(); // consume closing delimiter char

        return Optional.of(sourceText().substring(start + 1, current - 1));
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

    private String sourceText() {
        return sourceFile.getText();
    }
}
