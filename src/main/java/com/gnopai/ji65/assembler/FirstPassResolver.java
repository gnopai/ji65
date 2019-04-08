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
    private String currentSegment;

    public FirstPassResolver(ExpressionEvaluator expressionEvaluator) {
        this.expressionEvaluator = expressionEvaluator;
    }

    public void resolve(List<Statement> statements, AssembledSegments assembledSegments) {
        this.assembledSegments = assembledSegments;
        statements.forEach(statement -> statement.accept(this));
    }

    @Override
    public void visit(InstructionStatement instructionStatement) {
        // second pass only
    }

    @Override
    public void visit(LabelStatement labelStatement) {
        String name = labelStatement.getName();
        getEnvironment().define(name, new Label(name, isCurrentSegmentZeroPage()));
    }

    @Override
    public void visit(DirectiveStatement directiveStatement) {
        if (DirectiveType.SEGMENT.equals(directiveStatement.getType())) {
            currentSegment = directiveStatement.getName();
        }
    }

    @Override
    public void visit(AssignmentStatement assignmentStatement) {
        // TODO support labels here? We'll error out on them as it is now.
        int value = expressionEvaluator.evaluate(assignmentStatement.getExpression(), getEnvironment());
        getEnvironment().define(assignmentStatement.getName(), new PrimaryExpression(TokenType.NUMBER, value));
    }

    private boolean isCurrentSegmentZeroPage() {
        return assembledSegments.getSegment(currentSegment)
                .map(Segment::isZeroPage)
                .orElse(false);
    }

    private Environment getEnvironment() {
        return assembledSegments.getEnvironment();
    }
}
