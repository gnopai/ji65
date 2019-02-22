package com.gnopai.ji65.parser.statement;

import lombok.Value;

@Value
public class LabelStatement implements Statement {
    String name;

    @Override
    public void accept(StatementVisitor statementVisitor) {
        statementVisitor.visit(this);
    }
}
