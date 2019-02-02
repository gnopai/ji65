package com.gnopai.ji65.parser.expression;

import com.gnopai.ji65.scanner.TokenType;
import lombok.Value;

@Value
public class PrimaryExpression implements Expression {
    TokenType type;
    Object value;
}
