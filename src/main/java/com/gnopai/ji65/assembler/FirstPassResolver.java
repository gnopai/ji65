package com.gnopai.ji65.assembler;

import com.gnopai.ji65.parser.expression.ExpressionEvaluator;
import com.gnopai.ji65.parser.expression.PrimaryExpression;
import com.gnopai.ji65.parser.statement.*;
import com.gnopai.ji65.scanner.TokenType;

import java.util.List;

public class FirstPassResolver implements StatementVisitor<Void> {
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
        environment.goToRootScope();
        statements.forEach(statement -> statement.accept(this));
    }

    @Override
    public Void visit(InstructionStatement instructionStatement) {
        // second pass only
        return null;
    }

    @Override
    public Void visit(LabelStatement labelStatement) {
        String name = labelStatement.getName();
        Label label = Label.builder()
                .name(name)
                .zeroPage(isCurrentSegmentZeroPage())
                .build();
        environment.defineGlobal(name, label);
        environment.goToRootScope().enterChildScope(name);
        return null;
    }

    @Override
    public Void visit(UnnamedLabelStatement unnamedLabelStatement) {
        // no-op
        return null;
    }

    @Override
    public Void visit(LocalLabelStatement localLabelStatement) {
        String name = localLabelStatement.getName();
        Label label = Label.builder()
                .name(name)
                .zeroPage(isCurrentSegmentZeroPage())
                .local(true)
                .build();
        environment.define(name, label);
        return null;
    }

    @Override
    public Void visit(DirectiveStatement directiveStatement) {
        switch (directiveStatement.getType()) {
            case SEGMENT:
                currentSegment = directiveStatement.getName();
                environment.goToRootScope();
                break;
            case MACRO:
                environment.defineMacro(Macro.of(directiveStatement));
                break;
        }
        return null;
    }

    @Override
    public Void visit(AssignmentStatement assignmentStatement) {
        // TODO support labels here? We'll error out on them as it is now.
        int value = expressionEvaluator.evaluate(assignmentStatement.getExpression(), environment);
        environment.defineGlobal(assignmentStatement.getName(), new PrimaryExpression(TokenType.NUMBER, value));
        return null;
    }

    @Override
    public Void visit(MacroStatement macroStatement) {
        // no-op
        return null;
    }

    private boolean isCurrentSegmentZeroPage() {
        return assembledSegments.getSegment(currentSegment)
                .map(Segment::isZeroPage)
                .orElse(false);
    }
}
