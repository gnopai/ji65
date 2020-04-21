package com.gnopai.ji65;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DirectiveTypeTest {

    @ParameterizedTest
    @EnumSource
    void fromIdentifier(DirectiveType directiveType) {
        directiveType.getIdentifiers().forEach(identifier ->
                assertEquals(Optional.of(directiveType), DirectiveType.fromIdentifier(identifier))
        );
    }

    @Test
    void fromIdentifier_invalidDirective() {
        assertEquals(Optional.empty(), DirectiveType.fromIdentifier("not_a_directive"));
    }
}