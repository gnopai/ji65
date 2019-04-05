package com.gnopai.ji65.parser.statement;

import com.gnopai.ji65.directive.DirectiveType;
import com.gnopai.ji65.parser.expression.Expression;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class DirectiveStatement implements Statement {
    DirectiveType type;
    String name;
    Expression expression;
    // TODO more stuff here... eventually

    @Override
    public void accept(StatementVisitor statementVisitor) {
        statementVisitor.visit(this);
    }
}
