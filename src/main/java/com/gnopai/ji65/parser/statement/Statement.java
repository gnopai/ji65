package com.gnopai.ji65.parser.statement;

import com.gnopai.ji65.compiler.SegmentData;

import java.util.Optional;

public interface Statement {
    Optional<SegmentData> accept(StatementVisitor statementVisitor);
}
