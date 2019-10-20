package com.gnopai.ji65.parser.statement;

import com.gnopai.ji65.AddressingModeType;
import com.gnopai.ji65.DirectiveType;
import com.gnopai.ji65.InstructionType;
import com.gnopai.ji65.parser.ParseException;
import com.gnopai.ji65.util.ErrorHandler;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.gnopai.ji65.parser.ParserTestUtil.parse;
import static com.gnopai.ji65.parser.ParserTestUtil.token;
import static com.gnopai.ji65.scanner.TokenType.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

class MacroDirectiveParserTest {
    private final ErrorHandler errorHandler = mock(ErrorHandler.class);

    @Test
    void testSingleStatement_noArguments() {
        Statement result = parse(errorHandler,
                token(DIRECTIVE, DirectiveType.MACRO),
                token(IDENTIFIER, "Whee"),
                token(EOL),
                token(INSTRUCTION, InstructionType.CLC),
                token(EOL),
                token(DIRECTIVE, DirectiveType.MACRO_END),
                token(EOL)
        );

        DirectiveStatement expectedResult = DirectiveStatement.builder()
                .type(DirectiveType.MACRO)
                .name("Whee")
                .statement(InstructionStatement.builder()
                        .instructionType(InstructionType.CLC)
                        .addressingModeType(AddressingModeType.IMPLICIT)
                        .build())
                .build();
        assertEquals(expectedResult, result);
    }

    @Test
    void testSingleStatement_singleArgument() {
        Statement result = parse(errorHandler,
                token(DIRECTIVE, DirectiveType.MACRO),
                token(IDENTIFIER, "Whee"),
                token(IDENTIFIER, "a"),
                token(EOL),
                token(INSTRUCTION, InstructionType.CLC),
                token(EOL),
                token(DIRECTIVE, DirectiveType.MACRO_END),
                token(EOL)
        );

        DirectiveStatement expectedResult = DirectiveStatement.builder()
                .type(DirectiveType.MACRO)
                .name("Whee")
                .statement(InstructionStatement.builder()
                        .instructionType(InstructionType.CLC)
                        .addressingModeType(AddressingModeType.IMPLICIT)
                        .build())
                .arguments(List.of("a"))
                .build();
        assertEquals(expectedResult, result);
    }

    @Test
    void testSingleStatement_multipleArguments() {
        Statement result = parse(errorHandler,
                token(DIRECTIVE, DirectiveType.MACRO),
                token(IDENTIFIER, "Whee"),
                token(IDENTIFIER, "a"),
                token(COMMA),
                token(IDENTIFIER, "b"),
                token(COMMA),
                token(IDENTIFIER, "c"),
                token(EOL),
                token(INSTRUCTION, InstructionType.CLC),
                token(EOL),
                token(DIRECTIVE, DirectiveType.MACRO_END),
                token(EOL)
        );

        DirectiveStatement expectedResult = DirectiveStatement.builder()
                .type(DirectiveType.MACRO)
                .name("Whee")
                .statement(InstructionStatement.builder()
                        .instructionType(InstructionType.CLC)
                        .addressingModeType(AddressingModeType.IMPLICIT)
                        .build())
                .arguments(List.of("a", "b", "c"))
                .build();
        assertEquals(expectedResult, result);
    }

    @Test
    void testMultipleStatements_singleArgument() {
        Statement result = parse(errorHandler,
                token(DIRECTIVE, DirectiveType.MACRO),
                token(IDENTIFIER, "Whee"),
                token(IDENTIFIER, "a"),
                token(EOL),
                token(INSTRUCTION, InstructionType.CLC),
                token(EOL),
                token(INSTRUCTION, InstructionType.CLV),
                token(EOL),
                token(DIRECTIVE, DirectiveType.MACRO_END),
                token(EOL)
        );

        DirectiveStatement expectedResult = DirectiveStatement.builder()
                .type(DirectiveType.MACRO)
                .name("Whee")
                .statement(InstructionStatement.builder()
                        .instructionType(InstructionType.CLC)
                        .addressingModeType(AddressingModeType.IMPLICIT)
                        .build())
                .statement(InstructionStatement.builder()
                        .instructionType(InstructionType.CLV)
                        .addressingModeType(AddressingModeType.IMPLICIT)
                        .build())
                .arguments(List.of("a"))
                .build();
        assertEquals(expectedResult, result);
    }

    @Test
    void testSingleStatement_missingName() {
        ParseException parseException = assertThrows(ParseException.class, () -> parse(errorHandler,
                token(DIRECTIVE, DirectiveType.MACRO),
                token(EOL),
                token(INSTRUCTION, InstructionType.CLC),
                token(EOL),
                token(DIRECTIVE, DirectiveType.MACRO_END),
                token(EOL)
        ));

        assertEquals(token(EOL), parseException.getToken());
        assertEquals("Expected macro identifier", parseException.getMessage());
    }

    @Test
    void testSingleStatement_missingArgument() {
        ParseException parseException = assertThrows(ParseException.class, () -> parse(errorHandler,
                token(DIRECTIVE, DirectiveType.MACRO),
                token(IDENTIFIER, "Whee"),
                token(IDENTIFIER, "a"),
                token(COMMA),
                token(EOL),
                token(INSTRUCTION, InstructionType.CLC),
                token(EOL),
                token(DIRECTIVE, DirectiveType.MACRO_END),
                token(EOL)
        ));

        assertEquals(token(EOL), parseException.getToken());
        assertEquals("Expected macro argument", parseException.getMessage());
    }

    @Test
    void testSingleStatement_missingEndOfLineAfterArguments() {
        ParseException parseException = assertThrows(ParseException.class, () -> parse(errorHandler,
                token(DIRECTIVE, DirectiveType.MACRO),
                token(IDENTIFIER, "Whee"),
                token(IDENTIFIER, "a"),
                token(INSTRUCTION, InstructionType.CLC),
                token(EOL),
                token(DIRECTIVE, DirectiveType.MACRO_END),
                token(EOL)
        ));

        assertEquals(token(INSTRUCTION, InstructionType.CLC), parseException.getToken());
        assertEquals("Expected end of line", parseException.getMessage());
    }

    @Test
    void testSingleStatement_missingEndOfLineAfterMacroEnd() {
        ParseException parseException = assertThrows(ParseException.class, () -> parse(errorHandler,
                token(DIRECTIVE, DirectiveType.MACRO),
                token(IDENTIFIER, "Whee"),
                token(IDENTIFIER, "a"),
                token(EOL),
                token(INSTRUCTION, InstructionType.CLC),
                token(EOL),
                token(DIRECTIVE, DirectiveType.MACRO_END),
                token(PLUS),
                token(EOF)
        ));

        assertEquals(token(PLUS), parseException.getToken());
        assertEquals("Expected end of line", parseException.getMessage());
    }

    @Test
    void testSingleStatement_missingMacroEnd() {
        ParseException parseException = assertThrows(ParseException.class, () -> parse(errorHandler,
                token(DIRECTIVE, DirectiveType.MACRO),
                token(IDENTIFIER, "Whee"),
                token(IDENTIFIER, "a"),
                token(EOL),
                token(INSTRUCTION, InstructionType.CLC),
                token(EOL),
                token(EOF)
        ));

        // TODO better handling of missing end token
        assertEquals(token(EOF), parseException.getToken());
        assertEquals("Failed to parse token", parseException.getMessage());
    }
}