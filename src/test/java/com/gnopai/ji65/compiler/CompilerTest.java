package com.gnopai.ji65.compiler;

import com.gnopai.ji65.parser.expression.Expression;
import com.gnopai.ji65.parser.statement.LabelStatement;
import com.gnopai.ji65.parser.statement.Statement;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class CompilerTest {
    private final FirstPassResolver firstPassResolver = mock(FirstPassResolver.class);
    private final InstructionCompiler instructionCompiler = mock(InstructionCompiler.class);

    @Test
    void testCompile() {
        Statement statement1 = mock(Statement.class);
        Statement statement2 = mock(Statement.class);
        Statement statement3 = mock(Statement.class);
        List<Statement> statements = List.of(statement1, statement2, statement3);

        Environment<Expression> environment = new Environment<>();
        when(firstPassResolver.resolve(statements)).thenReturn(environment);

        Compiler compiler = new Compiler(firstPassResolver, instructionCompiler);

        CompiledSegments result = compiler.compile(statements);

        assertEquals(new CompiledSegments(), result);
        verify(statement1).accept(compiler);
        verify(statement2).accept(compiler);
        verify(statement3).accept(compiler);
    }

    @Test
    void testLabelStatement() {
        LabelStatement statement = new LabelStatement("derp");
        List<Statement> statements = List.of(statement);

        Environment<Expression> environment = new Environment<>();
        Label expectedLabel = new Label("derp", false);
        environment.define("derp", expectedLabel);

        when(firstPassResolver.resolve(statements)).thenReturn(environment);

        Compiler compiler = new Compiler(firstPassResolver, instructionCompiler);

        CompiledSegments result = compiler.compile(statements);

        Segment expectedSegment = new Segment("CODE", false, List.of(expectedLabel));
        CompiledSegments expectedResult = new CompiledSegments(Map.of("CODE", expectedSegment));
        assertEquals(expectedResult, result);
    }
}