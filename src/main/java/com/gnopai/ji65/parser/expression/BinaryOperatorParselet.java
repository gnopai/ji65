package com.gnopai.ji65.parser.expression;

import com.gnopai.ji65.parser.Parser;
import com.gnopai.ji65.parser.Precedence;
import com.gnopai.ji65.scanner.Token;

public class BinaryOperatorParselet implements InfixParselet {
    private final Precedence precedence;

    public BinaryOperatorParselet(Precedence precedence) {
        this.precedence = precedence;
    }

    @Override
    public Expression parse(Token token, Expression left, Parser parser) {
        return new BinaryOperatorExpression(left, token.getType(), parser.expression(precedence));
    }

    @Override
    public Precedence getPrecedence() {
        return precedence;
    }
}
