package com.gnopai.ji65.parser.expression;

import com.gnopai.ji65.compiler.Environment;

public interface Expression {
    <T, E> T accept(ExpressionVisitor<T, E> visitor, Environment<E> environment);
}
