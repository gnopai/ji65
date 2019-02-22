package com.gnopai.ji65.compiler;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class EnvironmentTest {

    @Test
    void testGet_notPresent() {
        Environment environment = new Environment();
        Optional<Integer> value = environment.get("derp");
        assertFalse(value.isPresent());
    }

    @Test
    void testGet_present() {
        Environment environment = new Environment();
        environment.define("derp", 5);
        Optional<Integer> value = environment.get("derp");
        assertEquals(Optional.of(5), value);
    }

    @Test
    void testDefine_duplicate() {
        Environment environment = new Environment();
        environment.define("derp", 5);
        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                environment.define("derp", 7)
        );
        assertEquals("Duplicate assignment of \"derp\"", exception.getMessage());
    }
}