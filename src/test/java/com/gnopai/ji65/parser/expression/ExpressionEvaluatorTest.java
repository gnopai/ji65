package com.gnopai.ji65.parser.expression;

import com.gnopai.ji65.compiler.Environment;
import com.gnopai.ji65.scanner.TokenType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ExpressionEvaluatorTest {
    private Environment<Integer> environment;

    @BeforeEach
    void setUp() {
        environment = new Environment<>();
    }

    @Test
    void testUnaryNegation() {
        Expression rightSideExpression = new PrimaryExpression(TokenType.NUMBER, 17);
        PrefixExpression prefixExpression = new PrefixExpression(TokenType.MINUS, rightSideExpression);

        int result = new ExpressionEvaluator().evaluate(prefixExpression, environment);
        assertEquals(-17, result);
    }

    @Test
    void testUnsupportedPrefixOperator() {
        Expression rightSideExpression = new PrimaryExpression(TokenType.NUMBER, 17);
        PrefixExpression prefixExpression = new PrefixExpression(TokenType.COMMA, rightSideExpression);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> new ExpressionEvaluator().evaluate(prefixExpression, environment));
        assertEquals("Unsupported prefix expression: " + TokenType.COMMA.name(), exception.getMessage());
    }

    @Test
    void testBinaryAddition() {
        Expression leftSideExpression = new PrimaryExpression(TokenType.NUMBER, 7);
        Expression rightSideExpression = new PrimaryExpression(TokenType.NUMBER, 8);
        BinaryOperatorExpression binaryExpression = new BinaryOperatorExpression(leftSideExpression, TokenType.PLUS, rightSideExpression);

        int result = new ExpressionEvaluator().evaluate(binaryExpression, environment);
        assertEquals(15, result);
    }

    @Test
    void testBinarySubtraction() {
        Expression leftSideExpression = new PrimaryExpression(TokenType.NUMBER, 8);
        Expression rightSideExpression = new PrimaryExpression(TokenType.NUMBER, 2);
        BinaryOperatorExpression binaryExpression = new BinaryOperatorExpression(leftSideExpression, TokenType.MINUS, rightSideExpression);

        int result = new ExpressionEvaluator().evaluate(binaryExpression, environment);
        assertEquals(6, result);
    }

    @Test
    void testBinaryMultiplication() {
        Expression leftSideExpression = new PrimaryExpression(TokenType.NUMBER, 7);
        Expression rightSideExpression = new PrimaryExpression(TokenType.NUMBER, 8);
        BinaryOperatorExpression binaryExpression = new BinaryOperatorExpression(leftSideExpression, TokenType.STAR, rightSideExpression);

        int result = new ExpressionEvaluator().evaluate(binaryExpression, environment);
        assertEquals(56, result);
    }

    @Test
    void testBinaryDivision() {
        Expression leftSideExpression = new PrimaryExpression(TokenType.NUMBER, 24);
        Expression rightSideExpression = new PrimaryExpression(TokenType.NUMBER, 3);
        BinaryOperatorExpression binaryExpression = new BinaryOperatorExpression(leftSideExpression, TokenType.SLASH, rightSideExpression);

        int result = new ExpressionEvaluator().evaluate(binaryExpression, environment);
        assertEquals(8, result);
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
                TokenType.SLASH,
                new IdentifierExpression("two")
        );

        int result = new ExpressionEvaluator().evaluate(expression, environment);
        assertEquals(3, result);
    }

    @Test
    void testUnsupportedBinaryOperator() {
        Expression leftSideExpression = new PrimaryExpression(TokenType.NUMBER, 24);
        Expression rightSideExpression = new PrimaryExpression(TokenType.NUMBER, 3);
        BinaryOperatorExpression binaryExpression = new BinaryOperatorExpression(leftSideExpression, TokenType.POUND, rightSideExpression);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> new ExpressionEvaluator().evaluate(binaryExpression, environment));
        assertEquals("Unsupported binary expression: " + TokenType.POUND.name(), exception.getMessage());
    }

    @Test
    void testUndefinedConstantReferenced() {
        IdentifierExpression identifierExpression = new IdentifierExpression("nope");

        RuntimeException exception = assertThrows(RuntimeException.class, () -> new ExpressionEvaluator().evaluate(identifierExpression, environment));
        assertEquals("Unknown identifier referenced \"nope\"", exception.getMessage());
    }
}