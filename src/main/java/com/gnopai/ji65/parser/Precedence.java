package com.gnopai.ji65.parser;

public enum Precedence {
    // Note that the order here controls the actual precedence order, where they go from first/lowest to last/highest
    NONE,
    ASSIGNMENT,
    BOOLEAN_NOT,
    BOOLEAN_OR,
    BOOLEAN_AND,
    COMPARISON,
    SUM,
    BITWISE,
    MULTIPLY,
    UNARY,
    PRIMARY,
    ;

    public boolean isHigherThan(Precedence other) {
        return ordinal() > other.ordinal();
    }

    public boolean isLowerThan(Precedence other) {
        return ordinal() < other.ordinal();
    }

    public static Precedence lowest() {
        return values()[0];
    }
}
