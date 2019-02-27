package com.gnopai.ji65.compiler;

import com.gnopai.ji65.parser.expression.Expression;
import com.gnopai.ji65.parser.statement.*;

import java.util.List;

// TODO rename this and associated classes to "assembler" -- it's an assembler not a compiler...
public class Compiler implements StatementVisitor {
    private final InstructionCompiler instructionCompiler;
    private final FirstPassResolver firstPassResolver;
    private Environment<Expression> environment;
    private CompiledSegments compiledSegments;
    private String currentSegment = "CODE";

    public Compiler(FirstPassResolver firstPassResolver, InstructionCompiler instructionCompiler) {
        this.instructionCompiler = instructionCompiler;
        this.firstPassResolver = firstPassResolver;
    }

    public CompiledSegments compile(List<Statement> statements) {
        environment = firstPassResolver.resolve(statements);
        compiledSegments = new CompiledSegments();
        statements.forEach(statement -> statement.accept(this));
        return compiledSegments;
    }

    @Override
    public void visit(InstructionStatement instructionStatement) {
        SegmentData segmentData = instructionCompiler.compile(instructionStatement, environment);
        compiledSegments.add(currentSegment, segmentData);
    }

    @Override
    public void visit(LabelStatement labelStatement) {
        String name = labelStatement.getName();
        Label label = environment.get(name)
                .filter(l -> l instanceof Label)
                .map(l -> (Label) l)
                .orElseThrow(() -> new RuntimeException("Expected label named \"" + name + "\""));
        compiledSegments.add(currentSegment, label);
    }

    @Override
    public void visit(DirectiveStatement directiveStatement) {
        // TODO I think this is needed in both passes
    }

    @Override
    public void visit(AssignmentStatement assignmentStatement) {
        // first pass only
    }
}
