package com.gnopai.ji65.parser;

import com.gnopai.ji65.DirectiveType;
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
    private final TokenStream tokenStream;

    public Parser(ParseletFactory parseletFactory, TokenStream tokenStream) {
        this.parseletFactory = parseletFactory;
        this.tokenStream = tokenStream;
    }

    public List<Statement> parse() {
        List<Statement> statements = new ArrayList<>();
        while (tokenStream.hasMore()) {
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
        Token token = tokenStream.consume();
        while (TokenType.EOL.equals(token.getType())) {
            token = tokenStream.consume();
        }
        return token;
    }

    public Expression expression() {
        return expression(Precedence.lowest());
    }

    public Expression expression(Precedence precedence) {
        Token firstToken = tokenStream.consume();

        Expression expression = parsePrefixExpression(firstToken);
        Precedence nextTokenPrecedence = getPrecedence(tokenStream.peek());

        while (precedence.isLowerThan(nextTokenPrecedence)) {
            Token currentToken = tokenStream.consume();
            expression = parseInfixExpression(currentToken, expression);
            nextTokenPrecedence = getPrecedence(tokenStream.peek());
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
        return tokenStream.error(token, "Failed to parse token");
    }

    public ParseException error(String message) {
        return tokenStream.error(message);
    }

    public Token consume() {
        return tokenStream.consume();
    }

    public Token consume(TokenType tokenType, String errorMessage) {
        return tokenStream.consume(tokenType, errorMessage);
    }

    public String consumeString(String errorMessage) {
        return tokenStream.consumeString(errorMessage);
    }

    public boolean match(TokenType tokenType) {
        return tokenStream.match(tokenType);
    }

    public boolean matchDirective(DirectiveType directiveType) {
        return tokenStream.match(TokenType.DIRECTIVE, directiveType);
    }

    public TokenType peekNextTokenType() {
        return tokenStream.peek().getType();
    }
}
