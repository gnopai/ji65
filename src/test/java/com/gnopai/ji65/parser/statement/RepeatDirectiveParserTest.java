package com.gnopai.ji65.parser.statement;

import com.gnopai.ji65.AddressingModeType;
import com.gnopai.ji65.DirectiveType;
import com.gnopai.ji65.InstructionType;
import com.gnopai.ji65.parser.ParseException;
import com.gnopai.ji65.parser.expression.PrimaryExpression;
import com.gnopai.ji65.util.ErrorHandler;
import org.junit.jupiter.api.Test;

import static com.gnopai.ji65.parser.ParserTestUtil.parse;
import static com.gnopai.ji65.parser.ParserTestUtil.token;
import static com.gnopai.ji65.scanner.TokenType.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

class RepeatDirectiveParserTest {
    private final ErrorHandler errorHandler = mock(ErrorHandler.class);

    @Test
    void testSingleStatement_noIndexIdentifier() {
        Statement result = parse(errorHandler,
                token(DIRECTIVE, DirectiveType.REPEAT),
                token(NUMBER, 5),
                token(EOL),
                token(INSTRUCTION, InstructionType.CLC),
                token(EOL),
                token(DIRECTIVE, DirectiveType.REPEAT_END),
                token(EOL)
        );

        DirectiveStatement expectedResult = DirectiveStatement.builder()
                .type(DirectiveType.REPEAT)
                .statement(InstructionStatement.builder()
                        .instructionType(InstructionType.CLC)
                        .addressingModeType(AddressingModeType.IMPLICIT)
                        .build())
                .expression(new PrimaryExpression(NUMBER, 5))
                .build();
        assertEquals(expectedResult, result);
    }

    @Test
    void testSingleStatement_withIndexIdentifier() {
        Statement result = parse(errorHandler,
                token(DIRECTIVE, DirectiveType.REPEAT),
                token(NUMBER, 5),
                token(COMMA),
                token(IDENTIFIER, "i"),
                token(EOL),
                token(INSTRUCTION, InstructionType.CLC),
                token(EOL),
                token(DIRECTIVE, DirectiveType.REPEAT_END),
                token(EOL)
        );

        DirectiveStatement expectedResult = DirectiveStatement.builder()
                .type(DirectiveType.REPEAT)
                .statement(InstructionStatement.builder()
                        .instructionType(InstructionType.CLC)
                        .addressingModeType(AddressingModeType.IMPLICIT)
                        .build())
                .expression(new PrimaryExpression(NUMBER, 5))
                .argument("i")
                .build();
        assertEquals(expectedResult, result);
    }

    @Test
    void testSingleStatement_withCommaButNoIndexIdentifier() {
        ParseException parseException = assertThrows(ParseException.class, () -> parse(errorHandler,
                token(DIRECTIVE, DirectiveType.REPEAT),
                token(NUMBER, 5),
                token(COMMA),
                token(EOL),
                token(INSTRUCTION, InstructionType.CLC),
                token(EOL),
                token(DIRECTIVE, DirectiveType.REPEAT_END),
                token(EOL)
        ));
        assertEquals(token(EOL), parseException.getToken());
        assertEquals("Expected repeat index identifier", parseException.getMessage());
    }

    @Test
    void testSingleStatement_missingEndOfLineAfterDirective() {
        ParseException parseException = assertThrows(ParseException.class, () -> parse(errorHandler,
                token(DIRECTIVE, DirectiveType.REPEAT),
                token(NUMBER, 5),
                token(INSTRUCTION, InstructionType.CLC),
                token(EOL),
                token(DIRECTIVE, DirectiveType.REPEAT_END),
                token(EOL)
        ));
        assertEquals(token(INSTRUCTION, InstructionType.CLC), parseException.getToken());
        assertEquals("Expected end of line", parseException.getMessage());
    }

    @Test
    void testSingleStatement_missingEndOfLineAfterEndRepeatDirective() {
        ParseException parseException = assertThrows(ParseException.class, () -> parse(errorHandler,
                token(DIRECTIVE, DirectiveType.REPEAT),
                token(NUMBER, 5),
                token(EOL),
                token(INSTRUCTION, InstructionType.CLC),
                token(EOL),
                token(DIRECTIVE, DirectiveType.REPEAT_END),
                token(NUMBER)
        ));
        assertEquals(token(NUMBER), parseException.getToken());
        assertEquals("Expected end of line", parseException.getMessage());
    }

    @Test
    void testSingleStatement_missingEndRepeatDirective() {
        ParseException parseException = assertThrows(ParseException.class, () -> parse(errorHandler,
                token(DIRECTIVE, DirectiveType.REPEAT),
                token(NUMBER, 5),
                token(EOL),
                token(INSTRUCTION, InstructionType.CLC),
                token(EOL),
                token(EOF)
        ));

        // TODO better handling of missing end token
        assertEquals(token(EOF), parseException.getToken());
        assertEquals("Failed to parse token", parseException.getMessage());
    }

    @Test
    void testMultipleStatements_noIndexIdentifier() {
        Statement result = parse(errorHandler,
                token(DIRECTIVE, DirectiveType.REPEAT),
                token(NUMBER, 5),
                token(EOL),
                token(INSTRUCTION, InstructionType.CLC),
                token(EOL),
                token(INSTRUCTION, InstructionType.CLV),
                token(EOL),
                token(DIRECTIVE, DirectiveType.REPEAT_END),
                token(EOL)
        );

        DirectiveStatement expectedResult = DirectiveStatement.builder()
                .type(DirectiveType.REPEAT)
                .statement(InstructionStatement.builder()
                        .instructionType(InstructionType.CLC)
                        .addressingModeType(AddressingModeType.IMPLICIT)
                        .build())
                .statement(InstructionStatement.builder()
                        .instructionType(InstructionType.CLV)
                        .addressingModeType(AddressingModeType.IMPLICIT)
                        .build())
                .expression(new PrimaryExpression(NUMBER, 5))
                .build();
        assertEquals(expectedResult, result);
    }

    @Test
    void testNestedRepeats() {
        Statement result = parse(errorHandler,
                token(DIRECTIVE, DirectiveType.REPEAT),
                token(NUMBER, 3),
                token(COMMA),
                token(IDENTIFIER, "i"),
                token(EOL),
                token(DIRECTIVE, DirectiveType.REPEAT),
                token(NUMBER, 2),
                token(COMMA),
                token(IDENTIFIER, "j"),
                token(EOL),
                token(INSTRUCTION, InstructionType.CLC),
                token(EOL),
                token(DIRECTIVE, DirectiveType.REPEAT_END),
                token(EOL),
                token(DIRECTIVE, DirectiveType.REPEAT_END),
                token(EOL)
        );

        DirectiveStatement expectedResult = DirectiveStatement.builder()
                .type(DirectiveType.REPEAT)
                .expression(new PrimaryExpression(NUMBER, 3))
                .argument("i")
                .statement(DirectiveStatement.builder()
                        .type(DirectiveType.REPEAT)
                        .expression(new PrimaryExpression(NUMBER, 2))
                        .argument("j")
                        .statement(InstructionStatement.builder()
                                .instructionType(InstructionType.CLC)
                                .addressingModeType(AddressingModeType.IMPLICIT)
                                .build())
                        .build())
                .build();
        assertEquals(expectedResult, result);
    }
}