package com.gnopai.ji65.parser;

import com.gnopai.ji65.scanner.ErrorHandler;
import com.gnopai.ji65.scanner.Token;
import com.gnopai.ji65.scanner.TokenType;

import java.util.Iterator;
import java.util.List;

public class TokenConsumer {
    private final ErrorHandler errorHandler;
    private final Iterator<Token> iterator;
    private Token next;
    private Token current;
    private boolean hasError;

    public TokenConsumer(ErrorHandler errorHandler, List<Token> tokens) {
        this.errorHandler = errorHandler;
        this.iterator = tokens.iterator();
        this.next = iterator.hasNext() ? iterator.next() : null;
    }

    public boolean hasMore() {
        return iterator.hasNext() || (next != null && !TokenType.EOF.equals(next.getType()));
    }

    public Token consume() {
        current = next;
        next = iterator.hasNext() ? iterator.next() : null;
        return current;
    }

    public Token consume(TokenType tokenType, String message) {
        if (match(tokenType)) {
            return current;
        }
        throw error(next, message);
    }

    public boolean match(TokenType tokenType) {
        if (tokenType.equals(next.getType())) {
            consume();
            return true;
        }
        return false;
    }

    public Token peek() {
        return next;
    }

    public ParseException error(String message) {
        return error(current, message);
    }

    public ParseException error(Token token, String message) {
        errorHandler.handleError(message, token.getLine());
        hasError = true;
        return new ParseException(token, message);
    }

    public boolean hasError() {
        return hasError;
    }
}