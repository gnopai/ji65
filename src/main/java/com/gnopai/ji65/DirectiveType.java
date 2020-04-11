package com.gnopai.ji65;

import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toUnmodifiableMap;

@Getter
public enum DirectiveType {
    SEGMENT("segment"),
    RESERVE("res"),
    WORD("word"),
    BYTE("byte"),
    REPEAT("repeat"),
    REPEAT_END("endrep"),
    MACRO("macro"),
    MACRO_END("endmacro"),
    INCLUDE("include"),
    INCLUDE_BINARY("incbin"),
    TEST("test"),
    TEST_END("endtest"),
    TEST_SET("testset"),
    TEST_RUN("testrun"),
    TEST_ASSERT("assert"),
    ;

    private final String identifier;

    DirectiveType(String identifier) {
        this.identifier = identifier;
    }

    private static final Map<String, DirectiveType> TYPES_BY_IDENTIFIER =
            Arrays.stream(values())
                    .collect(toUnmodifiableMap(
                            DirectiveType::getIdentifier,
                            type -> type)
                    );

    public static Optional<DirectiveType> fromIdentifier(String string) {
        return ofNullable(TYPES_BY_IDENTIFIER.get(string.toLowerCase()));
    }
}
