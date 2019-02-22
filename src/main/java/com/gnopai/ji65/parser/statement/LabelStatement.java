package com.gnopai.ji65.parser.statement;

import com.gnopai.ji65.compiler.SegmentData;
import lombok.Value;

import java.util.Optional;

@Value
public class LabelStatement implements Statement {
    String name;

    @Override
    public Optional<SegmentData> accept(StatementVisitor statementVisitor) {
        return statementVisitor.visit(this);
    }
}
