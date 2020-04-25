package com.gnopai.ji65.parser;

import com.gnopai.ji65.SourceFileProcessor;
import com.gnopai.ji65.parser.expression.*;
import com.gnopai.ji65.parser.statement.*;
import com.gnopai.ji65.scanner.TokenType;

import java.util.Map;
import java.util.Optional;

import static java.util.Map.entry;

public class ParseletFactory {
    private final Map<TokenType, StatementParselet> statementParselets;
    private final Map<TokenType, InfixParselet> infixParselets;
    private final Map<TokenType, PrefixParselet> prefixParselets;

    public ParseletFactory(SourceFileProcessor sourceFileProcessor) {
        statementParselets = Map.of(
                TokenType.INSTRUCTION, new InstructionStatementParselet(),
                TokenType.DIRECTIVE, new DirectiveStatementParselet(sourceFileProcessor),
                TokenType.IDENTIFIER, new MultiTokenStatementParselet(Map.of(
                        TokenType.COLON, new LabelStatementParselet(),
                        TokenType.EQUAL, new AssignmentStatementParselet()
                ), new MacroCallParselet(sourceFileProcessor)),
                TokenType.COLON, new UnnamedLabelStatementParselet()
        );

        infixParselets = Map.ofEntries(
                entry(TokenType.PLUS, new BinaryOperatorParselet(Precedence.SUM)),
                entry(TokenType.MINUS, new BinaryOperatorParselet(Precedence.SUM)),
                entry(TokenType.PIPE, new BinaryOperatorParselet(Precedence.SUM)),
                entry(TokenType.AMPERSAND, new BinaryOperatorParselet(Precedence.BITWISE)),
                entry(TokenType.CARET, new BinaryOperatorParselet(Precedence.BITWISE)),
                entry(TokenType.SHIFT_LEFT, new BinaryOperatorParselet(Precedence.BITWISE)),
                entry(TokenType.SHIFT_RIGHT, new BinaryOperatorParselet(Precedence.BITWISE)),
                entry(TokenType.STAR, new BinaryOperatorParselet(Precedence.MULTIPLY)),
                entry(TokenType.SLASH, new BinaryOperatorParselet(Precedence.MULTIPLY)),
                entry(TokenType.EQUAL, new BinaryOperatorParselet(Precedence.COMPARISON)),
                entry(TokenType.NOT_EQUAL, new BinaryOperatorParselet(Precedence.COMPARISON)),
                entry(TokenType.GREATER_THAN, new BinaryOperatorParselet(Precedence.COMPARISON)),
                entry(TokenType.GREATER_OR_EQUAL_THAN, new BinaryOperatorParselet(Precedence.COMPARISON)),
                entry(TokenType.LESS_THAN, new BinaryOperatorParselet(Precedence.COMPARISON)),
                entry(TokenType.LESS_OR_EQUAL_THAN, new BinaryOperatorParselet(Precedence.COMPARISON)),
                entry(TokenType.AND, new BinaryOperatorParselet(Precedence.BOOLEAN_AND)),
                entry(TokenType.OR, new BinaryOperatorParselet(Precedence.BOOLEAN_OR))
        );

        prefixParselets = Map.ofEntries(
                entry(TokenType.LEFT_PAREN, new GroupParselet()),
                entry(TokenType.MINUS, new PrefixOperatorParselet(Precedence.UNARY)),
                entry(TokenType.LESS_THAN, new PrefixOperatorParselet(Precedence.UNARY)),
                entry(TokenType.GREATER_THAN, new PrefixOperatorParselet(Precedence.UNARY)),
                entry(TokenType.TILDE, new PrefixOperatorParselet(Precedence.UNARY)),
                entry(TokenType.BANG, new PrefixOperatorParselet(Precedence.BOOLEAN_NOT)),
                entry(TokenType.NUMBER, new PrimaryParselet()),
                entry(TokenType.CHAR, new PrimaryParselet()),
                entry(TokenType.STAR, new IdentifierParselet()),
                entry(TokenType.COLON, new RelativeUnnamedLabelParselet()),
                entry(TokenType.IDENTIFIER, new IdentifierParselet())
        );
    }

    public Optional<StatementParselet> getStatementParselet(TokenType tokenType) {
        return getParselet(statementParselets, tokenType);
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
