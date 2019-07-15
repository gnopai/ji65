package com.gnopai.ji65.parser.statement;

import com.gnopai.ji65.parser.expression.Expression;
import lombok.Value;
import lombok.experimental.Wither;

import java.util.List;

@Value
@Wither
public class MacroStatement implements Statement {
    String name;
    List<Expression> arguments;

    @Override
    public <T> T accept(StatementVisitor<T> statementVisitor) {
        return statementVisitor.visit(this);
    }
}
