package com.gnopai.ji65.parser.statement;

import lombok.Value;

import java.util.List;

@Value
public class MultiStatement implements Statement {
    List<Statement> statements;

    @Override
    public <T> T accept(StatementVisitor<T> statementVisitor) {
        return statementVisitor.visit(this);
    }
}
