package com.gnopai.ji65.parser.expression;

import com.gnopai.ji65.InstructionType;
import com.gnopai.ji65.parser.ParseException;
import com.gnopai.ji65.parser.statement.InstructionStatement;
import com.gnopai.ji65.parser.statement.Statement;
import com.gnopai.ji65.util.ErrorHandler;
import org.junit.jupiter.api.Test;

import static com.gnopai.ji65.parser.ParserTestUtil.parse;
import static com.gnopai.ji65.parser.ParserTestUtil.token;
import static com.gnopai.ji65.scanner.TokenType.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class RelativeUnnamedLabelParseletTest {
    private final ErrorHandler errorHandler = mock(ErrorHandler.class);

    @Test
    void testPositiveOffset() {
        Statement result = parse(errorHandler,
                token(INSTRUCTION, InstructionType.BCC),
                token(COLON),
                token(PLUS),
                token(PLUS),
                token(EOL)
        );

        assertTrue(result instanceof InstructionStatement);
        Expression expectedExpression = new RelativeUnnamedLabelExpression(2);
        assertEquals(expectedExpression, ((InstructionStatement) result).getAddressExpression());
    }

    @Test
    void testNegativeOffset() {
        Statement result = parse(errorHandler,
                token(INSTRUCTION, InstructionType.BCC),
                token(COLON),
                token(MINUS),
                token(MINUS),
                token(MINUS),
                token(EOL)
        );

        assertTrue(result instanceof InstructionStatement);
        Expression expectedExpression = new RelativeUnnamedLabelExpression(-3);
        assertEquals(expectedExpression, ((InstructionStatement) result).getAddressExpression());
    }

    @Test
    void testMissingOffset() {
        ParseException parseException = assertThrows(ParseException.class, () -> parse(errorHandler,
                token(INSTRUCTION, InstructionType.BCC),
                token(COLON),
                token(EOL)
        ));
        assertEquals("Expected plus or minus for referencing unnamed label", parseException.getMessage());
    }
}