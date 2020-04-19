package com.gnopai.ji65.config;

import com.gnopai.ji65.scanner.SourceFile;
import com.gnopai.ji65.scanner.Token;
import com.gnopai.ji65.scanner.TokenReader;
import com.gnopai.ji65.scanner.TokenType;

import java.util.List;

import static com.gnopai.ji65.scanner.TokenType.*;

public class ConfigScanner {
    private final TokenReader tokenReader;

    public ConfigScanner(TokenReader tokenReader) {
        this.tokenReader = tokenReader;
    }

    public List<Token> scan(SourceFile sourceFile) {
        return tokenReader.read(sourceFile, this::scanToken);
    }

    private void scanToken(char c) {
        switch (c) {
            case '{':
                addToken(LEFT_BRACE);
                break;
            case '}':
                addToken(RIGHT_BRACE);
                break;
            case ',':
                addToken(COMMA);
                break;
            case ';':
                addToken(SEMICOLON);
                break;
            case ':':
                addToken(COLON);
                break;
            case '=':
                addToken(EQUAL);
                break;
            case '#':
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
            case '$':
                tokenReader.hexNumber();
                break;
            case ' ':
            case '\r':
            case '\t':
                // ignore most whitespace
                break;
            case '\n':
                tokenReader.incrementLine();
                break;
            case '%':
                identifier();
                break;
            default:
                if (tokenReader.isLetter(c)) {
                    identifier();
                } else {
                    tokenReader.error("Unexpected character '" + c + "'");
                }
        }
    }

    private void string() {
        tokenReader.string()
                .ifPresent(string -> tokenReader.addToken(STRING, string));
    }

    private void identifier() {
        tokenReader.advanceWhileAlphaNumeric();
        addToken(IDENTIFIER);
    }

    private void addToken(TokenType type) {
        tokenReader.addToken(type);
    }
}
