package com.gnopai.ji65.parser.expression;

import com.gnopai.ji65.compiler.Environment;

public interface Expression {
    int accept(ExpressionVisitor visitor, Environment environment);
}
