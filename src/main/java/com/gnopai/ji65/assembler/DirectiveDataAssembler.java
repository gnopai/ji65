package com.gnopai.ji65.assembler;

import com.gnopai.ji65.parser.expression.ExpressionEvaluator;
import com.gnopai.ji65.parser.statement.DirectiveStatement;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class DirectiveDataAssembler {
    private final ExpressionEvaluator expressionEvaluator;

    public DirectiveDataAssembler(ExpressionEvaluator expressionEvaluator) {
        this.expressionEvaluator = expressionEvaluator;
    }

    public List<SegmentData> assemble(DirectiveStatement directiveStatement, Environment environment) {
        switch (directiveStatement.getType()) {
            case RESERVE:
                int size = expressionEvaluator.evaluate(directiveStatement.getExpression(), environment);
                return List.of(new ReservedData(size));
            case BYTE:
                return directiveStatement.getExpressions().stream()
                        .map(expression -> new UnresolvedExpression(expression, true))
                        .collect(toList());
            case WORD:
                return directiveStatement.getExpressions().stream()
                        .map(expression -> new UnresolvedExpression(expression, false))
                        .collect(toList());
            default:
                throw new RuntimeException("Directive type not supported: " + directiveStatement.getType().name());
        }
    }
}