package com.gnopai.ji65.config;

import com.gnopai.ji65.Address;
import com.gnopai.ji65.parser.ParseException;
import com.gnopai.ji65.scanner.Token;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Optional;

import static com.gnopai.ji65.scanner.TokenType.*;
import static org.junit.jupiter.api.Assertions.*;

class ConfigSegmentTest {
    private final String name = "name";

    @Test
    void testGetIntValue_valid() {
        ConfigSegment segment = segmentWithValue(name, new Token(NUMBER, "", 1102, 0));
        assertEquals(Optional.of(1102), segment.getIntValue(name));
    }

    @Test
    void testGetIntValue_notPresent() {
        ConfigSegment segment = emptySegment();
        assertFalse(segment.getIntValue(name).isPresent());
    }

    @Test
    void testGetIntValue_notNumber() {
        ConfigSegment segment = segmentWithValue(name, new Token(STRING, "\"derp\"", "derp", 0));
        ParseException parseException = assertThrows(ParseException.class, () -> segment.getIntValue(name));
        assertEquals("Expected numerical value", parseException.getMessage());
        assertEquals(STRING, parseException.getTokenType());
    }

    @Test
    void testGetByteValue_valid() {
        ConfigSegment segment = segmentWithValue(name, new Token(NUMBER, "$FF", 0xFF, 0));
        assertEquals(Optional.of((byte) 0xFF), segment.getByteValue(name));
    }

    @Test
    void testGetByteValue_notPresent() {
        ConfigSegment segment = emptySegment();
        assertFalse(segment.getByteValue(name).isPresent());
    }

    @Test
    void testGetByteValue_notNumber() {
        ConfigSegment segment = segmentWithValue(name, new Token(STRING, "\"derp\"", "derp", 0));
        ParseException parseException = assertThrows(ParseException.class, () -> segment.getByteValue(name));
        assertEquals("Expected numerical value", parseException.getMessage());
        assertEquals(STRING, parseException.getTokenType());
    }

    @Test
    void testGetAddressValue_valid() {
        ConfigSegment segment = segmentWithValue(name, new Token(NUMBER, "$2345", 0x2345, 0));
        assertEquals(Optional.of(new Address(0x2345)), segment.getAddressValue(name));
    }

    @Test
    void testGetAddressValue_notPresent() {
        ConfigSegment segment = emptySegment();
        assertFalse(segment.getAddressValue(name).isPresent());
    }

    @Test
    void testGetAddressValue_notNumber() {
        ConfigSegment segment = segmentWithValue(name, new Token(STRING, "\"derp\"", "derp", 0));
        ParseException parseException = assertThrows(ParseException.class, () -> segment.getAddressValue(name));
        assertEquals("Expected numerical value", parseException.getMessage());
        assertEquals(STRING, parseException.getTokenType());
    }

    @Test
    void testGetStringValue_validFromString() {
        ConfigSegment segment = segmentWithValue(name, new Token(STRING, "\"derp\"", "derp", 0));
        assertEquals(Optional.of("derp"), segment.getStringValue(name));
    }

    @Test
    void testGetStringValue_validFromIdentifier() {
        ConfigSegment segment = segmentWithValue(name, new Token(IDENTIFIER, "derp", null, 0));
        assertEquals(Optional.of("derp"), segment.getStringValue(name));
    }

    @Test
    void testGetStringValue_notPresent() {
        ConfigSegment segment = emptySegment();
        assertFalse(segment.getStringValue(name).isPresent());
    }

    @Test
    void testGetStringValue_notString() {
        ConfigSegment segment = segmentWithValue(name, new Token(NUMBER, "45", 45, 0));
        ParseException parseException = assertThrows(ParseException.class, () -> segment.getStringValue(name));
        assertEquals("Expected string value", parseException.getMessage());
        assertEquals(NUMBER, parseException.getTokenType());
    }

    @Test
    void testGetValueUsingMapper_valid() {
        ConfigSegment segment = segmentWithValue(name, new Token(IDENTIFIER, "ro", null, 0));
        Optional<MemoryType> enumValue = segment.getValue(name, MemoryType::fromName);
        assertEquals(Optional.of(MemoryType.READ_ONLY), enumValue);
    }

    @Test
    void testGetValueUsingMapper_notPresent() {
        ConfigSegment segment = emptySegment();
        assertFalse(segment.getValue(name, MemoryType::fromName).isPresent());
    }

    @Test
    void testGetValueUsingMapper_invalid() {
        ConfigSegment segment = segmentWithValue(name, new Token(STRING, "\"derp\"", "derp", 0));
        ParseException parseException = assertThrows(ParseException.class, () -> segment.getValue(name, MemoryType::fromName));
        assertEquals("Invalid value", parseException.getMessage());
        assertEquals(STRING, parseException.getTokenType());
    }

    @Test
    void testGetBooleanValue_validAndTrue() {
        ConfigSegment segment = segmentWithValue(name, new Token(IDENTIFIER, "yes", null, 0));
        assertEquals(Optional.of(true), segment.getBooleanValue(name));
    }

    @Test
    void testGetBooleanValue_validAndFalse() {
        ConfigSegment segment = segmentWithValue(name, new Token(IDENTIFIER, "nope", null, 0));
        assertEquals(Optional.of(false), segment.getBooleanValue(name));
    }

    @Test
    void testGetBooleanValue_notPresent() {
        ConfigSegment segment = emptySegment();
        assertFalse(segment.getBooleanValue(name).isPresent());
    }

    @Test
    void testGetBooleanValue_notString() {
        ConfigSegment segment = segmentWithValue(name, new Token(NUMBER, "45", 45, 0));
        ParseException parseException = assertThrows(ParseException.class, () -> segment.getBooleanValue(name));
        assertEquals("Expected string value", parseException.getMessage());
        assertEquals(NUMBER, parseException.getTokenType());
    }

    private ConfigSegment segmentWithValue(String name, Token value) {
        return new ConfigSegment("segment", Map.of(name, value));
    }

    private ConfigSegment emptySegment() {
        return new ConfigSegment("segment", Map.of());
    }
}