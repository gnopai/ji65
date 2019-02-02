package com.gnopai.ji65.parser.expression;

import com.gnopai.ji65.parser.Parser;
import com.gnopai.ji65.scanner.Token;

public interface PrefixParselet {
    Expression parse(Token token, Parser parser);
}
