package com.gnopai.ji65.assembler;

import com.gnopai.ji65.AddressingModeType;
import com.gnopai.ji65.DirectiveType;
import com.gnopai.ji65.InstructionType;
import com.gnopai.ji65.parser.expression.Expression;
import com.gnopai.ji65.parser.expression.PrimaryExpression;
import com.gnopai.ji65.parser.statement.*;
import com.gnopai.ji65.scanner.TokenType;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class StatementValueSubstituterTest {
    private final ExpressionValueSubstituter expressionValueSubstituter = mock(ExpressionValueSubstituter.class);
    private final String name = "valueName";
    private final Expression value = new PrimaryExpression(TokenType.NUMBER, 99);
    private final Environment environment = new Environment();

    @Test
    void testInstructionStatement() {
        Expression addressExpression = mock(Expression.class);
        InstructionStatement instructionStatement = InstructionStatement.builder()
                .instructionType(InstructionType.LDA)
                .addressingModeType(AddressingModeType.ABSOLUTE)
                .addressExpression(addressExpression)
                .build();

        Expression tweakedAddressExpression = mock(Expression.class);
        when(expressionValueSubstituter.substituteValue(name, value, addressExpression, environment)).thenReturn(tweakedAddressExpression);

        StatementValueSubstituter testClass = new StatementValueSubstituter(expressionValueSubstituter);

        Statement statement = testClass.substituteValuesInStatement(instructionStatement, name, value, environment);

        assertEquals(instructionStatement.withAddressExpression(tweakedAddressExpression), statement);
    }

    @Test
    void testInstructionStatement_noAddressExpression() {
        InstructionStatement instructionStatement = InstructionStatement.builder()
                .instructionType(InstructionType.INX)
                .addressingModeType(AddressingModeType.IMPLICIT)
                .build();

        StatementValueSubstituter testClass = new StatementValueSubstituter(expressionValueSubstituter);

        Statement statement = testClass.substituteValuesInStatement(instructionStatement, name, value, environment);

        assertEquals(instructionStatement, statement);
    }

    @Test
    void testAssignmentStatement() {
        Expression assignmentExpression = mock(Expression.class);
        AssignmentStatement assignmentStatement = new AssignmentStatement("whee", assignmentExpression);

        Expression tweakedAddressExpression = mock(Expression.class);
        when(expressionValueSubstituter.substituteValue(name, value, assignmentExpression, environment)).thenReturn(tweakedAddressExpression);

        StatementValueSubstituter testClass = new StatementValueSubstituter(expressionValueSubstituter);

        Statement statement = testClass.substituteValuesInStatement(assignmentStatement, name, value, environment);

        assertEquals(assignmentStatement.withExpression(tweakedAddressExpression), statement);
    }

    @Test
    void testDirectiveStatement() {
        Expression statement1Expression = mock(Expression.class);
        InstructionStatement statement1 = InstructionStatement.builder()
                .instructionType(InstructionType.LDA)
                .addressingModeType(AddressingModeType.ABSOLUTE_Y)
                .addressExpression(statement1Expression)
                .build();
        Expression tweakedStatement1Expression = mock(Expression.class);
        when(expressionValueSubstituter.substituteValue(name, value, statement1Expression, environment)).thenReturn(tweakedStatement1Expression);

        Expression statement2Expression = mock(Expression.class);
        InstructionStatement statement2 = InstructionStatement.builder()
                .instructionType(InstructionType.LDX)
                .addressingModeType(AddressingModeType.ABSOLUTE)
                .addressExpression(statement2Expression)
                .build();
        Expression tweakedStatement2Expression = mock(Expression.class);
        when(expressionValueSubstituter.substituteValue(name, value, statement2Expression, environment)).thenReturn(tweakedStatement2Expression);

        Expression expression1 = mock(Expression.class);
        Expression tweakedExpression1 = mock(Expression.class);
        when(expressionValueSubstituter.substituteValue(name, value, expression1, environment)).thenReturn(tweakedExpression1);

        Expression expression2 = mock(Expression.class);
        Expression tweakedExpression2 = mock(Expression.class);
        when(expressionValueSubstituter.substituteValue(name, value, expression2, environment)).thenReturn(tweakedExpression2);

        DirectiveStatement directiveStatement = DirectiveStatement.builder()
                .type(DirectiveType.REPEAT)
                .statement(statement1)
                .statement(statement2)
                .expression(expression1)
                .expression(expression2)
                .build();

        StatementValueSubstituter testClass = new StatementValueSubstituter(expressionValueSubstituter);

        Statement statement = testClass.substituteValuesInStatement(directiveStatement, name, value, environment);

        Statement expectedStatement = DirectiveStatement.builder()
                .type(DirectiveType.REPEAT)
                .statement(statement1.withAddressExpression(tweakedStatement1Expression))
                .statement(statement2.withAddressExpression(tweakedStatement2Expression))
                .expression(tweakedExpression1)
                .expression(tweakedExpression2)
                .build();
        assertEquals(expectedStatement, statement);
    }

    @Test
    void testMultiStatement() {
        Statement statement1 = mock(Statement.class);
        Statement statement2 = mock(Statement.class);
        MultiStatement multiStatement = new MultiStatement(List.of(statement1, statement2));

        StatementValueSubstituter testClass = new StatementValueSubstituter(expressionValueSubstituter);

        Statement substitutedStatement1 = mock(Statement.class);
        Statement substitutedStatement2 = mock(Statement.class);
        when(statement1.accept(testClass)).thenReturn(substitutedStatement1);
        when(statement2.accept(testClass)).thenReturn(substitutedStatement2);

        Statement statement = testClass.substituteValuesInStatement(multiStatement, name, value, environment);

        MultiStatement expectedStatement = new MultiStatement(List.of(substitutedStatement1, substitutedStatement2));
        assertEquals(expectedStatement, statement);
    }
}