package com.gnopai.ji65.compiler;

import com.gnopai.ji65.parser.statement.*;

import java.util.List;

public class Compiler implements StatementVisitor {
    private final InstructionCompiler instructionCompiler;
    private final ExpressionEvaluator expressionEvaluator;
    private String currentSegment = "CODE";

    public Compiler(InstructionCompiler instructionCompiler, ExpressionEvaluator expressionEvaluator) {
        this.instructionCompiler = instructionCompiler;
        this.expressionEvaluator = expressionEvaluator;
    }

    public CompiledSegments compile(List<Statement> statements) {
        CompiledSegments compiledSegments = new CompiledSegments();
        for (Statement statement : statements) {
            SegmentData segmentData = statement.accept(this);
            compiledSegments.add(currentSegment, segmentData);
        }
        return compiledSegments;
    }

    @Override
    public SegmentData visit(InstructionStatement instructionStatement) {
        return instructionCompiler.compile(instructionStatement);
    }

    @Override
    public SegmentData visit(ExpressionStatement expressionStatement) {
        int value = expressionEvaluator.evaluate(expressionStatement.getExpression());
        byte highByte = (byte) (value / 256);
        byte lowByte = (byte) (value % 256);
        return new RawData(List.of(lowByte, highByte));
    }

    @Override
    public SegmentData visit(LabelStatement labelStatement) {
        return new Label(labelStatement.getName());
    }

    @Override
    public SegmentData visit(DirectiveStatement directiveStatement) {
        // TODO
        return null;
    }
}
