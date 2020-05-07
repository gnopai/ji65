package com.gnopai.ji65.parser.statement;

import com.gnopai.ji65.parser.expression.Expression;
import com.gnopai.ji65.test.Target;
import lombok.Builder;
import lombok.Singular;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class TestStatement implements Statement {
    Type type;
    Target target;
    Expression targetAddress;
    @Singular
    List<Expression> values;
    String message;

    @Override
    public <T> T accept(StatementVisitor<T> statementVisitor) {
        return statementVisitor.visit(this);
    }

    public Expression getFirstValue() {
        return values.get(0);
    }

    public enum Type {
        SET,
        RUN,
        ASSERT,
        MOCK,
        VERIFY_READ,
        VERIFY_WRITE,
        ;
    }
}
