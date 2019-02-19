package com.gnopai.ji65.parser.statement;

import com.gnopai.ji65.parser.Parser;
import com.gnopai.ji65.scanner.Token;
import com.gnopai.ji65.scanner.TokenType;

import java.util.Map;

import static java.util.Optional.ofNullable;

public class MultiTokenStatementParselet implements StatementParselet {
    private final Map<TokenType, StatementParselet> parselets;

    public MultiTokenStatementParselet(Map<TokenType, StatementParselet> parselets) {
        this.parselets = parselets;
    }

    @Override
    public Statement parse(Token token, Parser parser) {
        Token nextToken = parser.consume();
        return ofNullable(parselets.get(nextToken.getType()))
                .map(parselet -> parselet.parse(nextToken, parser))
                .orElseThrow(() -> parser.error("Unexpected token " + nextToken.getType()));
    }
}
