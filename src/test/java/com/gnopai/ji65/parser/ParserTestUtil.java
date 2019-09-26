package com.gnopai.ji65.parser;

import com.gnopai.ji65.parser.statement.Statement;
import com.gnopai.ji65.scanner.Token;
import com.gnopai.ji65.scanner.TokenType;
import com.gnopai.ji65.util.ErrorHandler;

import java.util.List;

public class ParserTestUtil {
    public static Token token(TokenType type) {
        return token(type, null);
    }

    public static Token token(TokenType type, Object value) {
        String lexeme = value == null ? null : value.toString();
        return new Token(type, lexeme, value, 0);
    }

    public static Statement parse(ErrorHandler errorHandler, Token... tokens) {
        TokenConsumer tokenConsumer = new TokenConsumer(errorHandler, List.of(tokens));
        Parser parser = new Parser(new ParseletFactory(), tokenConsumer);
        return parser.statement();
    }
}
