package com.gnopai.ji65.parser.statement;

import lombok.Value;

@Value
public class LabelStatement implements Statement {
    String name;

    @Override
    public <T> T accept(StatementVisitor<T> statementVisitor) {
        return statementVisitor.visit(this);
    }
}
