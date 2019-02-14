package com.gnopai.ji65;

import lombok.Getter;

@Getter
public enum AddressingModeType {
    IMPLICIT(1),
    ACCUMULATOR(1),
    IMMEDIATE(2),
    ZERO_PAGE(2),
    ZERO_PAGE_X(2),
    ZERO_PAGE_Y(2),
    RELATIVE(2),
    ABSOLUTE(3),
    ABSOLUTE_X(3),
    ABSOLUTE_Y(3),
    INDIRECT(3),
    INDEXED_INDIRECT(2),
    INDIRECT_INDEXED(2);

    private final int byteCount;

    AddressingModeType(int byteCount) {
        this.byteCount = byteCount;
    }
}
