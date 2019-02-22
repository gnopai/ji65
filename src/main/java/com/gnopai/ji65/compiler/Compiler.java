package com.gnopai.ji65.compiler;

import com.gnopai.ji65.parser.statement.*;

import java.util.List;
import java.util.Optional;

public class Compiler implements StatementVisitor {
    private final InstructionCompiler instructionCompiler;
    private final ExpressionEvaluator expressionEvaluator;
    private final Environment environment;
    private String currentSegment = "CODE";

    public Compiler(InstructionCompiler instructionCompiler, ExpressionEvaluator expressionEvaluator, Environment environment) {
        this.instructionCompiler = instructionCompiler;
        this.expressionEvaluator = expressionEvaluator;
        this.environment = environment;
    }

    public CompiledSegments compile(List<Statement> statements) {
        CompiledSegments compiledSegments = new CompiledSegments();
        for (Statement statement : statements) {
            Optional<SegmentData> segmentData = statement.accept(this);
            segmentData.ifPresent(data -> compiledSegments.add(currentSegment, data));
        }
        return compiledSegments;
    }

    @Override
    public Optional<SegmentData> visit(InstructionStatement instructionStatement) {
        return Optional.of(instructionCompiler.compile(instructionStatement));
    }

    @Override
    public Optional<SegmentData> visit(ExpressionStatement expressionStatement) {
        int value = expressionEvaluator.evaluate(expressionStatement.getExpression());
        byte highByte = (byte) (value / 256);
        byte lowByte = (byte) (value % 256);
        return Optional.of(new RawData(List.of(lowByte, highByte)));
    }

    @Override
    public Optional<SegmentData> visit(LabelStatement labelStatement) {
        return Optional.of(new Label(labelStatement.getName()));
    }

    @Override
    public Optional<SegmentData> visit(DirectiveStatement directiveStatement) {
        // TODO
        return Optional.empty();
    }

    @Override
    public Optional<SegmentData> visit(AssignmentStatement assignmentStatement) {
        int value = expressionEvaluator.evaluate(assignmentStatement.getExpression());
        environment.define(assignmentStatement.getName(), value);
        return Optional.empty();
    }
}
