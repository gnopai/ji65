package com.gnopai.ji65.compiler;

import com.gnopai.ji65.parser.expression.Expression;
import com.gnopai.ji65.parser.expression.PrimaryExpression;
import com.gnopai.ji65.parser.statement.AssignmentStatement;
import com.gnopai.ji65.parser.statement.LabelStatement;
import com.gnopai.ji65.parser.statement.Statement;
import com.gnopai.ji65.scanner.TokenType;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class FirstPassResolverTest {

    @Test
    void testAssignmentStatement() {
        PrimaryExpression expression = new PrimaryExpression(TokenType.NUMBER, 55);

        AssignmentStatement statement = new AssignmentStatement("derp", expression);

        FirstPassResolver testClass = new FirstPassResolver();

        Environment<Expression> environment = testClass.resolve(List.of(statement));

        assertEquals(Optional.of(expression), environment.get("derp"));
    }

    @Test
    void testLabelStatement() {
        LabelStatement statement = new LabelStatement("derp");

        FirstPassResolver testClass = new FirstPassResolver();

        Environment<Expression> environment = testClass.resolve(List.of(statement));

        Label expectedLabel = new Label("derp", false);
        assertEquals(Optional.of(expectedLabel), environment.get("derp"));
    }

    @Test
    void testResolve() {
        Statement statement1 = mock(Statement.class);
        Statement statement2 = mock(Statement.class);
        Statement statement3 = mock(Statement.class);

        FirstPassResolver testClass = new FirstPassResolver();

        Environment environment = testClass.resolve(List.of(statement1, statement2, statement3));

        assertEquals(new Environment(), environment);
        verify(statement1).accept(testClass);
        verify(statement2).accept(testClass);
        verify(statement3).accept(testClass);
    }
}