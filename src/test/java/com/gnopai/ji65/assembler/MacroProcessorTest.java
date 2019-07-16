package com.gnopai.ji65.assembler;

import com.gnopai.ji65.parser.expression.Expression;
import com.gnopai.ji65.parser.expression.PrimaryExpression;
import com.gnopai.ji65.parser.statement.MacroStatement;
import com.gnopai.ji65.parser.statement.Statement;
import com.gnopai.ji65.scanner.TokenType;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class MacroProcessorTest {
    private final StatementValueSubstituter statementValueSubstituter = mock(StatementValueSubstituter.class);
    private final Environment environment = new Environment();

    @Test
    void testWithNoArguments() {
        Statement statement1 = mock(Statement.class);
        Statement statement2 = mock(Statement.class);
        Statement statement3 = mock(Statement.class);
        Macro macro = new Macro("foo", List.of(statement1, statement2, statement3), List.of());
        environment.defineMacro(macro);

        MacroStatement macroStatement = new MacroStatement("foo", List.of());

        MacroProcessor testClass = new MacroProcessor(statementValueSubstituter);

        List<Statement> statements = testClass.process(macroStatement, environment);

        List<Statement> expectedStatements = List.of(statement1, statement2, statement3);
        assertEquals(expectedStatements, statements);
    }

    @Test
    void testWithArguments() {
        Statement statement1 = mock(Statement.class);
        Statement statement2 = mock(Statement.class);
        Statement statement3 = mock(Statement.class);
        String argument1 = "a";
        String argument2 = "b";
        Macro macro = new Macro("foo", List.of(statement1, statement2, statement3), List.of(argument1, argument2));
        environment.defineMacro(macro);

        Expression argumentExpression1 = new PrimaryExpression(TokenType.NUMBER, 7);
        Expression argumentExpression2 = new PrimaryExpression(TokenType.NUMBER, 4);
        MacroStatement macroStatement = new MacroStatement("foo", List.of(argumentExpression1, argumentExpression2));

        Statement statement1A = mock(Statement.class);
        Statement statement1B = mock(Statement.class);
        Statement statement2A = mock(Statement.class);
        Statement statement2B = mock(Statement.class);
        Statement statement3A = mock(Statement.class);
        Statement statement3B = mock(Statement.class);
        when(statementValueSubstituter.substituteValuesInStatement(statement1, argument1, argumentExpression1, environment)).thenReturn(statement1A);
        when(statementValueSubstituter.substituteValuesInStatement(statement1A, argument2, argumentExpression2, environment)).thenReturn(statement1B);
        when(statementValueSubstituter.substituteValuesInStatement(statement2, argument1, argumentExpression1, environment)).thenReturn(statement2A);
        when(statementValueSubstituter.substituteValuesInStatement(statement2A, argument2, argumentExpression2, environment)).thenReturn(statement2B);
        when(statementValueSubstituter.substituteValuesInStatement(statement3, argument1, argumentExpression1, environment)).thenReturn(statement3A);
        when(statementValueSubstituter.substituteValuesInStatement(statement3A, argument2, argumentExpression2, environment)).thenReturn(statement3B);

        MacroProcessor testClass = new MacroProcessor(statementValueSubstituter);

        List<Statement> statements = testClass.process(macroStatement, environment);

        List<Statement> expectedStatements = List.of(statement1B, statement2B, statement3B);
        assertEquals(expectedStatements, statements);
    }

    @Test
    void testUnknownMacro() {
        MacroStatement macroStatement = new MacroStatement("foo", List.of());
        MacroProcessor testClass = new MacroProcessor(statementValueSubstituter);
        RuntimeException exception = assertThrows(RuntimeException.class, () -> testClass.process(macroStatement, environment));
        assertEquals("Unknown macro referenced: foo", exception.getMessage());
    }
}