package com.gnopai.ji65.parser.statement;

import com.gnopai.ji65.parser.Parser;
import com.gnopai.ji65.scanner.Token;

public interface StatementParselet {
    Statement parse(Token token, Parser parser);
}
