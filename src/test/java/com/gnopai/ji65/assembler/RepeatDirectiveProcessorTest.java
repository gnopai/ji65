package com.gnopai.ji65.assembler;

import com.gnopai.ji65.DirectiveType;
import com.gnopai.ji65.parser.expression.Expression;
import com.gnopai.ji65.parser.expression.ExpressionEvaluator;
import com.gnopai.ji65.parser.expression.PrimaryExpression;
import com.gnopai.ji65.parser.statement.DirectiveStatement;
import com.gnopai.ji65.parser.statement.Statement;
import com.gnopai.ji65.scanner.TokenType;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class RepeatDirectiveProcessorTest {
    private final StatementValueSubstituter statementValueSubstituter = mock(StatementValueSubstituter.class);
    private final ExpressionEvaluator expressionEvaluator = mock(ExpressionEvaluator.class);
    private final Environment environment = new Environment();

    @Test
    void testWithoutArgument() {
        int count = 4;
        Expression countExpression = new PrimaryExpression(TokenType.NUMBER, 4);
        when(expressionEvaluator.evaluate(countExpression, environment)).thenReturn(count);

        Statement statement1 = mock(Statement.class);
        Statement statement2 = mock(Statement.class);

        DirectiveStatement directiveStatement = DirectiveStatement.builder()
                .type(DirectiveType.REPEAT)
                .expression(countExpression)
                .statement(statement1)
                .statement(statement2)
                .build();

        RepeatDirectiveProcessor testClass = new RepeatDirectiveProcessor(statementValueSubstituter, expressionEvaluator);

        List<Statement> statements = testClass.process(directiveStatement, environment);

        List<Statement> expectedStatements = List.of(
                statement1, statement2,
                statement1, statement2,
                statement1, statement2,
                statement1, statement2
        );
        assertEquals(expectedStatements, statements);
    }

    @Test
    void testWithArgument() {
        int count = 3;
        Expression countExpression = new PrimaryExpression(TokenType.NUMBER, 3);
        when(expressionEvaluator.evaluate(countExpression, environment)).thenReturn(count);

        Statement statement1 = mock(Statement.class);
        Statement statement2 = mock(Statement.class);

        String argument = "eek";
        Statement statement1_0 = mock(Statement.class);
        Statement statement1_1 = mock(Statement.class);
        Statement statement1_2 = mock(Statement.class);
        Statement statement2_0 = mock(Statement.class);
        Statement statement2_1 = mock(Statement.class);
        Statement statement2_2 = mock(Statement.class);

        when(statementValueSubstituter.substituteValuesInStatement(statement1, argument, 0, environment)).thenReturn(statement1_0);
        when(statementValueSubstituter.substituteValuesInStatement(statement1, argument, 1, environment)).thenReturn(statement1_1);
        when(statementValueSubstituter.substituteValuesInStatement(statement1, argument, 2, environment)).thenReturn(statement1_2);
        when(statementValueSubstituter.substituteValuesInStatement(statement2, argument, 0, environment)).thenReturn(statement2_0);
        when(statementValueSubstituter.substituteValuesInStatement(statement2, argument, 1, environment)).thenReturn(statement2_1);
        when(statementValueSubstituter.substituteValuesInStatement(statement2, argument, 2, environment)).thenReturn(statement2_2);

        DirectiveStatement directiveStatement = DirectiveStatement.builder()
                .type(DirectiveType.REPEAT)
                .expression(countExpression)
                .argument(argument)
                .statement(statement1)
                .statement(statement2)
                .build();

        RepeatDirectiveProcessor testClass = new RepeatDirectiveProcessor(statementValueSubstituter, expressionEvaluator);

        List<Statement> statements = testClass.process(directiveStatement, environment);

        List<Statement> expectedStatements = List.of(
                statement1_0, statement2_0,
                statement1_1, statement2_1,
                statement1_2, statement2_2
        );
        assertEquals(expectedStatements, statements);
    }
}