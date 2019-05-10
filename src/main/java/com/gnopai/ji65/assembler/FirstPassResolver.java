package com.gnopai.ji65.assembler;

import com.gnopai.ji65.DirectiveType;
import com.gnopai.ji65.parser.expression.ExpressionEvaluator;
import com.gnopai.ji65.parser.expression.PrimaryExpression;
import com.gnopai.ji65.parser.statement.*;
import com.gnopai.ji65.scanner.TokenType;

import java.util.List;

public class FirstPassResolver implements StatementVisitor {
    private final ExpressionEvaluator expressionEvaluator;
    private AssembledSegments assembledSegments;
    private Environment environment;
    private String currentSegment;

    public FirstPassResolver(ExpressionEvaluator expressionEvaluator) {
        this.expressionEvaluator = expressionEvaluator;
    }

    public void resolve(List<Statement> statements, AssembledSegments assembledSegments) {
        this.assembledSegments = assembledSegments;
        this.environment = assembledSegments.getEnvironment();
        statements.forEach(statement -> statement.accept(this));
    }

    @Override
    public void visit(InstructionStatement instructionStatement) {
        // second pass only
    }

    @Override
    public void visit(LabelStatement labelStatement) {
        String name = labelStatement.getName();
        Label label = Label.builder()
                .name(name)
                .zeroPage(isCurrentSegmentZeroPage())
                .build();
        environment.defineGlobal(name, label);
        environment.goToRootScope().enterChildScope(name);
    }

    @Override
    public void visit(LocalLabelStatement localLabelStatement) {
        String name = localLabelStatement.getName();
        Label label = Label.builder()
                .name(name)
                .zeroPage(isCurrentSegmentZeroPage())
                .local(true)
                .build();
        environment.define(name, label);
    }

    @Override
    public void visit(DirectiveStatement directiveStatement) {
        if (DirectiveType.SEGMENT.equals(directiveStatement.getType())) {
            currentSegment = directiveStatement.getName();
            environment.goToRootScope();
        }
    }

    @Override
    public void visit(AssignmentStatement assignmentStatement) {
        // TODO support labels here? We'll error out on them as it is now.
        int value = expressionEvaluator.evaluate(assignmentStatement.getExpression(), environment);
        environment.defineGlobal(assignmentStatement.getName(), new PrimaryExpression(TokenType.NUMBER, value));
    }

    private boolean isCurrentSegmentZeroPage() {
        return assembledSegments.getSegment(currentSegment)
                .map(Segment::isZeroPage)
                .orElse(false);
    }
}
