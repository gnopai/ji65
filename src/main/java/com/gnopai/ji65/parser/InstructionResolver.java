package com.gnopai.ji65.parser;

import com.gnopai.ji65.BytesValue;
import com.gnopai.ji65.RuntimeInstruction;

import java.util.Optional;

public class InstructionResolver {
    private final NumberParser numberParser;

    public InstructionResolver(NumberParser numberParser) {
        this.numberParser = numberParser;
    }

    public RuntimeInstruction resolve(UnresolvedInstruction instruction, ParsingData parsingData) {
        BytesValue argumentBytes = resolveArgument(instruction.getArgument(), parsingData);
        return new RuntimeInstruction(instruction.getOpcode(), argumentBytes);
    }

    private BytesValue resolveArgument(String argument, ParsingData parsingData) {
        // TODO resolve labels/value into addresses, maybe handle address arithmetic here too?
        // TODO multi-byte values
        Optional<Integer> value = numberParser.parseValue(argument);
        byte byteValue = (byte) (value.orElse(0)).intValue();
        return new BytesValue(byteValue, (byte) 0x00);
    }
}
