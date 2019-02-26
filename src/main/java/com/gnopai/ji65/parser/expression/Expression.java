package com.gnopai.ji65.parser.expression;

import com.gnopai.ji65.compiler.Environment;

public interface Expression {
    <T> T accept(ExpressionVisitor<T> visitor, Environment environment);
}
