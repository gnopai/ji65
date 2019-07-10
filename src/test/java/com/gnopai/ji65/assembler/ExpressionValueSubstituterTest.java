package com.gnopai.ji65.assembler;

import com.gnopai.ji65.parser.expression.*;
import com.gnopai.ji65.scanner.TokenType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ExpressionValueSubstituterTest {
    private final String name = "valueName";
    private final int value = 99;
    private final Environment environment = new Environment();
    private final Expression substitutedExpression = new PrimaryExpression(TokenType.NUMBER, value);

    @Test
    void testPrefixExpression_noMatch() {
        PrefixExpression expression = new PrefixExpression(TokenType.MINUS, identifier("nope"));
        Expression result = testSubstitution(expression);
        Expression expectedResult = new PrefixExpression(TokenType.MINUS, identifier("nope"));
        assertEquals(expectedResult, result);
    }

    @Test
    void testPrefixExpression_match() {
        PrefixExpression expression = new PrefixExpression(TokenType.MINUS, identifier(name));
        Expression result = testSubstitution(expression);
        Expression expectedResult = new PrefixExpression(TokenType.MINUS, substitutedExpression);
        assertEquals(expectedResult, result);
    }

    @Test
    void testBinaryOperatorExpression_leftMatch() {
        BinaryOperatorExpression expression = new BinaryOperatorExpression(identifier(name), TokenType.PLUS, identifier("nope"));
        Expression result = testSubstitution(expression);
        Expression expectedResult = new BinaryOperatorExpression(substitutedExpression, TokenType.PLUS, identifier("nope"));
        assertEquals(expectedResult, result);
    }

    @Test
    void testBinaryOperatorExpression_rightMatch() {
        BinaryOperatorExpression expression = new BinaryOperatorExpression(identifier("nope"), TokenType.PLUS, identifier(name));
        Expression result = testSubstitution(expression);
        Expression expectedResult = new BinaryOperatorExpression(identifier("nope"), TokenType.PLUS, substitutedExpression);
        assertEquals(expectedResult, result);
    }

    @Test
    void testIdentifierExpression_noMatch() {
        IdentifierExpression expression = identifier("nope");
        Expression result = testSubstitution(expression);
        assertEquals(expression, result);
    }

    @Test
    void testIdentifierExpression_match() {
        IdentifierExpression expression = identifier(name);
        Expression result = testSubstitution(expression);
        assertEquals(substitutedExpression, result);
    }

    private IdentifierExpression identifier(String name) {
        return new IdentifierExpression(name);
    }

    private Expression testSubstitution(Expression expression) {
        return new ExpressionValueSubstituter()
                .substituteValue(name, value, expression, environment);
    }
}