package com.gnopai.ji65.linker;

import com.gnopai.ji65.Address;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class UnnamedLabelsTest {

    @ParameterizedTest
    @MethodSource("addArgumentsProvider")
    void testAdd(List<Integer> testValues, List<Integer> expected) {
        UnnamedLabels unnamedLabels = new UnnamedLabels();
        for (Integer value : testValues) {
            unnamedLabels.add(value);
        }
        assertEquals(expected, unnamedLabels.getAll());
    }

    static Stream<Arguments> addArgumentsProvider() {
        return Stream.of(
                arguments(List.of(), List.of()),
                arguments(List.of(1), List.of(1)),
                arguments(List.of(2, 1), List.of(1, 2)),
                arguments(List.of(1, 2, 3), List.of(1, 2, 3)),
                arguments(List.of(1, 2, 1, 1, 2), List.of(1, 1, 1, 2, 2)),
                arguments(List.of(9, 4, 2, 6, 3, 55, 0, 3), List.of(0, 2, 3, 3, 4, 6, 9, 55)),
                arguments(List.of(1, 2, 3, 1, 1, 2, 2, 2, 3, 3, 4), List.of(1, 1, 1, 2, 2, 2, 2, 3, 3, 3, 4))
        );
    }

    @ParameterizedTest
    @MethodSource("getArgumentsProvider")
    void testGet(List<Integer> values, int startAddress, int offset, Optional<Integer> expected) {
        UnnamedLabels unnamedLabels = new UnnamedLabels();
        for (Integer value : values) {
            unnamedLabels.add(value);
        }

        Optional<Integer> result = unnamedLabels.getFromAddress(new Address(startAddress), offset);
        assertEquals(expected, result);
    }

    static Stream<Arguments> getArgumentsProvider() {
        List<Integer> values = List.of(1, 3, 5, 5, 7, 9, 10, 10, 10, 11);
        return Stream.of(
                arguments(List.of(), 0, 1, Optional.empty()),
                arguments(values, 0, 1, Optional.of(1)),
                arguments(values, 0, 2, Optional.of(3)),
                arguments(values, 1, 1, Optional.of(3)),
                arguments(values, 1, 2, Optional.of(5)),
                arguments(values, 4, 1, Optional.of(5)),
                arguments(values, 4, 2, Optional.of(5)),
                arguments(values, 4, 3, Optional.of(7)),
                arguments(values, 5, 1, Optional.of(7)),
                arguments(values, 5, 2, Optional.of(9)),
                arguments(values, 10, 1, Optional.of(11)),
                arguments(values, 10, 4, Optional.empty()),
                arguments(values, 12, 1, Optional.empty()),
                arguments(values, 1, 0, Optional.empty()),
                arguments(values, 8, -1, Optional.of(7)),
                arguments(values, 8, -2, Optional.of(5)),
                arguments(values, 8, -3, Optional.of(5)),
                arguments(values, 8, -4, Optional.of(3)),
                arguments(values, 12, -1, Optional.of(11)),
                arguments(values, 0, -1, Optional.empty())
        );
    }
}