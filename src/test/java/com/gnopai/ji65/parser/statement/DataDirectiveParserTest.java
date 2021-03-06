package com.gnopai.ji65.parser.statement;

import com.gnopai.ji65.DirectiveType;
import com.gnopai.ji65.parser.ParseException;
import com.gnopai.ji65.parser.expression.PrimaryExpression;
import com.gnopai.ji65.util.ErrorHandler;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static com.gnopai.ji65.parser.ParserTestUtil.parse;
import static com.gnopai.ji65.parser.ParserTestUtil.token;
import static com.gnopai.ji65.scanner.TokenType.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

class DataDirectiveParserTest {
    private final ErrorHandler errorHandler = mock(ErrorHandler.class);

    static Stream<DirectiveType> typeProvider() {
        return Stream.of(DirectiveType.BYTE, DirectiveType.WORD);
    }

    @ParameterizedTest
    @MethodSource("typeProvider")
    void testSingleExpression(DirectiveType directiveType) {
        Statement result = parse(errorHandler,
                token(DIRECTIVE, directiveType),
                token(NUMBER, 32),
                token(EOL)
        );

        DirectiveStatement expectedResult = DirectiveStatement.builder()
                .type(directiveType)
                .expressions(List.of(new PrimaryExpression(NUMBER, 32)))
                .build();
        assertEquals(expectedResult, result);
    }

    @ParameterizedTest
    @MethodSource("typeProvider")
    void testString(DirectiveType directiveType) {
        Statement result = parse(errorHandler,
                token(DIRECTIVE, directiveType),
                token(CHAR, (int) 'W'),
                token(CHAR, (int) 'h'),
                token(CHAR, (int) 'e'),
                token(CHAR, (int) 'e'),
                token(EOL)
        );

        DirectiveStatement expectedResult = DirectiveStatement.builder()
                .type(directiveType)
                .expressions(List.of(
                        new PrimaryExpression(CHAR, 0x57),
                        new PrimaryExpression(CHAR, 0x68),
                        new PrimaryExpression(CHAR, 0x65),
                        new PrimaryExpression(CHAR, 0x65)
                ))
                .build();
        assertEquals(expectedResult, result);
    }

    @ParameterizedTest
    @MethodSource("typeProvider")
    void testMultipleExpressions(DirectiveType directiveType) {
        Statement result = parse(errorHandler,
                token(DIRECTIVE, directiveType),
                token(NUMBER, 32),
                token(COMMA),
                token(NUMBER, 64),
                token(COMMA),
                token(NUMBER, 96),
                token(EOL)
        );

        DirectiveStatement expectedResult = DirectiveStatement.builder()
                .type(directiveType)
                .expressions(List.of(
                        new PrimaryExpression(NUMBER, 32),
                        new PrimaryExpression(NUMBER, 64),
                        new PrimaryExpression(NUMBER, 96)
                ))
                .build();
        assertEquals(expectedResult, result);
    }

    @ParameterizedTest
    @MethodSource("typeProvider")
    void testMultipleExpressionsIncludingString(DirectiveType directiveType) {
        Statement result = parse(errorHandler,
                token(DIRECTIVE, directiveType),
                token(NUMBER, 32),
                token(COMMA),
                token(NUMBER, 64),
                token(COMMA),
                token(CHAR, (int) 'A'),
                token(CHAR, (int) 'b'),
                token(CHAR, (int) 'c'),
                token(COMMA),
                token(NUMBER, 96),
                token(EOL)
        );

        DirectiveStatement expectedResult = DirectiveStatement.builder()
                .type(directiveType)
                .expressions(List.of(
                        new PrimaryExpression(NUMBER, 32),
                        new PrimaryExpression(NUMBER, 64),
                        new PrimaryExpression(CHAR, 0x41),
                        new PrimaryExpression(CHAR, 0x62),
                        new PrimaryExpression(CHAR, 0x63),
                        new PrimaryExpression(NUMBER, 96)
                ))
                .build();
        assertEquals(expectedResult, result);
    }

    @ParameterizedTest
    @MethodSource("typeProvider")
    void testNoExpressions(DirectiveType directiveType) {
        ParseException exception = assertThrows(ParseException.class, () -> parse(errorHandler,
                token(DIRECTIVE, directiveType),
                token(EOL)
        ));
        assertEquals(EOL, exception.getTokenType());
        assertEquals("Failed to parse token", exception.getMessage());
    }

    @ParameterizedTest
    @MethodSource("typeProvider")
    void testDanglingComma(DirectiveType directiveType) {
        ParseException exception = assertThrows(ParseException.class, () -> parse(errorHandler,
                token(DIRECTIVE, directiveType),
                token(NUMBER, 32),
                token(COMMA),
                token(EOL)
        ));
        assertEquals(EOL, exception.getTokenType());
        assertEquals("Failed to parse token", exception.getMessage());
    }

    @ParameterizedTest
    @MethodSource("typeProvider")
    void testMissingEndOfLine(DirectiveType directiveType) {
        ParseException exception = assertThrows(ParseException.class, () -> parse(errorHandler,
                token(DIRECTIVE, directiveType),
                token(NUMBER, 32),
                token(INSTRUCTION)
        ));
        assertEquals(INSTRUCTION, exception.getTokenType());
        assertEquals("Failed to parse token", exception.getMessage());
    }
}