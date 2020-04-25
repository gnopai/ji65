package com.gnopai.ji65.parser;

import com.gnopai.ji65.ParsingService;
import com.gnopai.ji65.parser.statement.Statement;
import com.gnopai.ji65.scanner.Token;
import com.gnopai.ji65.scanner.TokenType;
import com.gnopai.ji65.util.ErrorHandler;

import java.util.List;

import static org.mockito.Mockito.mock;

public class ParserTestUtil {
    public static Token token(TokenType type) {
        return token(type, null);
    }

    public static Token token(TokenType type, Object value) {
        String lexeme = value == null ? null : value.toString();
        return new Token(type, lexeme, value, 0, null);
    }

    public static Statement parse(ErrorHandler errorHandler, Token... tokens) {
        return parse(errorHandler, List.of(tokens));

    }
    public static Statement parse(ErrorHandler errorHandler, List<Token> tokens) {
        ParsingService parsingService = mock(ParsingService.class);
        return parse(errorHandler, parsingService, tokens);
    }

    public static Statement parse(ErrorHandler errorHandler, ParsingService parsingService, Token... tokens) {
        return parse(errorHandler, parsingService, List.of(tokens));
    }

    public static Statement parse(ErrorHandler errorHandler, ParsingService parsingService, List<Token> tokens) {
        TokenStream tokenStream = new TokenStream(errorHandler, tokens);
        ParseletFactory parseletFactory = new ParseletFactory(parsingService);
        Parser parser = new Parser(parseletFactory, tokenStream);
        return parser.statement();
    }
}
