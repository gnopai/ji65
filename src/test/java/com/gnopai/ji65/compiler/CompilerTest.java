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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CompilerTest {
    private final InstructionCompiler instructionCompiler = mock(InstructionCompiler.class);
    private final ExpressionEvaluator expressionEvaluator = mock(ExpressionEvaluator.class);
    private Environment environment;

    @BeforeEach
    void setUp() {
        environment = new Environment();
    }

    @Test
    void testVisitExpressionStatement() {
        PrimaryExpression expression = new PrimaryExpression(TokenType.NUMBER, 19);
        ExpressionStatement expressionStatement = new ExpressionStatement(expression);

        int expressionValue = 259;
        when(expressionEvaluator.evaluate(expression)).thenReturn(expressionValue);

        Compiler compiler = new Compiler(instructionCompiler, expressionEvaluator, environment);
        Optional<SegmentData> result = compiler.visit(expressionStatement);
        assertEquals(new RawData((byte) 3, (byte) 1), result.get());
    }

    @Test
    void testVisitAssignmentStatement() {
        PrimaryExpression expression = new PrimaryExpression(TokenType.NUMBER, 55);
        when(expressionEvaluator.evaluate(expression)).thenReturn(55);

        AssignmentStatement statement = new AssignmentStatement("derp", expression);

        Compiler compiler = new Compiler(instructionCompiler, expressionEvaluator, environment);

        Optional<SegmentData> result = compiler.visit(statement);
        assertFalse(result.isPresent());
        assertEquals(Optional.of(55), environment.get("derp"));
    }

    @Test
    void testCompile() {
        Statement statement1 = mock(Statement.class);
        Statement statement2 = mock(Statement.class);
        Statement statement3 = mock(Statement.class);

        SegmentData segmentData1 = new RawData((byte) 1);
        SegmentData segmentData2 = new RawData((byte) 2);
        SegmentData segmentData3 = new RawData((byte) 3);

        Compiler compiler = new Compiler(instructionCompiler, expressionEvaluator, environment);

        when(statement1.accept(compiler)).thenReturn(Optional.of(segmentData1));
        when(statement2.accept(compiler)).thenReturn(Optional.of(segmentData2));
        when(statement3.accept(compiler)).thenReturn(Optional.of(segmentData3));

        CompiledSegments result = compiler.compile(List.of(statement1, statement2, statement3));

        CompiledSegments expectedResult = new CompiledSegments(Map.of(
                "CODE", new Segment("CODE", List.of(segmentData1, segmentData2, segmentData3))
        ));
        assertEquals(expectedResult, result);
    }
}