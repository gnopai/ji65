package com.gnopai.ji65.parser.expression;

import com.gnopai.ji65.parser.Parser;
import com.gnopai.ji65.scanner.Token;

public class IdentifierParselet implements PrefixParselet {
    @Override
    public Expression parse(Token token, Parser parser) {
        return new IdentifierExpression(token.getLexeme());
    }
}
