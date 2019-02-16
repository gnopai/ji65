package com.gnopai.ji65.parser.statement;

import com.gnopai.ji65.compiler.SegmentData;
import lombok.Value;

@Value
public class LabelStatement implements Statement {
    String name;

    @Override
    public SegmentData accept(StatementVisitor statementVisitor) {
        return statementVisitor.visit(this);
    }
}
