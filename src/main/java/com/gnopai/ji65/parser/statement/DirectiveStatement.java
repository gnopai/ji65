package com.gnopai.ji65.parser.statement;

import com.gnopai.ji65.compiler.SegmentData;
import com.gnopai.ji65.directive.DirectiveType;
import lombok.Builder;
import lombok.Value;

import java.util.Optional;

@Value
@Builder
public class DirectiveStatement implements Statement {
    DirectiveType type;

    @Override
    public Optional<SegmentData> accept(StatementVisitor statementVisitor) {
        return statementVisitor.visit(this);
    }
}
