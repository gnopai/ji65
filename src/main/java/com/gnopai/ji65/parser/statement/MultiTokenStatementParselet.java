package com.gnopai.ji65.parser.statement;

import com.gnopai.ji65.parser.Parser;
import com.gnopai.ji65.scanner.Token;
import com.gnopai.ji65.scanner.TokenType;

import java.util.Map;

import static java.util.Optional.ofNullable;

public class MultiTokenStatementParselet implements StatementParselet {
    private final Map<TokenType, StatementParselet> parselets;
    private final StatementParselet defaultParselet;

    public MultiTokenStatementParselet(Map<TokenType, StatementParselet> parselets, StatementParselet defaultParselet) {
        this.parselets = parselets;
        this.defaultParselet = defaultParselet;
    }

    @Override
    public Statement parse(Token token, Parser parser) {
        TokenType nextTokenType = parser.peekNextTokenType();
        return ofNullable(parselets.get(nextTokenType))
                .map(parselet -> parselet.parse(token, parser))
                .orElseGet(() -> defaultParselet.parse(token, parser));
    }
}
