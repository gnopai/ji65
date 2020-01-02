package com.gnopai.ji65.parser.statement;

import com.gnopai.ji65.parser.expression.Expression;
import com.gnopai.ji65.test.Target;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class TestStatement implements Statement {
    Type type;
    Target target;
    Expression targetAddress;
    Expression value;
    String message;

    @Override
    public <T> T accept(StatementVisitor<T> statementVisitor) {
        return statementVisitor.visit(this);
    }

    public enum Type { SET, RUN, ASSERT }
}
