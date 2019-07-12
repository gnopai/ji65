package com.gnopai.ji65.parser.statement;

import lombok.Value;

@Value
public class UnnamedLabelStatement implements Statement {
    @Override
    public <T> T accept(StatementVisitor<T> statementVisitor) {
        return statementVisitor.visit(this);
    }
}
