package com.gnopai.ji65.compiler;

import com.gnopai.ji65.parser.statement.*;

import java.util.List;

public class Compiler implements StatementVisitor {
    private final InstructionCompiler instructionCompiler;
    private final ExpressionEvaluator expressionEvaluator;
    private Environment environment;
    private CompiledSegments compiledSegments;
    private String currentSegment = "CODE";

    public Compiler(InstructionCompiler instructionCompiler, ExpressionEvaluator expressionEvaluator) {
        this.instructionCompiler = instructionCompiler;
        this.expressionEvaluator = expressionEvaluator;
    }

    public CompiledSegments compile(List<Statement> statements, Environment environment) {
        this.environment = environment;
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
    public void visit(ExpressionStatement expressionStatement) {
        // FIXME not used yet -- and maybe this should be single-byte anyway?
        int value = expressionEvaluator.evaluate(expressionStatement.getExpression(), environment);
        byte highByte = (byte) (value / 256);
        byte lowByte = (byte) (value % 256);
        RawData rawData = new RawData(List.of(lowByte, highByte));
        compiledSegments.add(currentSegment, rawData);
    }

    @Override
    public void visit(LabelStatement labelStatement) {
        Label label = new Label(labelStatement.getName());
        compiledSegments.add(currentSegment, label);
    }

    @Override
    public void visit(DirectiveStatement directiveStatement) {
        // TODO
    }

    @Override
    public void visit(AssignmentStatement assignmentStatement) {
        int value = expressionEvaluator.evaluate(assignmentStatement.getExpression(), environment);
        environment.define(assignmentStatement.getName(), value);
    }
}
