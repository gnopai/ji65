package com.gnopai.ji65.parser.expression;

import com.gnopai.ji65.assembler.Environment;

public interface Expression {
    <T> T accept(ExpressionVisitor<T> visitor, Environment environment);
}
