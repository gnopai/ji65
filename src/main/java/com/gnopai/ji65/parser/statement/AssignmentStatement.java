package com.gnopai.ji65.parser.statement;

import com.gnopai.ji65.parser.expression.Expression;
import lombok.Value;

@Value
public class AssignmentStatement implements Statement {
    String name;
    Expression expression;

    @Override
    public <T> T accept(StatementVisitor<T> statementVisitor) {
        return statementVisitor.visit(this);
    }
}
