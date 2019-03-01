package com.gnopai.ji65.parser.statement;

public interface Statement {
    void accept(StatementVisitor statementVisitor);
}
