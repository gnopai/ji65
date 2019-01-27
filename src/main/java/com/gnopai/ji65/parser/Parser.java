package com.gnopai.ji65.parser;

import com.gnopai.ji65.parser.expression.Expression;
import com.gnopai.ji65.parser.expression.InfixParselet;
import com.gnopai.ji65.parser.statement.Statement;
import com.gnopai.ji65.scanner.ErrorHandler;
import com.gnopai.ji65.scanner.Token;
import com.gnopai.ji65.scanner.TokenType;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

// TODO unit tests, integration tests!
public class Parser {
    private final ParseletFactory parseletFactory;
    private final ErrorHandler errorHandler;
    private final List<Token> tokens;
    private final Iterator<Token> iterator;

    private Token current;
    private Token previous;
    private boolean hasError = false;

    public Parser(ParseletFactory parseletFactory, ErrorHandler errorHandler, List<Token> tokens) {
        this.parseletFactory = parseletFactory;
        this.errorHandler = errorHandler;
        this.tokens = tokens;
        this.iterator = tokens.iterator();
    }

    public List<Statement> parse() {
        List<Statement> statements = new ArrayList<>();
        while (!isAtEnd()) {
            statements.add(statement());
        }
        return List.copyOf(statements);
    }

    public Statement statement() {
        Token token = consume();
        return parseletFactory.getStatementParselet(token.getType())
                .map(statementParselet -> statementParselet.parse(token, this))
                .orElseThrow(() -> error(token, "Failed to parse token"));
    }

    private boolean isAtEnd() {
        return !iterator.hasNext() || current == null || current.getType().equals(TokenType.EOF);
    }

    public Expression expression() {
        return expression(Precedence.lowest());
    }

    public Expression expression(Precedence precedence) {
        Token firstToken = consume();

        Expression expression = parseletFactory.getPrefixParselet(firstToken.getType())
                .map(prefixParselet -> prefixParselet.parse(firstToken, this))
                .orElseThrow(() -> error(firstToken, "Failed to parse token"));

        while (precedence.ordinal() < getPrecedence().ordinal()) {
            Token token = consume();
            Expression leftExpression = expression;
            expression = parseletFactory.getInfixParselet(token.getType())
                    .map(infixParselet -> infixParselet.parse(token, leftExpression, this))
                    .orElseThrow(() -> error(token, "Failed to parse token"));
        }

        return expression;
    }

    private Precedence getPrecedence() {
        return parseletFactory.getInfixParselet(current.getType())
                .map(InfixParselet::getPrecedence)
                .orElse(Precedence.lowest());
    }

    public Token consume() {
        previous = current;
        current = iterator.hasNext() ? iterator.next() : null;
        return current;
    }

    public Token consume(TokenType tokenType, String message) {
        if (match(tokenType)) {
            return current;
        }
        throw error(message);
    }

    public boolean match(TokenType tokenType) {
        if (tokenType.equals(current.getType())) {
            consume();
            return true;
        }
        return false;
    }

    public ParseException error(String message) {
        return error(current, message);
    }

    private ParseException error(Token token, String message) {
        errorHandler.handleError(message, token.getLine());
        hasError = true;
        return new ParseException(token, message);
    }
}
