package com.gnopai.ji65.parser.statement;

import com.gnopai.ji65.parser.expression.Expression;
import lombok.Value;

@Value
public class ExpressionStatement implements Statement {
    Expression expression;

    @Override
    public void accept(StatementVisitor statementVisitor) {
        statementVisitor.visit(this);
    }
}
