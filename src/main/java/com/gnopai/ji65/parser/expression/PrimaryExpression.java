package com.gnopai.ji65.parser.expression;

import lombok.Value;

@Value
public class PrimaryExpression implements Expression {
    Object value;
}
