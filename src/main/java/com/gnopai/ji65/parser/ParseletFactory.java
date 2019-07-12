package com.gnopai.ji65.parser;

import com.gnopai.ji65.parser.expression.*;
import com.gnopai.ji65.parser.statement.*;
import com.gnopai.ji65.scanner.TokenType;

import java.util.Map;
import java.util.Optional;

public class ParseletFactory {
    private final Map<TokenType, StatementParselet> statementParsers = Map.of(
            TokenType.INSTRUCTION, new InstructionStatementParselet(),
            TokenType.DIRECTIVE, new DirectiveStatementParselet(),
            TokenType.IDENTIFIER, new MultiTokenStatementParselet(Map.of(
                    TokenType.COLON, new LabelStatementParselet(),
                    TokenType.EQUAL, new AssignmentStatementParselet()
            )),
            TokenType.COLON, new UnnamedLabelStatementParselet()
    );

    private final Map<TokenType, InfixParselet> infixParselets = Map.of(
            TokenType.PLUS, new BinaryOperatorParselet(Precedence.SUM),
            TokenType.MINUS, new BinaryOperatorParselet(Precedence.SUM),
            TokenType.PIPE, new BinaryOperatorParselet(Precedence.SUM),
            TokenType.AMPERSAND, new BinaryOperatorParselet(Precedence.BITWISE),
            TokenType.CARET, new BinaryOperatorParselet(Precedence.BITWISE),
            TokenType.SHIFT_LEFT, new BinaryOperatorParselet(Precedence.BITWISE),
            TokenType.SHIFT_RIGHT, new BinaryOperatorParselet(Precedence.BITWISE),
            TokenType.STAR, new BinaryOperatorParselet(Precedence.MULTIPLY),
            TokenType.SLASH, new BinaryOperatorParselet(Precedence.MULTIPLY)
    );

    private final Map<TokenType, PrefixParselet> prefixParselets = Map.of(
            TokenType.LEFT_PAREN, new GroupParselet(),
            TokenType.MINUS, new PrefixOperatorParselet(Precedence.UNARY),
            TokenType.LESS_THAN, new PrefixOperatorParselet(Precedence.UNARY),
            TokenType.GREATER_THAN, new PrefixOperatorParselet(Precedence.UNARY),
            TokenType.TILDE, new PrefixOperatorParselet(Precedence.UNARY),
            TokenType.NUMBER, new PrimaryParselet(),
            TokenType.CHAR, new PrimaryParselet(),
            TokenType.STAR, new IdentifierParselet(),
            TokenType.COLON, new RelativeUnnamedLabelParselet(),
            TokenType.IDENTIFIER, new IdentifierParselet()
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
