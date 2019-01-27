package com.gnopai.ji65.parser;

import com.gnopai.ji65.parser.expression.InfixParselet;
import com.gnopai.ji65.parser.expression.PrefixParselet;
import com.gnopai.ji65.parser.expression.PrimaryParselet;
import com.gnopai.ji65.parser.statement.InstructionStatementParselet;
import com.gnopai.ji65.parser.statement.StatementParselet;
import com.gnopai.ji65.scanner.TokenType;

import java.util.Map;
import java.util.Optional;

public class ParseletFactory {
    private final Map<TokenType, StatementParselet> statementParsers = Map.of(
            TokenType.INSTRUCTION, new InstructionStatementParselet()
    );

    private final Map<TokenType, InfixParselet> infixParselets = Map.of(
    );

    private final Map<TokenType, PrefixParselet> prefixParselets = Map.of(
            TokenType.NUMBER, new PrimaryParselet()
    );

    public Optional<StatementParselet> getStatementParselet(TokenType tokenType) {
        return getParselet(statementParsers, tokenType);
    }

    public Optional<PrefixParselet> getPrefixParselet(TokenType tokenType) {
        return getParselet(prefixParselets, tokenType);
    }

    public Optional<InfixParselet> getInfixParselet(TokenType tokenType) {
        return getParselet(infixParselets, tokenType);
    }

    private <T> Optional<T> getParselet(Map<TokenType, T> map, TokenType tokenType) {
        return Optional.ofNullable(map.get(tokenType));
    }
}
