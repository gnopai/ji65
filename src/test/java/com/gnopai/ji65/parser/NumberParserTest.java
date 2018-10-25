package com.gnopai.ji65.parser;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class NumberParserTest {
    private NumberParser testClass;

    @BeforeEach
    public void setUp() {
        testClass = new NumberParser();
    }

    @Test
    public void testValidHexValue() {
        assertEquals(Optional.of(65535), testClass.parseValue("$FFFF"));
        assertEquals(Optional.of(4660), testClass.parseValue("$1234"));
        assertEquals(Optional.of(1114), testClass.parseValue("$45A"));
        assertEquals(Optional.of(90), testClass.parseValue("$5A"));
        assertEquals(Optional.of(10), testClass.parseValue("$A"));
        assertEquals(Optional.of(1), testClass.parseValue("$1"));
        assertEquals(Optional.of(0), testClass.parseValue("$0"));
    }

    @Test
    public void testInvalidHexValue() {
        assertFalse(testClass.parseValue("$FFFFF").isPresent());
        assertFalse(testClass.parseValue("$12345").isPresent());
        assertFalse(testClass.parseValue("$12G1").isPresent());
        assertFalse(testClass.parseValue("$").isPresent());
    }

    @Test
    public void testValidBinaryValue() {
        assertEquals(Optional.of(255), testClass.parseValue("%11111111"));
        assertEquals(Optional.of(85), testClass.parseValue("%01010101"));
        assertEquals(Optional.of(0), testClass.parseValue("%00000000"));
    }

    @Test
    public void testInvalidBinaryValue() {
        assertFalse(testClass.parseValue("%111111111").isPresent());
        assertFalse(testClass.parseValue("%0010101").isPresent());
        assertFalse(testClass.parseValue("%01210101").isPresent());
        assertFalse(testClass.parseValue("%").isPresent());
    }

    @Test
    public void testValidDecimalValue() {
        assertEquals(Optional.of(65535), testClass.parseValue("65535"));
        assertEquals(Optional.of(6553), testClass.parseValue("6553"));
        assertEquals(Optional.of(124), testClass.parseValue("124"));
        assertEquals(Optional.of(12), testClass.parseValue("12"));
        assertEquals(Optional.of(1), testClass.parseValue("1"));
        assertEquals(Optional.of(0), testClass.parseValue("0"));
    }

    @Test
    public void testInvalidDecimalValue() {
        assertFalse(testClass.parseValue("65536").isPresent());
        assertFalse(testClass.parseValue("-1").isPresent());
    }

    @Test
    public void testIsZeroPageValue_valid() {
        assertTrue(testClass.isZeroPageValue("$00"));
        assertTrue(testClass.isZeroPageValue("$FF"));
        assertTrue(testClass.isZeroPageValue("$A"));
        assertTrue(testClass.isZeroPageValue("$1"));
        assertTrue(testClass.isZeroPageValue("$0"));
        assertTrue(testClass.isZeroPageValue("$0000"));
        assertTrue(testClass.isZeroPageValue("$0011"));
        assertTrue(testClass.isZeroPageValue("$001"));
        assertTrue(testClass.isZeroPageValue("$00A"));
        assertTrue(testClass.isZeroPageValue("%10101010"));
        assertTrue(testClass.isZeroPageValue("255"));
        assertTrue(testClass.isZeroPageValue("25"));
        assertTrue(testClass.isZeroPageValue("2"));
        assertTrue(testClass.isZeroPageValue("0"));
    }

    @Test
    public void testIsZeroPageValue_invalid() {
        assertFalse(testClass.isZeroPageValue("$1010"));
        assertFalse(testClass.isZeroPageValue("$100"));
        assertFalse(testClass.isZeroPageValue("256"));
        assertFalse(testClass.isZeroPageValue("-1"));
        assertFalse(testClass.isZeroPageValue("derp"));
    }

    @Test
    public void testIsAbsoluteValue_valid() {
        assertTrue(testClass.isAbsoluteValue("$100"));
        assertTrue(testClass.isAbsoluteValue("$1FF"));
        assertTrue(testClass.isAbsoluteValue("$1111"));
        assertTrue(testClass.isAbsoluteValue("$FFFF"));
        assertTrue(testClass.isAbsoluteValue("256"));
        assertTrue(testClass.isAbsoluteValue("5000"));
        assertTrue(testClass.isAbsoluteValue("65535"));
    }

    @Test
    public void testIsAbsoluteValue_invalid() {
        assertFalse(testClass.isAbsoluteValue("$00FF"));
        assertFalse(testClass.isAbsoluteValue("$0000"));
        assertFalse(testClass.isAbsoluteValue("$FF"));
        assertFalse(testClass.isAbsoluteValue("255"));
        assertFalse(testClass.isAbsoluteValue("-1"));
        assertFalse(testClass.isAbsoluteValue("%11111111"));
        assertFalse(testClass.isAbsoluteValue("derp"));
    }
}