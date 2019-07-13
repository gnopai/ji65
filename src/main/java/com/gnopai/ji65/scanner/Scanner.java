package com.gnopai.ji65.scanner;

import com.gnopai.ji65.DirectiveType;
import com.gnopai.ji65.InstructionType;

import java.util.List;

import static com.gnopai.ji65.scanner.TokenType.*;

public class Scanner {
    private final TokenReader tokenReader;

    public Scanner(TokenReader tokenReader) {
        this.tokenReader = tokenReader;
    }

    public List<Token> scan(String source) {
        return tokenReader.read(source, this::scanToken);
    }

    private void scanToken(char c) {
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
                addToken(tokenReader.match('|') ? OR : PIPE);
                break;
            case '&':
                addToken(tokenReader.match('&') ? AND : AMPERSAND);
                break;
            case '@':
                identifier();
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
            case '!':
                addToken(BANG);
                break;
            case '^':
                addToken(CARET);
                break;
            case '~':
                addToken(TILDE);
                break;
            case '>':
                if (tokenReader.match('>')) {
                    addToken(SHIFT_RIGHT);
                } else if (tokenReader.match('=')) {
                    addToken(GREATER_OR_EQUAL_THAN);
                } else {
                    addToken(GREATER_THAN);
                }
                break;
            case '<':
                if (tokenReader.match('<')) {
                    addToken(SHIFT_LEFT);
                } else if (tokenReader.match('>')) {
                    addToken(NOT_EQUAL);
                } else if (tokenReader.match('=')) {
                    addToken(LESS_OR_EQUAL_THAN);
                } else {
                    addToken(LESS_THAN);
                }
                break;
            case ';':
                tokenReader.advanceUntilNextLine();
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
                tokenReader.decimalNumber();
                break;
            case '%':
                tokenReader.binaryNumber();
                break;
            case '$':
                tokenReader.hexNumber();
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
                addToken(EOL);
                tokenReader.incrementLine();
                break;
            default:
                if (tokenReader.isLetter(c)) {
                    identifier();
                } else {
                    error("Unexpected character '" + c + "'");
                }
        }
    }

    private void string() {
        tokenReader.string()
                .ifPresent(string -> string.chars().forEach(i -> addToken(CHAR, i)));
    }

    private void directive() {
        String value = tokenReader.getAlphaNumericValueWithOffset(1);
        DirectiveType.fromIdentifier(value)
                .ifPresentOrElse(
                        directiveType -> addToken(DIRECTIVE, directiveType),
                        () -> error("Invalid directive")
                );
    }

    private void identifier() {
        String string = tokenReader.getAlphaNumericValue();

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

    private void addToken(TokenType type) {
        tokenReader.addToken(type);
    }

    private void addToken(TokenType type, Object value) {
        tokenReader.addToken(type, value);
    }

    private void error(String message) {
        tokenReader.error(message);
    }
}
