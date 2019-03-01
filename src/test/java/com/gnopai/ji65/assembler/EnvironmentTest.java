package com.gnopai.ji65.assembler;

import com.gnopai.ji65.parser.expression.Expression;
import com.gnopai.ji65.parser.expression.PrimaryExpression;
import com.gnopai.ji65.scanner.TokenType;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class EnvironmentTest {

    @Test
    void testGet_notPresent() {
        Environment environment = new Environment();
        Optional<Expression> value = environment.get("derp");
        assertFalse(value.isPresent());
    }

    @Test
    void testGet_present() {
        Environment environment = new Environment();
        environment.define("derp", 5);
        Optional<Expression> value = environment.get("derp");
        Expression expected = new PrimaryExpression(TokenType.NUMBER, 5);
        assertEquals(Optional.of(expected), value);
    }

    @Test
    void testDefine_duplicate() {
        Environment environment = new Environment();
        environment.define("derp", 5);
        environment.define("derp", 8);
        Optional<Expression> value = environment.get("derp");
        Expression expected = new PrimaryExpression(TokenType.NUMBER, 8);
        assertEquals(Optional.of(expected), value);
    }
}