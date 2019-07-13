package com.gnopai.ji65.parser.expression;

import com.gnopai.ji65.assembler.Environment;
import com.gnopai.ji65.assembler.Label;
import com.gnopai.ji65.scanner.TokenType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static com.gnopai.ji65.scanner.TokenType.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class ExpressionEvaluatorTest {
    private Environment environment;

    @BeforeEach
    void setUp() {
        environment = new Environment();
    }

    @ParameterizedTest
    @MethodSource("prefixExpressionProvider")
    void testPrefixExpression(TokenType tokenType, int value, int expected) {
        Expression rightSideExpression = new PrimaryExpression(NUMBER, value);
        PrefixExpression prefixExpression = new PrefixExpression(tokenType, rightSideExpression);

        int result = new ExpressionEvaluator().evaluate(prefixExpression, environment);
        assertEquals(expected, result);
    }

    static Stream<Arguments> prefixExpressionProvider() {
        return Stream.of(
                arguments(MINUS, 17, -17),
                arguments(LESS_THAN, 0x5432, 0x32),
                arguments(GREATER_THAN, 0x5432, 0x54),
                arguments(TILDE, 0b01101100, 0b1111111110010011),
                arguments(BANG, 5, 0),
                arguments(BANG, 0, 1)
        );
    }

    @Test
    void testUnsupportedPrefixOperator() {
        Expression rightSideExpression = new PrimaryExpression(NUMBER, 17);
        PrefixExpression prefixExpression = new PrefixExpression(COMMA, rightSideExpression);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> new ExpressionEvaluator().evaluate(prefixExpression, environment));
        assertEquals("Unsupported prefix expression: " + COMMA.name(), exception.getMessage());
    }

    @ParameterizedTest
    @MethodSource("binaryExpressionProvider")
    void testBinaryExpression(int leftValue, TokenType tokenType, int rightValue, int expected) {
        Expression leftSideExpression = new PrimaryExpression(NUMBER, leftValue);
        Expression rightSideExpression = new PrimaryExpression(NUMBER, rightValue);
        BinaryOperatorExpression binaryExpression = new BinaryOperatorExpression(leftSideExpression, tokenType, rightSideExpression);

        int result = new ExpressionEvaluator().evaluate(binaryExpression, environment);
        assertEquals(expected, result);
    }

    static Stream<Arguments> binaryExpressionProvider() {
        return Stream.of(
                arguments(7, PLUS, 8, 15),
                arguments(8, MINUS, 2, 6),
                arguments(0xF0, PIPE, 0x0F, 0xFF),
                arguments(0b01110111, AMPERSAND, 0b11011101, 0b01010101),
                arguments(0b11110000, CARET, 0b01010101, 0b10100101),
                arguments(0b00011100, SHIFT_LEFT, 3, 0b11100000),
                arguments(0b01110000, SHIFT_RIGHT, 2, 0b00011100),
                arguments(7, STAR, 8, 56),
                arguments(24, SLASH, 3, 8),
                arguments(4, EQUAL, 5, 0),
                arguments(4, EQUAL, 4, 1),
                arguments(4, NOT_EQUAL, 5, 1),
                arguments(4, NOT_EQUAL, 4, 0),
                arguments(6, GREATER_THAN, 5, 1),
                arguments(6, GREATER_THAN, 6, 0),
                arguments(6, GREATER_THAN, 7, 0),
                arguments(6, GREATER_OR_EQUAL_THAN, 5, 1),
                arguments(6, GREATER_OR_EQUAL_THAN, 6, 1),
                arguments(6, GREATER_OR_EQUAL_THAN, 7, 0),
                arguments(6, LESS_THAN, 7, 1),
                arguments(6, LESS_THAN, 6, 0),
                arguments(6, LESS_THAN, 5, 0),
                arguments(6, LESS_OR_EQUAL_THAN, 7, 1),
                arguments(6, LESS_OR_EQUAL_THAN, 6, 1),
                arguments(6, LESS_OR_EQUAL_THAN, 5, 0),
                arguments(1, AND, 1, 1),
                arguments(1, AND, 0, 0),
                arguments(0, AND, 0, 0),
                arguments(1, OR, 1, 1),
                arguments(1, OR, 0, 1),
                arguments(0, OR, 0, 0)
        );
    }

    @Test
    void testShortCircuit_booleanAnd() {
        Expression leftSideExpression = new PrimaryExpression(NUMBER, 0);
        Expression rightSideExpression = null;
        BinaryOperatorExpression binaryExpression = new BinaryOperatorExpression(leftSideExpression, AND, rightSideExpression);

        int result = new ExpressionEvaluator().evaluate(binaryExpression, environment);
        assertEquals(0, result);
    }

    @Test
    void testShortCircuit_booleanOr() {
        Expression leftSideExpression = new PrimaryExpression(NUMBER, 1);
        Expression rightSideExpression = null;
        BinaryOperatorExpression binaryExpression = new BinaryOperatorExpression(leftSideExpression, OR, rightSideExpression);

        int result = new ExpressionEvaluator().evaluate(binaryExpression, environment);
        assertEquals(1, result);
    }

    @Test
    void testSimpleConstantExpression() {
        String constant = "derp";
        environment.define(constant, 6);
        IdentifierExpression identifierExpression = new IdentifierExpression(constant);

        int result = new ExpressionEvaluator().evaluate(identifierExpression, environment);
        assertEquals(6, result);
    }

    @Test
    void testConstantExpressionInsideMoreComplexExpression() {
        environment.define("one", 24);
        environment.define("two", 8);
        BinaryOperatorExpression expression = new BinaryOperatorExpression(
                new IdentifierExpression("one"),
                SLASH,
                new IdentifierExpression("two")
        );

        int result = new ExpressionEvaluator().evaluate(expression, environment);
        assertEquals(3, result);
    }

    @Test
    void testUnsupportedBinaryOperator() {
        Expression leftSideExpression = new PrimaryExpression(NUMBER, 24);
        Expression rightSideExpression = new PrimaryExpression(NUMBER, 3);
        BinaryOperatorExpression binaryExpression = new BinaryOperatorExpression(leftSideExpression, POUND, rightSideExpression);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> new ExpressionEvaluator().evaluate(binaryExpression, environment));
        assertEquals("Unsupported binary expression: " + POUND.name(), exception.getMessage());
    }

    @Test
    void testUndefinedConstantReferenced() {
        IdentifierExpression identifierExpression = new IdentifierExpression("nope");

        RuntimeException exception = assertThrows(RuntimeException.class, () -> new ExpressionEvaluator().evaluate(identifierExpression, environment));
        assertEquals("Unknown identifier referenced \"nope\"", exception.getMessage());
    }

    @Test
    void testLabel() {
        Label label = new Label("derp");
        environment.define("derp", new PrimaryExpression(NUMBER, 17));

        int result = new ExpressionEvaluator().evaluate(label, environment);
        assertEquals(17, result);
    }

    @Test
    void testUnresolvedLabel() {
        Label label = new Label("derp");
        environment.define("derp", label);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> new ExpressionEvaluator().evaluate(label, environment));
        assertEquals("Unresolved label encountered: \"derp\"", exception.getMessage());
    }

    @Test
    void testRelativeUnnamedLabelExpression() {
        environment.setCurrentAddress(0x4563);
        environment.defineUnnamedLabel(0x4568);
        environment.defineUnnamedLabel(0x4575);
        environment.defineUnnamedLabel(0x4699);
        RelativeUnnamedLabelExpression expression = new RelativeUnnamedLabelExpression(2);

        int result = new ExpressionEvaluator().evaluate(expression, environment);
        assertEquals(0x4575, result);
    }
}