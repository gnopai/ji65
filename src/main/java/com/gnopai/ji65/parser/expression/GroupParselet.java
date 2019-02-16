package com.gnopai.ji65.parser.expression;

import com.gnopai.ji65.parser.Parser;
import com.gnopai.ji65.scanner.Token;
import com.gnopai.ji65.scanner.TokenType;

public class GroupParselet implements PrefixParselet {
    @Override
    public Expression parse(Token token, Parser parser) {
        Expression expression = parser.expression();
        parser.consume(TokenType.RIGHT_PAREN, "Expected closing parenthesis");
        return expression;
    }
}
