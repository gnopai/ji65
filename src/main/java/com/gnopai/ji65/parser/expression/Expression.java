package com.gnopai.ji65.parser.expression;

public interface Expression {
    int accept(ExpressionVisitor visitor);
}
