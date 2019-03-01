package com.gnopai.ji65.assembler;

import com.gnopai.ji65.parser.expression.Expression;
import com.gnopai.ji65.parser.expression.PrimaryExpression;
import com.gnopai.ji65.scanner.TokenType;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@EqualsAndHashCode
@ToString
public class Environment {
    private final Map<String, Expression> values = new HashMap<>();

    public Optional<Expression> get(String name) {
        return Optional.ofNullable(values.get(name));
    }

    public void define(String name, Expression value) {
        values.put(name, value);
    }

    public void define(String name, int value) {
        values.put(name, new PrimaryExpression(TokenType.NUMBER, value));
    }
}
