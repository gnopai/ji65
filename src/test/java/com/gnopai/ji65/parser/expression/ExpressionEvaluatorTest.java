package com.gnopai.ji65.parser.expression;

import com.gnopai.ji65.assembler.Environment;
import com.gnopai.ji65.assembler.Label;
import com.gnopai.ji65.scanner.TokenType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ExpressionEvaluatorTest {
    private Environment environment;

    @BeforeEach
    void setUp() {
        environment = new Environment();
    }

    @Test
    void testUnaryNegation() {
        Expression rightSideExpression = new PrimaryExpression(TokenType.NUMBER, 17);
        PrefixExpression prefixExpression = new PrefixExpression(TokenType.MINUS, rightSideExpression);

        int result = new ExpressionEvaluator().evaluate(prefixExpression, environment);
        assertEquals(-17, result);
    }

    @Test
    void testUnaryLowByte() {
        Expression rightSideExpression = new PrimaryExpression(TokenType.NUMBER, 0x5432);
        PrefixExpression prefixExpression = new PrefixExpression(TokenType.LESS_THAN, rightSideExpression);

        int result = new ExpressionEvaluator().evaluate(prefixExpression, environment);
        assertEquals(0x32, result);
    }

    @Test
    void testUnaryHighByte() {
        Expression rightSideExpression = new PrimaryExpression(TokenType.NUMBER, 0x5432);
        PrefixExpression prefixExpression = new PrefixExpression(TokenType.GREATER_THAN, rightSideExpression);

        int result = new ExpressionEvaluator().evaluate(prefixExpression, environment);
        assertEquals(0x54, result);
    }

    @Test
    void testUnaryNot() {
        Expression rightSideExpression = new PrimaryExpression(TokenType.NUMBER, 0b01101100);
        PrefixExpression prefixExpression = new PrefixExpression(TokenType.TILDE, rightSideExpression);

        int result = new ExpressionEvaluator().evaluate(prefixExpression, environment);
        System.out.printf("%x\n", result);
        assertEquals(0b1111111110010011, result);
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
    void testBinaryBitwiseOr() {
        Expression leftSideExpression = new PrimaryExpression(TokenType.NUMBER, 0xF0);
        Expression rightSideExpression = new PrimaryExpression(TokenType.NUMBER, 0x0F);
        BinaryOperatorExpression binaryExpression = new BinaryOperatorExpression(leftSideExpression, TokenType.PIPE, rightSideExpression);

        int result = new ExpressionEvaluator().evaluate(binaryExpression, environment);
        assertEquals(0xFF, result);
    }

    @Test
    void testBinaryBitwiseAnd() {
        Expression leftSideExpression = new PrimaryExpression(TokenType.NUMBER, 0b01110111);
        Expression rightSideExpression = new PrimaryExpression(TokenType.NUMBER, 0b11011101);
        BinaryOperatorExpression binaryExpression = new BinaryOperatorExpression(leftSideExpression, TokenType.AMPERSAND, rightSideExpression);

        int result = new ExpressionEvaluator().evaluate(binaryExpression, environment);
        assertEquals(0b01010101, result);
    }

    @Test
    void testBinaryBitwiseXor() {
        Expression leftSideExpression = new PrimaryExpression(TokenType.NUMBER, 0b11110000);
        Expression rightSideExpression = new PrimaryExpression(TokenType.NUMBER, 0b01010101);
        BinaryOperatorExpression binaryExpression = new BinaryOperatorExpression(leftSideExpression, TokenType.CARET, rightSideExpression);

        int result = new ExpressionEvaluator().evaluate(binaryExpression, environment);
        assertEquals(0b10100101, result);
    }

    @Test
    void testBinaryShiftLeft() {
        Expression leftSideExpression = new PrimaryExpression(TokenType.NUMBER, 0b00011100);
        Expression rightSideExpression = new PrimaryExpression(TokenType.NUMBER, 3);
        BinaryOperatorExpression binaryExpression = new BinaryOperatorExpression(leftSideExpression, TokenType.SHIFT_LEFT, rightSideExpression);

        int result = new ExpressionEvaluator().evaluate(binaryExpression, environment);
        assertEquals(0b11100000, result);
    }

    @Test
    void testBinaryShiftRight() {
        Expression leftSideExpression = new PrimaryExpression(TokenType.NUMBER, 0b01110000);
        Expression rightSideExpression = new PrimaryExpression(TokenType.NUMBER, 2);
        BinaryOperatorExpression binaryExpression = new BinaryOperatorExpression(leftSideExpression, TokenType.SHIFT_RIGHT, rightSideExpression);

        int result = new ExpressionEvaluator().evaluate(binaryExpression, environment);
        assertEquals(0b00011100, result);
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

    @Test
    void testLabel() {
        Label label = new Label("derp");
        environment.define("derp", new PrimaryExpression(TokenType.NUMBER, 17));

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