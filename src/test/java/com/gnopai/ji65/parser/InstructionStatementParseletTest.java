package com.gnopai.ji65.parser;

import com.gnopai.ji65.parser.expression.PrimaryExpression;
import com.gnopai.ji65.parser.statement.InstructionStatement;
import com.gnopai.ji65.parser.statement.Statement;
import com.gnopai.ji65.scanner.ErrorHandler;
import com.gnopai.ji65.scanner.Token;
import com.gnopai.ji65.scanner.TokenType;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.gnopai.ji65.address.AddressingModeType.ABSOLUTE;
import static com.gnopai.ji65.address.AddressingModeType.ACCUMULATOR;
import static com.gnopai.ji65.instruction.InstructionType.LDA;
import static com.gnopai.ji65.scanner.TokenType.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

// TODO get the parser tests working first, then come back here.
// There's some issues with the Parser behavior that needs to be resolved first.
class InstructionStatementParseletTest {
    private final ErrorHandler errorHandler = mock(ErrorHandler.class);

    @Test
    void testAbsolute() {
        Statement result = parse(
                token(INSTRUCTION, LDA),
                token(NUMBER, 99)
        );

        InstructionStatement expectedResult = InstructionStatement.builder()
                .instructionType(LDA)
                .addressExpression(new PrimaryExpression(99))
                .addressingModeType(ABSOLUTE)
                .build();
        assertEquals(expectedResult, result);
    }

    @Test
    void testAccumulator() {
        Statement result = parse(
                token(INSTRUCTION, LDA),
                token(A)
        );

        InstructionStatement expectedResult = InstructionStatement.builder()
                .instructionType(LDA)
                .addressingModeType(ACCUMULATOR)
                .build();
        assertEquals(expectedResult, result);
    }

    private Token token(TokenType type) {
        return token(type, null);
    }

    private Token token(TokenType type, Object value) {
        return new Token(type, "", value, 0);
    }

    private Statement parse(Token... tokens) {
        Parser parser = new Parser(
                new ParseletFactory(), // FIXME mock??
                errorHandler,
                List.of(tokens));

        return parser.statement();
    }
}
