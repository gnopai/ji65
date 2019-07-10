package com.gnopai.ji65.parser.statement;

public interface Statement {
    <T> T accept(StatementVisitor<T> statementVisitor);
}
