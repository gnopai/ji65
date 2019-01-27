package com.gnopai.ji65.parser.expression;

import com.gnopai.ji65.parser.Parser;
import com.gnopai.ji65.scanner.Token;

public class PrimaryParselet implements PrefixParselet {
    @Override
    public Expression parse(Token token, Parser parser) {
        return new PrimaryExpression(token.getValue());
    }
}
