package com.gnopai.ji65.parser.expression;

import com.gnopai.ji65.parser.Parser;
import com.gnopai.ji65.parser.Precedence;
import com.gnopai.ji65.scanner.Token;

public class PrefixOperatorParselet implements PrefixParselet {
    private final Precedence precedence;

    public PrefixOperatorParselet(Precedence precedence) {
        this.precedence = precedence;
    }

    @Override
    public Expression parse(Token token, Parser parser) {
        return new PrefixExpression(token.getType(), parser.expression(precedence));
    }
}
