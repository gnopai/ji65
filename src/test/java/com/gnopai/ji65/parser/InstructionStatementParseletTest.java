package com.gnopai.ji65.parser;

import com.gnopai.ji65.parser.expression.PrimaryExpression;
import com.gnopai.ji65.parser.statement.InstructionStatement;
import com.gnopai.ji65.parser.statement.Statement;
import com.gnopai.ji65.scanner.ErrorHandler;
import com.gnopai.ji65.scanner.Token;
import com.gnopai.ji65.scanner.TokenType;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.gnopai.ji65.address.AddressingModeType.*;
import static com.gnopai.ji65.instruction.InstructionType.LDA;
import static com.gnopai.ji65.scanner.TokenType.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

class InstructionStatementParseletTest {
    private final ErrorHandler errorHandler = mock(ErrorHandler.class);

    @Test
    void testAbsolute() {
        Statement result = parse(
                token(INSTRUCTION, LDA),
                token(NUMBER, 99),
                token(EOF)
        );

        InstructionStatement expectedResult = InstructionStatement.builder()
                .instructionType(LDA)
                .addressExpression(new PrimaryExpression(NUMBER, 99))
                .addressingModeType(ABSOLUTE)
                .build();
        assertEquals(expectedResult, result);
    }

    @Test
    void testAccumulator() {
        Statement result = parse(
                token(INSTRUCTION, LDA),
                token(A),
                token(EOF)
        );

        InstructionStatement expectedResult = InstructionStatement.builder()
                .instructionType(LDA)
                .addressingModeType(ACCUMULATOR)
                .build();
        assertEquals(expectedResult, result);
    }

    @Test
    void testXIndexed() {
        Statement result = parse(
                token(INSTRUCTION, LDA),
                token(NUMBER, 50),
                token(COMMA),
                token(X),
                token(EOF)
        );

        InstructionStatement expectedResult = InstructionStatement.builder()
                .instructionType(LDA)
                .addressingModeType(ABSOLUTE_X)
                .addressExpression(new PrimaryExpression(NUMBER, 50))
                .build();
        assertEquals(expectedResult, result);
    }

    @Test
    void testYIndexed() {
        Statement result = parse(
                token(INSTRUCTION, LDA),
                token(NUMBER, 50),
                token(COMMA),
                token(Y),
                token(EOF)
        );

        InstructionStatement expectedResult = InstructionStatement.builder()
                .instructionType(LDA)
                .addressingModeType(ABSOLUTE_Y)
                .addressExpression(new PrimaryExpression(NUMBER, 50))
                .build();
        assertEquals(expectedResult, result);
    }

    @Test
    void testIndexed_invalid() {
        ParseException parseException = assertThrows(ParseException.class, () -> parse(
                token(INSTRUCTION, LDA),
                token(NUMBER, 50),
                token(COMMA),
                token(A),
                token(EOF)
        ));
        assertEquals(token(A), parseException.getToken());
        assertEquals("Expected X or Y for index", parseException.getMessage());
    }

    @Test
    void testIndexedIndirect() {
        Statement result = parse(
                token(INSTRUCTION, LDA),
                token(LEFT_PAREN),
                token(NUMBER, 50),
                token(COMMA),
                token(X),
                token(RIGHT_PAREN),
                token(EOF)
        );

        InstructionStatement expectedResult = InstructionStatement.builder()
                .instructionType(LDA)
                .addressingModeType(INDEXED_INDIRECT)
                .addressExpression(new PrimaryExpression(NUMBER, 50))
                .build();
        assertEquals(expectedResult, result);
    }

    @Test
    void testIndexedIndirect_missingClosingParen() {
        ParseException parseException = assertThrows(ParseException.class, () -> parse(
                token(INSTRUCTION, LDA),
                token(LEFT_PAREN),
                token(NUMBER, 50),
                token(COMMA),
                token(X),
                token(PLUS),
                token(EOF)
        ));
        assertEquals(token(PLUS), parseException.getToken());
        assertEquals("Expected closing parenthesis for indirect address", parseException.getMessage());
    }

    @Test
    void testIndexedIndirect_invalidRegister() {
        ParseException parseException = assertThrows(ParseException.class, () -> parse(
                token(INSTRUCTION, LDA),
                token(LEFT_PAREN),
                token(NUMBER, 50),
                token(COMMA),
                token(Y),
                token(RIGHT_PAREN),
                token(EOF)
        ));
        assertEquals(token(Y), parseException.getToken());
        assertEquals("Expected X for index", parseException.getMessage());
    }

    @Test
    void testIndirectIndexed() {
        Statement result = parse(
                token(INSTRUCTION, LDA),
                token(LEFT_PAREN),
                token(NUMBER, 50),
                token(RIGHT_PAREN),
                token(COMMA),
                token(Y),
                token(EOF)
        );

        InstructionStatement expectedResult = InstructionStatement.builder()
                .instructionType(LDA)
                .addressingModeType(INDIRECT_INDEXED)
                .addressExpression(new PrimaryExpression(NUMBER, 50))
                .build();
        assertEquals(expectedResult, result);
    }

    @Test
    void testIndirectIndexed_invalidRegister() {
        ParseException parseException = assertThrows(ParseException.class, () -> parse(
                token(INSTRUCTION, LDA),
                token(LEFT_PAREN),
                token(NUMBER, 50),
                token(RIGHT_PAREN),
                token(COMMA),
                token(X),
                token(EOF)
        ));
        assertEquals(token(X), parseException.getToken());
        assertEquals("Expected Y for index", parseException.getMessage());
    }

    @Test
    void testIndirect() {
        Statement result = parse(
                token(INSTRUCTION, LDA),
                token(LEFT_PAREN),
                token(NUMBER, 50),
                token(RIGHT_PAREN),
                token(EOF)
        );

        InstructionStatement expectedResult = InstructionStatement.builder()
                .instructionType(LDA)
                .addressingModeType(INDIRECT)
                .addressExpression(new PrimaryExpression(NUMBER, 50))
                .build();
        assertEquals(expectedResult, result);
    }

    @Test
    void testIndirect_missingClosingParen() {
        ParseException parseException = assertThrows(ParseException.class, () -> parse(
                token(INSTRUCTION, LDA),
                token(LEFT_PAREN),
                token(NUMBER, 50),
                token(EOF)
        ));
        assertEquals(token(EOF), parseException.getToken());
        assertEquals("Expected closing parenthesis for indirect address", parseException.getMessage());
    }

    private Token token(TokenType type) {
        return token(type, null);
    }

    private Token token(TokenType type, Object value) {
        return new Token(type, "", value, 0);
    }

    private Statement parse(Token... tokens) {
        Parser parser = new Parser(
                new ParseletFactory(),
                new TokenConsumer(errorHandler, List.of(tokens))
        );
        return parser.statement();
    }
}
