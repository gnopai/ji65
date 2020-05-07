package com.gnopai.ji65;

import lombok.Getter;

import java.util.*;

import static java.util.Optional.ofNullable;

@Getter
public enum DirectiveType {
    SEGMENT("segment"),
    RESERVE("res"),
    WORD("word"),
    BYTE("byte", "byt"),
    REPEAT("repeat"),
    REPEAT_END("endrepeat", "endrep"),
    MACRO("macro", "mac"),
    MACRO_END("endmacro", "endmac"),
    INCLUDE("include"),
    INCLUDE_BINARY("incbin"),
    TEST("test"),
    TEST_END("endtest"),
    TEST_SET("testset"),
    TEST_RUN("testrun"),
    TEST_ASSERT("assert"),
    TEST_MOCK("testmock"),
    TEST_VERIFY_WRITE("verifywrite"),
    TEST_VERIFY_READ("verifyread"),
    ;

    private final List<String> identifiers;

    DirectiveType(String... identifiers) {
        this.identifiers = List.of(identifiers);
    }

    private static final Map<String, DirectiveType> TYPES_BY_IDENTIFIER = buildTypesByIdentifier();

    private static Map<String, DirectiveType> buildTypesByIdentifier() {
        Map<String, DirectiveType> map = new HashMap<>();
        Arrays.stream(values()).forEach(directiveType ->
                directiveType.getIdentifiers().forEach(
                        identifier -> map.put(identifier, directiveType)
                )
        );
        return Map.copyOf(map);
    }

    public static Optional<DirectiveType> fromIdentifier(String string) {
        return ofNullable(TYPES_BY_IDENTIFIER.get(string.toLowerCase()));
    }
}
