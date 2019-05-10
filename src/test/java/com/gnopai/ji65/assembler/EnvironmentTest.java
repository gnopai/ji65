package com.gnopai.ji65.assembler;

import com.gnopai.ji65.parser.expression.Expression;
import com.gnopai.ji65.parser.expression.PrimaryExpression;
import com.gnopai.ji65.scanner.TokenType;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

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
    void testGet_presentInParent() {
        Environment environment = new Environment();
        environment.define("derp", 5);
        environment.enterChildScope("foo");

        Optional<Expression> value = environment.get("derp");
        Expression expected = new PrimaryExpression(TokenType.NUMBER, 5);
        assertEquals(Optional.of(expected), value);
    }

    @Test
    void testGet_presentInChildAndInParent() {
        Environment environment = new Environment();
        environment.define("derp", 5);
        environment.enterChildScope("foo");
        environment.define("derp", 6);

        Expression expectedChildValue = new PrimaryExpression(TokenType.NUMBER, 6);
        assertEquals(Optional.of(expectedChildValue), environment.get("derp"));

        environment.goToRootScope();
        Expression expectedParentValue = new PrimaryExpression(TokenType.NUMBER, 5);
        assertEquals(Optional.of(expectedParentValue), environment.get("derp"));
    }

    @Test
    void testGetLabel_present() {
        Environment environment = new Environment();
        Label label = new Label("derp");
        environment.define("derp", label);
        assertEquals(label, environment.getLabel("derp"));
    }

    @Test
    void testGetLabel_notPresent() {
        Environment environment = new Environment();
        RuntimeException exception = assertThrows(RuntimeException.class, () -> environment.getLabel("derp"));
        assertEquals("Expected label named \"derp\"", exception.getMessage());
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