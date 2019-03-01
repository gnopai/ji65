package com.gnopai.ji65.parser.expression;

import com.gnopai.ji65.assembler.Environment;
import com.gnopai.ji65.scanner.TokenType;
import lombok.Value;

@Value
public class PrimaryExpression implements Expression {
    TokenType type;
    int value;

    @Override
    public <T> T accept(ExpressionVisitor<T> visitor, Environment environment) {
        return visitor.visit(this, environment);
    }
}
