package com.gnopai.ji65.parser.statement;

import com.gnopai.ji65.compiler.SegmentData;

public interface Statement {
    SegmentData accept(StatementVisitor statementVisitor);
}
