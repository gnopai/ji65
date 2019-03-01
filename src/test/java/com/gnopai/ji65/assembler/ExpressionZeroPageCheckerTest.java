package com.gnopai.ji65.assembler;

import com.gnopai.ji65.parser.expression.*;
import com.gnopai.ji65.scanner.TokenType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ExpressionZeroPageCheckerTest {

    @Test
    void testPrimaryExpression_zeroPage() {
        PrimaryExpression expression = new PrimaryExpression(TokenType.NUMBER, 255);
        assertTrue(isZeroPage(expression));
    }

    @Test
    void testPrimaryExpression_notZeroPage() {
        PrimaryExpression expression = new PrimaryExpression(TokenType.NUMBER, 256);
        assertFalse(isZeroPage(expression));
    }

    @Test
    void testPrefixExpression_zeroPage() {
        PrefixExpression expression = new PrefixExpression(TokenType.MINUS,
                new PrimaryExpression(TokenType.NUMBER, 7)
        );
        assertTrue(isZeroPage(expression));
    }

    @Test
    void testPrefixExpression_notZeroPage() {
        PrefixExpression expression = new PrefixExpression(TokenType.MINUS,
                new PrimaryExpression(TokenType.NUMBER, 0x4567)
        );
        assertFalse(isZeroPage(expression));
    }

    @Test
    void testBinaryExpression_zeroPage() {
        BinaryOperatorExpression expression = new BinaryOperatorExpression(
                new PrimaryExpression(TokenType.NUMBER, 67),
                TokenType.MINUS,
                new PrimaryExpression(TokenType.NUMBER, 15)
        );
        assertTrue(isZeroPage(expression));
    }

    @Test
    void testBinaryExpression_notZeroPageFromFirstArg() {
        BinaryOperatorExpression expression = new BinaryOperatorExpression(
                new PrimaryExpression(TokenType.NUMBER, 0x167),
                TokenType.MINUS,
                new PrimaryExpression(TokenType.NUMBER, 15)
        );
        assertFalse(isZeroPage(expression));
    }

    @Test
    void testBinaryExpression_notZeroPageFromSecondArg() {
        BinaryOperatorExpression expression = new BinaryOperatorExpression(
                new PrimaryExpression(TokenType.NUMBER, 15),
                TokenType.PLUS,
                new PrimaryExpression(TokenType.NUMBER, 0x167)
        );
        assertFalse(isZeroPage(expression));
    }

    @Test
    void testBinaryExpression_notZeroPageFromBothArgs() {
        BinaryOperatorExpression expression = new BinaryOperatorExpression(
                new PrimaryExpression(TokenType.NUMBER, 0x1115),
                TokenType.PLUS,
                new PrimaryExpression(TokenType.NUMBER, 0x167)
        );
        assertFalse(isZeroPage(expression));
    }

    @Test
    void testIdentifierExpression_zeroPage() {
        Environment environment = new Environment();
        environment.define("derp", new PrimaryExpression(TokenType.NUMBER, 0x0012));
        IdentifierExpression expression = new IdentifierExpression("derp");
        assertTrue(isZeroPage(expression, environment));
    }

    @Test
    void testIdentifierExpression_notZeroPage() {
        Environment environment = new Environment();
        environment.define("derp", new PrimaryExpression(TokenType.NUMBER, 0x0212));
        IdentifierExpression expression = new IdentifierExpression("derp");
        assertFalse(isZeroPage(expression, environment));
    }

    @Test
    void testIdentifierExpression_unknownIdentifier() {
        IdentifierExpression expression = new IdentifierExpression("nope");
        RuntimeException exception = assertThrows(RuntimeException.class, () -> isZeroPage(expression));
        assertEquals("Unknown identifier referenced \"nope\"", exception.getMessage());
    }

    @Test
    void testLabel_zeroPage() {
        Label label = new Label("derp", true);
        assertTrue(isZeroPage(label));
    }

    @Test
    void testLabel_notZeroPage() {
        Label label = new Label("derp", false);
        assertFalse(isZeroPage(label));
    }

    private boolean isZeroPage(Expression expression) {
        return isZeroPage(expression, new Environment());
    }

    private boolean isZeroPage(Expression expression, Environment environment) {
        return new ExpressionZeroPageChecker().isZeroPage(expression, environment);
    }
}