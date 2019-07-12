package com.gnopai.ji65.parser.statement;

import com.gnopai.ji65.parser.Parser;
import com.gnopai.ji65.scanner.Token;
import com.gnopai.ji65.scanner.TokenType;

public class UnnamedLabelStatementParselet implements StatementParselet {
    @Override
    public Statement parse(Token token, Parser parser) {
        parser.match(TokenType.EOL); // consume optional end-of-line
        return new UnnamedLabelStatement();
    }
}
