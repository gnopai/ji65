package com.gnopai.ji65.parser.statement;

import com.gnopai.ji65.DirectiveType;
import com.gnopai.ji65.parser.expression.Expression;
import lombok.Builder;
import lombok.Singular;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class DirectiveStatement implements Statement {
    DirectiveType type;
    String name;
    @Singular
    List<Expression> expressions;

    @Override
    public void accept(StatementVisitor statementVisitor) {
        statementVisitor.visit(this);
    }

    public Expression getExpression() {
        return expressions.get(0);
    }
}
