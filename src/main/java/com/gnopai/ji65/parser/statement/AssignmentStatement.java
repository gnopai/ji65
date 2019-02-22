package com.gnopai.ji65.parser.statement;

import com.gnopai.ji65.compiler.SegmentData;
import com.gnopai.ji65.parser.expression.Expression;
import lombok.Value;

import java.util.Optional;

@Value
public class AssignmentStatement implements Statement {
    String name;
    Expression expression;

    @Override
    public Optional<SegmentData> accept(StatementVisitor statementVisitor) {
        return statementVisitor.visit(this);
    }
}
