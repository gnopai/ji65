package com.gnopai.ji65.parser;

import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.lang.Integer.parseInt;

public class NumberParser {

    public Optional<Integer> parseValue(String input) {
        return Stream.<Function<String, Optional<Integer>>>of(
                this::parseHexValue,
                this::parseBinaryValue,
                this::parseDecimalValue)
                .map(function -> function.apply(input))
                .flatMap(Optional::stream)
                .findFirst();
    }

    private Optional<Integer> parseHexValue(String input) {
        return Optional.of(input)
                .filter(value -> value.matches("\\$[0-9a-fA-F]{1,4}"))
                .map(value -> parseInt(value.substring(1), 16));
    }

    private Optional<Integer> parseBinaryValue(String input) {
        return Optional.of(input)
                .filter(value -> value.matches("%[01]{8}"))
                .map(value -> parseInt(value.substring(1), 2));
    }

    private Optional<Integer> parseDecimalValue(String input) {
        return Optional.of(input)
                .filter(value -> value.matches("\\d+"))
                .map(Integer::parseInt)
                .filter(value -> 0 <= value && value < 65536);
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
