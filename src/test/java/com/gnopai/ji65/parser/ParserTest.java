package com.gnopai.ji65.parser;

import com.gnopai.ji65.instruction.InstructionType;
import com.gnopai.ji65.parser.expression.InfixParselet;
import com.gnopai.ji65.parser.expression.PrefixParselet;
import com.gnopai.ji65.parser.statement.InstructionStatement;
import com.gnopai.ji65.parser.statement.Statement;
import com.gnopai.ji65.parser.statement.StatementParselet;
import com.gnopai.ji65.scanner.ErrorHandler;
import com.gnopai.ji65.scanner.Token;
import com.gnopai.ji65.scanner.TokenType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

// TODO tests!
class ParserTest {
    private final ErrorHandler errorHandler = mock(ErrorHandler.class);

    private final ParseletFactory parseletFactory = mock(ParseletFactory.class);
    private final StatementParselet instructionStatementParselet = mock(StatementParselet.class);
    private final StatementParselet directiveParselet = mock(StatementParselet.class);
    private final PrefixParselet minusParselet = mock(PrefixParselet.class);
    private final PrefixParselet tildeParselet = mock(PrefixParselet.class);
    private final InfixParselet plusParselet = mock(InfixParselet.class);
    private final InfixParselet ampresandParselet = mock(InfixParselet.class);

    @BeforeEach
    void setUp() {
        when(parseletFactory.getStatementParselet(TokenType.INSTRUCTION))
                .thenReturn(Optional.of(instructionStatementParselet));
        when(parseletFactory.getStatementParselet(TokenType.DIRECTIVE))
                .thenReturn(Optional.of(directiveParselet));
        when(parseletFactory.getPrefixParselet(TokenType.MINUS))
                .thenReturn(Optional.of(minusParselet));
        when(parseletFactory.getPrefixParselet(TokenType.TILDE))
                .thenReturn(Optional.of(tildeParselet));
        when(parseletFactory.getInfixParselet(TokenType.PLUS))
                .thenReturn(Optional.of(plusParselet));
        when(parseletFactory.getInfixParselet(TokenType.AMPERSAND))
                .thenReturn(Optional.of(ampresandParselet));
    }

    @Test
    void testParse_noTokens() {
        Parser parser = new Parser(
                parseletFactory,
                errorHandler,
                List.of()
        );

        List<Statement> statements = parser.parse();

        assertTrue(statements.isEmpty());
    }

    @Test
    void testParse_eofOnly() {
        Parser parser = new Parser(
                parseletFactory,
                errorHandler,
                List.of(eof())
        );

        List<Statement> statements = parser.parse();

        assertTrue(statements.isEmpty());
    }

    @Test
    void testParse_singleStatement() {
        Token token1 = token(TokenType.INSTRUCTION, 1);
        Parser parser = new Parser(
                parseletFactory,
                errorHandler,
                List.of(token1, eof())
        );
        Statement statement = instructionStatement(InstructionType.LDA);
        when(instructionStatementParselet.parse(token1, parser)).thenReturn(statement);

        List<Statement> statements = parser.parse();
        assertEquals(List.of(statement), statements);
    }

    private Statement instructionStatement(InstructionType type) {
        return InstructionStatement.builder()
                .instructionType(type)
                .build();
    }

    private Token token(TokenType type, Object value) {
        return new Token(type, null, value, 0);
    }

    private Token eof() {
        return token(TokenType.EOF, null);
    }
}