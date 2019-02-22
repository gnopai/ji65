package com.gnopai.ji65.compiler;

import com.gnopai.ji65.parser.expression.PrimaryExpression;
import com.gnopai.ji65.parser.statement.AssignmentStatement;
import com.gnopai.ji65.parser.statement.ExpressionStatement;
import com.gnopai.ji65.parser.statement.Statement;
import com.gnopai.ji65.scanner.TokenType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class CompilerTest {
    private final InstructionCompiler instructionCompiler = mock(InstructionCompiler.class);
    private final ExpressionEvaluator expressionEvaluator = mock(ExpressionEvaluator.class);
    private Environment environment;

    @BeforeEach
    void setUp() {
        environment = new Environment();
    }

    @Test
    void testExpressionStatement() {
        PrimaryExpression expression = new PrimaryExpression(TokenType.NUMBER, 19);
        ExpressionStatement expressionStatement = new ExpressionStatement(expression);

        int expressionValue = 259;
        when(expressionEvaluator.evaluate(expression, environment)).thenReturn(expressionValue);

        Compiler compiler = new Compiler(instructionCompiler, expressionEvaluator);

        CompiledSegments result = compiler.compile(List.of(expressionStatement), environment);

        CompiledSegments expectedResult = new CompiledSegments(Map.of(
                "CODE", new Segment("CODE", List.of(
                        new RawData((byte) 3, (byte) 1)
                ))
        ));
        assertEquals(expectedResult, result);
    }

    @Test
    void testAssignmentStatement() {
        PrimaryExpression expression = new PrimaryExpression(TokenType.NUMBER, 55);
        when(expressionEvaluator.evaluate(expression, environment)).thenReturn(55);

        AssignmentStatement statement = new AssignmentStatement("derp", expression);

        Compiler compiler = new Compiler(instructionCompiler, expressionEvaluator);

        CompiledSegments result = compiler.compile(List.of(statement), environment);

        assertEquals(new CompiledSegments(), result);
        assertEquals(Optional.of(55), environment.get("derp"));
    }

    @Test
    void testCompile() {
        Statement statement1 = mock(Statement.class);
        Statement statement2 = mock(Statement.class);
        Statement statement3 = mock(Statement.class);

        Compiler compiler = new Compiler(instructionCompiler, expressionEvaluator);

        CompiledSegments result = compiler.compile(List.of(statement1, statement2, statement3), environment);

        assertEquals(new CompiledSegments(), result);
        verify(statement1).accept(compiler);
        verify(statement2).accept(compiler);
        verify(statement3).accept(compiler);
    }
}