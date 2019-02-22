package com.gnopai.ji65.parser.expression;

import com.gnopai.ji65.compiler.Environment;
import com.gnopai.ji65.scanner.TokenType;
import lombok.Value;

@Value
public class PrefixExpression implements Expression {
    TokenType operator;
    Expression expression;

    @Override
    public int accept(ExpressionVisitor visitor, Environment environment) {
        return visitor.visit(this, environment);
    }
}
