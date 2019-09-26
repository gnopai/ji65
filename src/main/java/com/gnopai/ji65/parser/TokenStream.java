package com.gnopai.ji65.parser;

import com.gnopai.ji65.scanner.Token;
import com.gnopai.ji65.scanner.TokenType;
import com.gnopai.ji65.util.ErrorHandler;

import java.util.Iterator;
import java.util.List;

public class TokenStream {
    private final ErrorHandler errorHandler;
    private final Iterator<Token> iterator;
    private Token next;
    private Token current;
    private Token previous;
    private boolean hasError;

    public TokenStream(ErrorHandler errorHandler, List<Token> tokens) {
        this.errorHandler = errorHandler;
        this.iterator = tokens.iterator();
        this.next = iterator.hasNext() ? iterator.next() : null;
    }

    public boolean hasMore() {
        return iterator.hasNext() || (next != null && !TokenType.EOF.equals(next.getType()));
    }

    public Token consume() {
        previous = current;
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

    public String consumeString(String errorMessage) {
        StringBuilder stringBuilder = new StringBuilder();
        while (match(TokenType.CHAR)) {
            stringBuilder.append((char) (int) current.getValue());
        }
        String string = stringBuilder.toString();

        if (string.isEmpty()) {
            throw error(next, errorMessage);
        }
        return string;
    }

    public boolean match(TokenType tokenType) {
        if (tokenType.equals(next.getType())) {
            consume();
            return true;
        }
        return false;
    }

    public boolean match(TokenType tokenType, Object value) {
        if (tokenType.equals(next.getType()) && value.equals(next.getValue())) {
            consume();
            return true;
        }
        return false;
    }

    public Token peek() {
        return next;
    }

    public Token getPrevious() {
        return previous;
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
