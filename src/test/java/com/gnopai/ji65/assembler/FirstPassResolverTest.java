package com.gnopai.ji65.assembler;

import com.gnopai.ji65.parser.expression.ExpressionEvaluator;
import com.gnopai.ji65.parser.expression.PrefixExpression;
import com.gnopai.ji65.parser.expression.PrimaryExpression;
import com.gnopai.ji65.parser.statement.AssignmentStatement;
import com.gnopai.ji65.parser.statement.LabelStatement;
import com.gnopai.ji65.parser.statement.Statement;
import com.gnopai.ji65.scanner.TokenType;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class FirstPassResolverTest {
    private final ExpressionEvaluator expressionEvaluator = mock(ExpressionEvaluator.class);

    @Test
    void testAssignmentStatement() {
        Environment environment = new Environment();
        PrefixExpression expression = new PrefixExpression(TokenType.MINUS, new PrimaryExpression(TokenType.NUMBER, 77));
        when(expressionEvaluator.evaluate(expression, environment)).thenReturn(44);

        AssignmentStatement statement = new AssignmentStatement("derp", expression);

        FirstPassResolver testClass = new FirstPassResolver(expressionEvaluator);

        testClass.resolve(List.of(statement), environment);

        assertEquals(Optional.of(new PrimaryExpression(TokenType.NUMBER, 44)), environment.get("derp"));
    }

    @Test
    void testLabelStatement() {
        LabelStatement statement = new LabelStatement("derp");
        Environment environment = new Environment();

        FirstPassResolver testClass = new FirstPassResolver(expressionEvaluator);

        testClass.resolve(List.of(statement), environment);

        Label expectedLabel = new Label("derp", false);
        assertEquals(Optional.of(expectedLabel), environment.get("derp"));
    }

    @Test
    void testResolve() {
        Statement statement1 = mock(Statement.class);
        Statement statement2 = mock(Statement.class);
        Statement statement3 = mock(Statement.class);
        Environment environment = new Environment();

        FirstPassResolver testClass = new FirstPassResolver(expressionEvaluator);

        testClass.resolve(List.of(statement1, statement2, statement3), environment);

        verify(statement1).accept(testClass);
        verify(statement2).accept(testClass);
        verify(statement3).accept(testClass);
    }
}