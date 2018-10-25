package com.gnopai.ji65.parser;

import java.util.Optional;

import static java.lang.Integer.parseInt;

public class NumberParser {

    public Optional<Integer> parseValue(String input) {
        if (isValidHexValue(input)) {
            return Optional.of(parseHexValue(input));
        }
        if (isValidBinaryValue(input)) {
            return Optional.of(parseBinaryValue(input));
        }
        if (isValidDecimalValue(input)) {
            return Optional.of(parseDecimalValue(input));
        }
        return Optional.empty();
    }

    private boolean isValidHexValue(String input) {
        return input.matches("\\$[0-9a-fA-F]{1,4}");
    }

    private boolean isValidBinaryValue(String input) {
        return input.matches("%[01]{8}");
    }

    private boolean isValidDecimalValue(String input) {
        if (!input.matches("\\d+")) {
            return false;
        }
        int value = parseInt(input);
        return 0 <= value && value < 65536;
    }

    private int parseHexValue(String input) {
        return parseInt(input.substring(1), 16);
    }

    private int parseBinaryValue(String input) {
        return parseInt(input.substring(1), 2);
    }

    private int parseDecimalValue(String input) {
        return parseInt(input);
    }

    public boolean isZeroPageValue(String input) {
        return parseValue(input)
                .map(value -> value < 256)
                .orElse(false);
    }

    public boolean isAbsoluteValue(String input) {
        return parseValue(input)
                .map(value -> value >= 256)
                .orElse(false);
    }

    public boolean isValidValue(String input) {
        return parseValue(input).isPresent();
    }
}
