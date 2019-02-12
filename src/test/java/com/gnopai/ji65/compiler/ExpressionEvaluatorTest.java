package com.gnopai.ji65.compiler;

import com.gnopai.ji65.parser.expression.BinaryOperatorExpression;
import com.gnopai.ji65.parser.expression.Expression;
import com.gnopai.ji65.parser.expression.PrefixExpression;
import com.gnopai.ji65.parser.expression.PrimaryExpression;
import com.gnopai.ji65.scanner.TokenType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ExpressionEvaluatorTest {

    @Test
    void testUnaryNegation() {
        Expression rightSideExpression = new PrimaryExpression(TokenType.NUMBER, 17);
        PrefixExpression prefixExpression = new PrefixExpression(TokenType.MINUS, rightSideExpression);

        int result = new ExpressionEvaluator().evaluate(prefixExpression);
        assertEquals(-17, result);
    }

    @Test
    void testUnsupportedPrefixOperator() {
        Expression rightSideExpression = new PrimaryExpression(TokenType.NUMBER, 17);
        PrefixExpression prefixExpression = new PrefixExpression(TokenType.COMMA, rightSideExpression);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> new ExpressionEvaluator().evaluate(prefixExpression));
        assertEquals("Unsupported prefix expression: " + TokenType.COMMA.name(), exception.getMessage());
    }

    @Test
    void testBinaryAddition() {
        Expression leftSideExpression = new PrimaryExpression(TokenType.NUMBER, 7);
        Expression rightSideExpression = new PrimaryExpression(TokenType.NUMBER, 8);
        BinaryOperatorExpression binaryExpression = new BinaryOperatorExpression(leftSideExpression, TokenType.PLUS, rightSideExpression);

        int result = new ExpressionEvaluator().evaluate(binaryExpression);
        assertEquals(15, result);
    }

    @Test
    void testBinarySubtraction() {
        Expression leftSideExpression = new PrimaryExpression(TokenType.NUMBER, 8);
        Expression rightSideExpression = new PrimaryExpression(TokenType.NUMBER, 2);
        BinaryOperatorExpression binaryExpression = new BinaryOperatorExpression(leftSideExpression, TokenType.MINUS, rightSideExpression);

        int result = new ExpressionEvaluator().evaluate(binaryExpression);
        assertEquals(6, result);
    }

    @Test
    void testBinaryMultiplication() {
        Expression leftSideExpression = new PrimaryExpression(TokenType.NUMBER, 7);
        Expression rightSideExpression = new PrimaryExpression(TokenType.NUMBER, 8);
        BinaryOperatorExpression binaryExpression = new BinaryOperatorExpression(leftSideExpression, TokenType.STAR, rightSideExpression);

        int result = new ExpressionEvaluator().evaluate(binaryExpression);
        assertEquals(56, result);
    }

    @Test
    void testBinaryDivision() {
        Expression leftSideExpression = new PrimaryExpression(TokenType.NUMBER, 24);
        Expression rightSideExpression = new PrimaryExpression(TokenType.NUMBER, 3);
        BinaryOperatorExpression binaryExpression = new BinaryOperatorExpression(leftSideExpression, TokenType.SLASH, rightSideExpression);

        int result = new ExpressionEvaluator().evaluate(binaryExpression);
        assertEquals(8, result);
    }

    @Test
    void testUnsupportedBinaryOperator() {
        Expression leftSideExpression = new PrimaryExpression(TokenType.NUMBER, 24);
        Expression rightSideExpression = new PrimaryExpression(TokenType.NUMBER, 3);
        BinaryOperatorExpression binaryExpression = new BinaryOperatorExpression(leftSideExpression, TokenType.POUND, rightSideExpression);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> new ExpressionEvaluator().evaluate(binaryExpression));
        assertEquals("Unsupported binary expression: " + TokenType.POUND.name(), exception.getMessage());
    }
}