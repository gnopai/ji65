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
                addToken(tokenReader.match('>') ? SHIFT_RIGHT : GREATER_THAN);
                break;
            case '<':
                addToken(tokenReader.match('<') ? SHIFT_LEFT : LESS_THAN);
                break;
            case ';':
                tokenReader.advanceUntilNextLine();
                break;
            case '"':
                tokenReader.string();
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
