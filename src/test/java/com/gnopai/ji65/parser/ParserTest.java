package com.gnopai.ji65.parser;

import com.gnopai.ji65.DirectiveType;
import com.gnopai.ji65.InstructionType;
import com.gnopai.ji65.parser.expression.BinaryOperatorExpression;
import com.gnopai.ji65.parser.expression.Expression;
import com.gnopai.ji65.parser.expression.PrimaryExpression;
import com.gnopai.ji65.parser.statement.DirectiveStatement;
import com.gnopai.ji65.parser.statement.InstructionStatement;
import com.gnopai.ji65.parser.statement.Statement;
import com.gnopai.ji65.parser.statement.StatementParselet;
import com.gnopai.ji65.scanner.Token;
import com.gnopai.ji65.scanner.TokenType;
import com.gnopai.ji65.util.ErrorHandler;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ParserTest {
    private final ErrorHandler errorHandler = mock(ErrorHandler.class);

    @Test
    void testParse_singleStatement() {
        ParseletFactory parseletFactory = mock(ParseletFactory.class);
        StatementParselet instructionStatementParselet = mock(StatementParselet.class);
        when(parseletFactory.getStatementParselet(TokenType.INSTRUCTION)).thenReturn(Optional.of(instructionStatementParselet));

        Token token = token(TokenType.INSTRUCTION, InstructionType.LDA);
        TokenStream tokenStream = tokenConsumer(token);
        Parser parser = new Parser(parseletFactory, tokenStream);

        Statement statement = instructionStatement(InstructionType.LDA);
        when(instructionStatementParselet.parse(token, parser)).thenReturn(statement);

        List<Statement> statements = parser.parse();
        assertEquals(List.of(statement), statements);
    }

    @Test
    void testParse_singleStatement_withEmptyLinesBefore() {
        ParseletFactory parseletFactory = mock(ParseletFactory.class);
        StatementParselet instructionStatementParselet = mock(StatementParselet.class);
        when(parseletFactory.getStatementParselet(TokenType.INSTRUCTION)).thenReturn(Optional.of(instructionStatementParselet));

        Token token = token(TokenType.INSTRUCTION, InstructionType.LDA);
        TokenStream tokenStream = tokenConsumer(
                token(TokenType.EOL),
                token(TokenType.EOL),
                token
        );
        Parser parser = new Parser(parseletFactory, tokenStream);

        Statement statement = instructionStatement(InstructionType.LDA);
        when(instructionStatementParselet.parse(token, parser)).thenReturn(statement);

        List<Statement> statements = parser.parse();
        assertEquals(List.of(statement), statements);
    }

    @Test
    void testParse_multipleStatements() {
        ParseletFactory parseletFactory = mock(ParseletFactory.class);
        StatementParselet instructionStatementParselet = mock(StatementParselet.class);
        when(parseletFactory.getStatementParselet(TokenType.INSTRUCTION)).thenReturn(Optional.of(instructionStatementParselet));
        StatementParselet directiveParselet = mock(StatementParselet.class);
        when(parseletFactory.getStatementParselet(TokenType.DIRECTIVE)).thenReturn(Optional.of(directiveParselet));

        Token token1 = token(TokenType.INSTRUCTION, InstructionType.LDA);
        Token token2 = token(TokenType.DIRECTIVE);
        Token token3 = token(TokenType.INSTRUCTION, InstructionType.STA);
        TokenStream tokenStream = tokenConsumer(token1, token2, token3);
        Parser parser = new Parser(parseletFactory, tokenStream);

        Statement statement1 = instructionStatement(InstructionType.LDA);
        when(instructionStatementParselet.parse(token1, parser)).thenReturn(statement1);
        Statement statement2 = directiveStatement(DirectiveType.BYTE);
        when(directiveParselet.parse(token2, parser)).thenReturn(statement2);
        Statement statement3 = instructionStatement(InstructionType.STA);
        when(instructionStatementParselet.parse(token3, parser)).thenReturn(statement3);

        List<Statement> statements = parser.parse();
        assertEquals(List.of(statement1, statement2, statement3), statements);
    }

    @Test
    void testParse_multipleStatements_withEmptyLines() {
        ParseletFactory parseletFactory = mock(ParseletFactory.class);
        StatementParselet instructionStatementParselet = mock(StatementParselet.class);
        when(parseletFactory.getStatementParselet(TokenType.INSTRUCTION)).thenReturn(Optional.of(instructionStatementParselet));
        StatementParselet directiveParselet = mock(StatementParselet.class);
        when(parseletFactory.getStatementParselet(TokenType.DIRECTIVE)).thenReturn(Optional.of(directiveParselet));

        Token token1 = token(TokenType.INSTRUCTION, InstructionType.LDA);
        Token token2 = token(TokenType.DIRECTIVE);
        Token token3 = token(TokenType.INSTRUCTION, InstructionType.STA);
        TokenStream tokenStream = tokenConsumer(
                token(TokenType.EOL),
                token1,
                token(TokenType.EOL),
                token(TokenType.EOL),
                token(TokenType.EOL),
                token2,
                token3
        );
        Parser parser = new Parser(parseletFactory, tokenStream);

        Statement statement1 = instructionStatement(InstructionType.LDA);
        when(instructionStatementParselet.parse(token1, parser)).thenReturn(statement1);
        Statement statement2 = directiveStatement(DirectiveType.BYTE);
        when(directiveParselet.parse(token2, parser)).thenReturn(statement2);
        Statement statement3 = instructionStatement(InstructionType.STA);
        when(instructionStatementParselet.parse(token3, parser)).thenReturn(statement3);

        List<Statement> statements = parser.parse();
        assertEquals(List.of(statement1, statement2, statement3), statements);
    }

    @Test
    void testParse_invalidStatementToken() {
        Token badToken = token(TokenType.STRING, "uhoh");
        TokenStream tokenStream = tokenConsumer(badToken);
        Parser parser = parser(tokenStream);

        ParseException parseException = assertThrows(ParseException.class, parser::parse);
        assertEquals(badToken, parseException.getToken());
    }

    @Test
    void testExpression_singleHighPrecedenceExpression() {
        TokenStream tokenStream = tokenConsumer(
                token(TokenType.NUMBER, 5),
                token(TokenType.STRING, "derp")
        );
        Parser parser = parser(tokenStream);

        Expression expression = parser.expression();
        assertEquals(new PrimaryExpression(TokenType.NUMBER, 5), expression);
    }

    @Test
    void testExpression_simpleExpressionChain() {
        TokenStream tokenStream = tokenConsumer(
                token(TokenType.NUMBER, 5),
                token(TokenType.PLUS),
                token(TokenType.NUMBER, 10),
                token(TokenType.STRING, "derp")
        );
        Parser parser = parser(tokenStream);

        Expression expression = parser.expression();
        Expression expectedExpression = new BinaryOperatorExpression(
                new PrimaryExpression(TokenType.NUMBER, 5),
                TokenType.PLUS,
                new PrimaryExpression(TokenType.NUMBER, 10)
        );
        assertEquals(expectedExpression, expression);
    }

    @Test
    void testExpression_moreComplexExpressionChain_precedenceHighToLow() {
        TokenStream tokenStream = tokenConsumer(
                token(TokenType.NUMBER, 2),
                token(TokenType.STAR),
                token(TokenType.NUMBER, 4),
                token(TokenType.PLUS, 10),
                token(TokenType.NUMBER, 6),
                token(TokenType.STRING, "derp")
        );
        Parser parser = parser(tokenStream);

        Expression expression = parser.expression();
        Expression expectedExpression = new BinaryOperatorExpression(
                new BinaryOperatorExpression(
                        new PrimaryExpression(TokenType.NUMBER, 2),
                        TokenType.STAR,
                        new PrimaryExpression(TokenType.NUMBER, 4)
                ),
                TokenType.PLUS,
                new PrimaryExpression(TokenType.NUMBER, 6)
        );
        assertEquals(expectedExpression, expression);
    }

    @Test
    void testExpression_moreComplexExpressionChain_precedenceLowToHigh() {
        TokenStream tokenStream = tokenConsumer(
                token(TokenType.NUMBER, 2),
                token(TokenType.PLUS),
                token(TokenType.NUMBER, 4),
                token(TokenType.STAR, 10),
                token(TokenType.NUMBER, 6),
                token(TokenType.STRING, "derp")
        );
        Parser parser = parser(tokenStream);

        Expression expression = parser.expression();

        Expression expectedExpression = new BinaryOperatorExpression(
                new PrimaryExpression(TokenType.NUMBER, 2),
                TokenType.PLUS,
                new BinaryOperatorExpression(
                        new PrimaryExpression(TokenType.NUMBER, 4),
                        TokenType.STAR,
                        new PrimaryExpression(TokenType.NUMBER, 6)
                ));
        assertEquals(expectedExpression, expression);
    }

    @Test
    void testExpression_invalidPrefixParseletToken() {
        TokenStream tokenStream = tokenConsumer(token(TokenType.PLUS));
        Parser parser = parser(tokenStream);

        ParseException parseException = assertThrows(ParseException.class, parser::expression);
        assertEquals(token(TokenType.PLUS), parseException.getToken());
    }

    @Test
    void testMatchEndOfLine_eol() {
        TokenStream tokenStream = tokenConsumer(token(TokenType.EOL));
        Parser parser = parser(tokenStream);
        assertTrue(parser.matchEndOfLine());
    }

    @Test
    void testMatchEndOfLine_eof() {
        TokenStream tokenStream = tokenConsumer(token(TokenType.EOF));
        Parser parser = parser(tokenStream);
        assertTrue(parser.matchEndOfLine());
    }

    @Test
    void testMatchEndOfLine_notEndOfLine() {
        TokenStream tokenStream = tokenConsumer(token(TokenType.COLON));
        Parser parser = parser(tokenStream);
        assertFalse(parser.matchEndOfLine());
    }

    @Test
    void testConsumeEndOfLine_eol() {
        TokenStream tokenStream = tokenConsumer(token(TokenType.EOL));
        Parser parser = parser(tokenStream);
        parser.consumeEndOfLine();
        // no exception thrown
    }

    @Test
    void testConsumeEndOfLine_eof() {
        TokenStream tokenStream = tokenConsumer(token(TokenType.EOF));
        Parser parser = parser(tokenStream);
        parser.consumeEndOfLine();
        // no exception thrown
    }

    @Test
    void testConsumeEndOfLine_notEndOfLine() {
        TokenStream tokenStream = tokenConsumer(token(TokenType.SEMICOLON));
        Parser parser = parser(tokenStream);
        ParseException parseException = assertThrows(ParseException.class, parser::consumeEndOfLine);
        assertEquals(TokenType.SEMICOLON, parseException.getTokenType());
        assertEquals("Expected end of line", parseException.getMessage());
    }

    private Parser parser(TokenStream tokenStream) {
        return new Parser(new ParseletFactory(null), tokenStream);
    }

    private TokenStream tokenConsumer(Token... tokens) {
        return new TokenStream(errorHandler, List.of(tokens));
    }

    private Statement instructionStatement(InstructionType type) {
        return InstructionStatement.builder()
                .instructionType(type)
                .build();
    }

    private Statement directiveStatement(DirectiveType type) {
        return DirectiveStatement.builder()
                .type(type)
                .build();
    }

    private Token token(TokenType type) {
        return token(type, null);
    }

    private Token token(TokenType type, Object value) {
        return new Token(type, null, value, 0, null);
    }
}