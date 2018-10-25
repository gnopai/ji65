package com.gnopai.ji65.parser;

import com.gnopai.ji65.Address;
import com.gnopai.ji65.Operand;

import java.util.HashMap;
import java.util.Map;

import static java.util.Optional.ofNullable;

public class ParsingData {
    private final Map<Address, UnresolvedInstruction> instructions;
    private final Map<String, Address> labels;
    private final Map<String, Operand> constants;

    public ParsingData() {
        instructions = new HashMap<>();
        labels = new HashMap<>();
        constants = new HashMap<>();
    }

    public boolean isZeroPageSymbol(String symbol) {
        return ofNullable(labels.get(symbol))
                .map(Address::isZeroPage)
                .orElse(false);
    }

    public boolean isAddressLabel(String symbol) {
        return labels.containsKey(symbol);
    }

    public boolean isConstant(String symbol) {
        return constants.containsKey(symbol);
    }
}
