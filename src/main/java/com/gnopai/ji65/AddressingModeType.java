package com.gnopai.ji65;

import lombok.Getter;

@Getter
public enum AddressingModeType {
    IMPLICIT(1),
    ACCUMULATOR(1), // TODO
    IMMEDIATE(2),
    ZERO_PAGE(2),
    ZERO_PAGE_X(2),
    ZERO_PAGE_Y(2),
    RELATIVE(2), // TODO
    ABSOLUTE(3), // TODO
    ABSOLUTE_X(3), // TODO
    ABSOLUTE_Y(3), // TODO
    INDIRECT(3), // TODO
    INDEXED_INDIRECT(2), // TODO
    INDIRECT_INDEXED(2); // TODO

    private final int byteCount;

    AddressingModeType(int byteCount) {
        this.byteCount = byteCount;
    }
}
