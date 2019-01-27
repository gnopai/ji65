package com.gnopai.ji65.parser.expression;

import com.gnopai.ji65.parser.Parser;
import com.gnopai.ji65.parser.Precedence;
import com.gnopai.ji65.scanner.Token;

public interface InfixParselet {
    Expression parse(Token token, Expression left, Parser parser);

    Precedence getPrecedence();
}
