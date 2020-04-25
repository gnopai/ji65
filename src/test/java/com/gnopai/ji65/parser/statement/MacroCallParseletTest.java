package com.gnopai.ji65.parser.statement;

import com.gnopai.ji65.InstructionType;
import com.gnopai.ji65.SourceFileProcessor;
import com.gnopai.ji65.assembler.Macro;
import com.gnopai.ji65.parser.ParseException;
import com.gnopai.ji65.scanner.Token;
import com.gnopai.ji65.util.ErrorHandler;
import lombok.Builder;
import lombok.Value;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static com.gnopai.ji65.parser.ParserTestUtil.parse;
import static com.gnopai.ji65.parser.ParserTestUtil.token;
import static com.gnopai.ji65.scanner.TokenType.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class MacroCallParseletTest {
    private final ErrorHandler errorHandler = mock(ErrorHandler.class);
    private final SourceFileProcessor sourceFileProcessor = mock(SourceFileProcessor.class);

    private final String macroName = "Whee";

    @Test
    void testMacroWithNoArguments() {
        testMacro(TestCase.builder()
                .inputTokens(List.of(
                        token(IDENTIFIER, macroName),
                        token(EOL)
                )).macroTokens(List.of(
                        token(INSTRUCTION, InstructionType.SEC),
                        token(EOL)
                )).expectedTokens(List.of(
                        token(INSTRUCTION, InstructionType.SEC),
                        token(EOL)
                ))
                .build()
        );
    }

    @Test
    void testMacroWithSingleArgument() {
        testMacro(TestCase.builder()
                .inputTokens(List.of(
                        token(IDENTIFIER, macroName),
                        token(PLUS),
                        token(NUMBER, 1),
                        token(EOL)
                )).macroTokens(List.of(
                        token(INSTRUCTION, InstructionType.JMP),
                        token(IDENTIFIER, "arg1"),
                        token(EOL)
                )).expectedTokens(List.of(
                        token(INSTRUCTION, InstructionType.JMP),
                        token(PLUS),
                        token(NUMBER, 1),
                        token(EOL)
                ))
                .build()
        );
    }

    @Test
    void testMacroWithSingleArgument_withBraces() {
        testMacro(TestCase.builder()
                .inputTokens(List.of(
                        token(IDENTIFIER, "Whee"),
                        token(LEFT_BRACE),
                        token(IDENTIFIER, "abc"),
                        token(RIGHT_BRACE),
                        token(EOL)
                )).macroTokens(List.of(
                        token(INSTRUCTION, InstructionType.LDA),
                        token(IDENTIFIER, "arg1"),
                        token(EOL)
                )).expectedTokens(List.of(
                        token(INSTRUCTION, InstructionType.LDA),
                        token(IDENTIFIER, "abc"),
                        token(EOL)
                ))
                .build()
        );
    }

    @Test
    void testMacroWithSingleArgument_withBracesButMissingEndBrace() {
        Macro macro = new Macro("Whee", List.of(
                token(INSTRUCTION, InstructionType.LDA),
                token(IDENTIFIER, "arg1"),
                token(EOL)
        ), List.of("arg1"));
        when(sourceFileProcessor.getMacro("Whee")).thenReturn(Optional.of(macro));

        ParseException parseException = assertThrows(ParseException.class, () -> parse(errorHandler, sourceFileProcessor,
                token(IDENTIFIER, "Whee"),
                token(LEFT_BRACE),
                token(IDENTIFIER, "abc"),
                token(EOL)
        ));

        assertEquals(token(EOL), parseException.getToken());
        assertEquals("Expected end brace at end of macro argument", parseException.getMessage());
    }

    @Test
    void testMacroWithMultipleArguments() {
        testMacro(TestCase.builder()
                .inputTokens(List.of(
                        token(IDENTIFIER, "Whee"),
                        token(IDENTIFIER, "abc"),
                        token(COMMA),
                        token(NUMBER, 5),
                        token(PLUS),
                        token(NUMBER, 3),
                        token(COMMA),
                        token(IDENTIFIER, "fooo"),
                        token(EOL)
                )).macroTokens(List.of(
                        token(INSTRUCTION, InstructionType.LDA),
                        token(IDENTIFIER, "arg1"),
                        token(EOL),
                        token(INSTRUCTION, InstructionType.LDX),
                        token(IDENTIFIER, "arg2"),
                        token(EOL),
                        token(INSTRUCTION, InstructionType.LDY),
                        token(IDENTIFIER, "arg3"),
                        token(EOL)
                )).expectedTokens(List.of(
                        token(INSTRUCTION, InstructionType.LDA),
                        token(IDENTIFIER, "abc"),
                        token(EOL),
                        token(INSTRUCTION, InstructionType.LDX),
                        token(NUMBER, 5),
                        token(PLUS),
                        token(NUMBER, 3),
                        token(EOL),
                        token(INSTRUCTION, InstructionType.LDY),
                        token(IDENTIFIER, "fooo"),
                        token(EOL)
                ))
                .build()
        );
    }

    @Test
    void testMacroWithMultipleArguments_someArgumentsNotPassedIn() {
        testMacro(TestCase.builder()
                .inputTokens(List.of(
                        token(IDENTIFIER, "Whee"),
                        token(COMMA),
                        token(COMMA),
                        token(IDENTIFIER, "fooo"),
                        token(EOL)
                )).macroTokens(List.of(
                        token(INSTRUCTION, InstructionType.DEC),
                        token(IDENTIFIER, "arg1"),
                        token(EOL),
                        token(INSTRUCTION, InstructionType.INC),
                        token(IDENTIFIER, "arg2"),
                        token(EOL),
                        token(INSTRUCTION, InstructionType.LDA),
                        token(IDENTIFIER, "arg3"),
                        token(EOL)
                )).expectedTokens(List.of(
                        token(INSTRUCTION, InstructionType.DEC),
                        token(EOL),
                        token(INSTRUCTION, InstructionType.INC),
                        token(EOL),
                        token(INSTRUCTION, InstructionType.LDA),
                        token(IDENTIFIER, "fooo"),
                        token(EOL)
                ))
                .build()
        );
    }

    @Test
    void testMacroWithMultipleArguments_multipleLines() {
        testMacro(TestCase.builder()
                .inputTokens(List.of(
                        token(IDENTIFIER, "Whee"),
                        token(COMMA),
                        token(EOL),
                        token(COMMA),
                        token(EOL),
                        token(IDENTIFIER, "fooo"),
                        token(EOL)
                )).macroTokens(List.of(
                        token(INSTRUCTION, InstructionType.DEC),
                        token(IDENTIFIER, "arg1"),
                        token(EOL),
                        token(INSTRUCTION, InstructionType.INC),
                        token(IDENTIFIER, "arg2"),
                        token(EOL),
                        token(INSTRUCTION, InstructionType.LDA),
                        token(IDENTIFIER, "arg3"),
                        token(EOL)
                )).expectedTokens(List.of(
                        token(INSTRUCTION, InstructionType.DEC),
                        token(EOL),
                        token(INSTRUCTION, InstructionType.INC),
                        token(EOL),
                        token(INSTRUCTION, InstructionType.LDA),
                        token(IDENTIFIER, "fooo"),
                        token(EOL)
                ))
                .build()
        );
    }

    @Test
    void testMacroWithMultipleArguments_endOfFile() {
        testMacro(TestCase.builder()
                .inputTokens(List.of(
                        token(IDENTIFIER, "Whee"),
                        token(IDENTIFIER, "abc"),
                        token(COMMA),
                        token(NUMBER, 5),
                        token(PLUS),
                        token(NUMBER, 3),
                        token(COMMA),
                        token(IDENTIFIER, "fooo"),
                        token(EOF)
                )).macroTokens(List.of(
                        token(INSTRUCTION, InstructionType.LDA),
                        token(IDENTIFIER, "arg1"),
                        token(EOL),
                        token(INSTRUCTION, InstructionType.LDX),
                        token(IDENTIFIER, "arg2"),
                        token(EOL),
                        token(INSTRUCTION, InstructionType.LDY),
                        token(IDENTIFIER, "arg3"),
                        token(EOL)
                )).expectedTokens(List.of(
                        token(INSTRUCTION, InstructionType.LDA),
                        token(IDENTIFIER, "abc"),
                        token(EOL),
                        token(INSTRUCTION, InstructionType.LDX),
                        token(NUMBER, 5),
                        token(PLUS),
                        token(NUMBER, 3),
                        token(EOL),
                        token(INSTRUCTION, InstructionType.LDY),
                        token(IDENTIFIER, "fooo"),
                        token(EOL)
                ))
                .build()
        );
    }

    @Test
    void testMacroWithMultipleArguments_bracesAndEmbeddedCommas() {
        // whee {$5,X}, abc, {99,Y}
        testMacro(TestCase.builder()
                .inputTokens(List.of(
                        token(IDENTIFIER, macroName),
                        token(LEFT_BRACE),
                        token(NUMBER, 5),
                        token(COMMA),
                        token(X),
                        token(RIGHT_BRACE),
                        token(COMMA),
                        token(IDENTIFIER, "abc"),
                        token(COMMA),
                        token(LEFT_BRACE),
                        token(NUMBER, 99),
                        token(COMMA),
                        token(Y),
                        token(RIGHT_BRACE),
                        token(EOL)
                )).macroTokens(List.of(
                        token(INSTRUCTION, InstructionType.LDA),
                        token(IDENTIFIER, "arg1"),
                        token(EOL),
                        token(INSTRUCTION, InstructionType.LDX),
                        token(IDENTIFIER, "arg2"),
                        token(EOL),
                        token(INSTRUCTION, InstructionType.LDY),
                        token(IDENTIFIER, "arg3"),
                        token(EOL)
                )).expectedTokens(List.of(
                        token(INSTRUCTION, InstructionType.LDA),
                        token(NUMBER, 5),
                        token(COMMA),
                        token(X),
                        token(EOL),
                        token(INSTRUCTION, InstructionType.LDX),
                        token(IDENTIFIER, "abc"),
                        token(EOL),
                        token(INSTRUCTION, InstructionType.LDY),
                        token(NUMBER, 99),
                        token(COMMA),
                        token(Y),
                        token(EOL)
                ))
                .build()
        );
    }

    @Test
    void testMacroWithSingleArgumentButMoreArgumentsPassedIn() {
        testMacro(TestCase.builder()
                .inputTokens(List.of(
                        token(IDENTIFIER, macroName),
                        token(IDENTIFIER, "une"),
                        token(COMMA),
                        token(IDENTIFIER, "deux"),
                        token(COMMA),
                        token(IDENTIFIER, "trois"),
                        token(EOL)
                )).macroTokens(List.of(
                        token(INSTRUCTION, InstructionType.JMP),
                        token(IDENTIFIER, "arg1"),
                        token(EOL)
                )).expectedTokens(List.of(
                        token(INSTRUCTION, InstructionType.JMP),
                        token(IDENTIFIER, "une"),
                        token(EOL)
                ))
                .build()
        );
    }

    @Test
    void testMacroWithMultipleArguments_danglingComma() {
        testMacro(TestCase.builder()
                .inputTokens(List.of(
                        token(IDENTIFIER, macroName),
                        token(COMMA),
                        token(COMMA),
                        token(EOL),
                        token(EOF)
                )).macroTokens(List.of(
                        token(INSTRUCTION, InstructionType.LDA),
                        token(IDENTIFIER, "arg1"),
                        token(EOL),
                        token(INSTRUCTION, InstructionType.LDX),
                        token(IDENTIFIER, "arg2"),
                        token(EOL),
                        token(INSTRUCTION, InstructionType.LDY),
                        token(IDENTIFIER, "arg3"),
                        token(EOL)
                )).expectedTokens(List.of(
                        token(INSTRUCTION, InstructionType.LDA),
                        token(EOL),
                        token(INSTRUCTION, InstructionType.LDX),
                        token(EOL),
                        token(INSTRUCTION, InstructionType.LDY),
                        token(EOL)
                ))
                .build()
        );
    }

    @Test
    void testMacroDoesNotExist() {
        when(sourceFileProcessor.getMacro("nope")).thenReturn(Optional.empty());

        ParseException parseException = assertThrows(ParseException.class, () -> parse(errorHandler, sourceFileProcessor,
                token(IDENTIFIER, "nope"),
                token(IDENTIFIER, "abc"),
                token(EOL)
        ));

        assertEquals(token(IDENTIFIER, "nope"), parseException.getToken());
        assertEquals("Unknown macro referenced: nope", parseException.getMessage());
    }

    private void testMacro(TestCase testCase) {
        Macro macro = new Macro(macroName, testCase.getMacroTokens(), List.of("arg1", "arg2", "arg3"));
        when(sourceFileProcessor.getMacro(macroName)).thenReturn(Optional.of(macro));

        Statement statement = mock(Statement.class);
        when(sourceFileProcessor.parseTokensFromCurrentFile(testCase.getExpectedTokens())).thenReturn(List.of(statement));

        Statement result = parse(errorHandler, sourceFileProcessor, testCase.getInputTokens());

        MultiStatement expectedResult = new MultiStatement(List.of(statement));
        assertEquals(expectedResult, result);
    }

    @Value
    @Builder
    static class TestCase {
        List<Token> inputTokens;
        List<Token> macroTokens;
        List<Token> expectedTokens;
    }
}