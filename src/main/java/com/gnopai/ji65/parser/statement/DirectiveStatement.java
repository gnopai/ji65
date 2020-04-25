package com.gnopai.ji65.parser.statement;

import com.gnopai.ji65.DirectiveType;
import com.gnopai.ji65.parser.expression.Expression;
import lombok.Builder;
import lombok.Singular;
import lombok.Value;
import lombok.With;

import java.util.List;

@Value
@Builder
@With
public class DirectiveStatement implements Statement {
    DirectiveType type;
    String name;
    @Singular
    List<Statement> statements;
    @Singular
    List<Expression> expressions;
    @Singular
    List<String> arguments;

    @Override
    public <T> T accept(StatementVisitor<T> statementVisitor) {
        return statementVisitor.visit(this);
    }

    public Expression getExpression() {
        return expressions.get(0);
    }

    public boolean hasArguments() {
        return !arguments.isEmpty();
    }

    public String getArgument(int i) {
        return arguments.get(i);
    }
}
