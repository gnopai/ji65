package com.gnopai.ji65.parser;

import com.gnopai.ji65.parser.expression.Expression;
import com.gnopai.ji65.parser.expression.InfixParselet;
import com.gnopai.ji65.parser.statement.Statement;
import com.gnopai.ji65.scanner.Token;
import com.gnopai.ji65.scanner.TokenType;

import java.util.ArrayList;
import java.util.List;

/**
 * A Pratt-style parser for creating statements and expressions out of tokens.
 */
public class Parser {
    private final ParseletFactory parseletFactory;
    private final TokenConsumer tokenConsumer;

    public Parser(ParseletFactory parseletFactory, TokenConsumer tokenConsumer) {
        this.parseletFactory = parseletFactory;
        this.tokenConsumer = tokenConsumer;
    }

    public List<Statement> parse() {
        List<Statement> statements = new ArrayList<>();
        while (tokenConsumer.hasMore()) {
            statements.add(statement());
        }
        return List.copyOf(statements);
    }

    public Statement statement() {
        Token token = nextNonEndOfLineToken();
        return parseletFactory.getStatementParselet(token.getType())
                .map(statementParselet -> statementParselet.parse(token, this))
                .orElseThrow(() -> error(token));
    }

    private Token nextNonEndOfLineToken() {
        Token token = tokenConsumer.consume();
        while (TokenType.EOL.equals(token.getType())) {
            token = tokenConsumer.consume();
        }
        return token;
    }

    public Expression expression() {
        return expression(Precedence.lowest());
    }

    public Expression expression(Precedence precedence) {
        Token firstToken = tokenConsumer.consume();

        Expression expression = parsePrefixExpression(firstToken);
        Precedence nextTokenPrecedence = getPrecedence(tokenConsumer.peek());

        while (precedence.isLowerThan(nextTokenPrecedence)) {
            Token currentToken = tokenConsumer.consume();
            expression = parseInfixExpression(currentToken, expression);
            nextTokenPrecedence = getPrecedence(tokenConsumer.peek());
        }

        return expression;
    }

    private Expression parsePrefixExpression(Token token) {
        return parseletFactory.getPrefixParselet(token.getType())
                .map(prefixParselet -> prefixParselet.parse(token, this))
                .orElseThrow(() -> error(token));
    }

    private Expression parseInfixExpression(Token token, Expression leftExpression) {
        return parseletFactory.getInfixParselet(token.getType())
                .map(infixParselet -> infixParselet.parse(token, leftExpression, this))
                .orElseThrow(() -> error(token));
    }

    private Precedence getPrecedence(Token token) {
        return parseletFactory.getInfixParselet(token.getType())
                .map(InfixParselet::getPrecedence)
                .orElse(Precedence.lowest());
    }

    private ParseException error(Token token) {
        return tokenConsumer.error(token, "Failed to parse token");
    }

    public ParseException error(String message) {
        return tokenConsumer.error(message);
    }

    public Token consume() {
        return tokenConsumer.consume();
    }

    public Token consume(TokenType tokenType, String errorMessage) {
        return tokenConsumer.consume(tokenType, errorMessage);
    }

    public String consumeString(String errorMessage) {
        return tokenConsumer.consumeString(errorMessage);
    }

    public boolean match(TokenType tokenType) {
        return tokenConsumer.match(tokenType);
    }

    public Token getPrevious() {
        return tokenConsumer.getPrevious();
    }

    public boolean hasError() {
        return tokenConsumer.hasError();
    }
}
