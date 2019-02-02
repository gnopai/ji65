package com.gnopai.ji65.parser;

import com.gnopai.ji65.parser.expression.*;
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
            TokenType.PLUS, new BinaryOperatorParselet(Precedence.SUM),
            TokenType.MINUS, new BinaryOperatorParselet(Precedence.SUM),
            TokenType.STAR, new BinaryOperatorParselet(Precedence.MULTIPLY),
            TokenType.SLASH, new BinaryOperatorParselet(Precedence.MULTIPLY)
    );

    private final Map<TokenType, PrefixParselet> prefixParselets = Map.of(
            TokenType.MINUS, new PrefixOperatorParselet(Precedence.UNARY),
            TokenType.NUMBER, new PrimaryParselet(),
            TokenType.STRING, new PrimaryParselet()
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
