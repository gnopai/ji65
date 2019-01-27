package com.gnopai.ji65.parser;

import com.gnopai.ji65.scanner.Token;

public class ParseException extends RuntimeException {
    private final Token token;

    public ParseException(Token token, String message) {
        super(message);
        this.token = token;
    }

    public Token getToken() {
        return token;
    }
}
