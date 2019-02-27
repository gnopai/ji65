package com.gnopai.ji65.parser.expression;

import com.gnopai.ji65.assembler.Environment;

public interface Expression {
    <T, E> T accept(ExpressionVisitor<T, E> visitor, Environment<E> environment);
}
