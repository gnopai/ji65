package com.gnopai.ji65.parser;

import com.gnopai.ji65.Opcode;
import com.gnopai.ji65.address.AddressingModeType;
import com.gnopai.ji65.instruction.InstructionType;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import lombok.Value;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

import static com.gnopai.ji65.address.AddressingModeType.*;

public class InstructionParser {
    private final NumberParser numberParser;

    public InstructionParser(NumberParser numberParser) {
        this.numberParser = numberParser;
    }

    public Optional<UnresolvedInstruction> parseInstruction(String line, ParsingData parsingData) {
        List<String> stringTokens = Splitter.on(" ").omitEmptyStrings().splitToList(line);
        String instructionTypeString = stringTokens.get(0);
        String argument = Joiner.on("").join(stringTokens.subList(1, stringTokens.size()));
        return InstructionType.fromName(instructionTypeString)
                .map(instructionType -> parseInstruction(line, instructionType, argument, parsingData));
    }

    private UnresolvedInstruction parseInstruction(String line, InstructionType instructionType, String argument, ParsingData parsingData) {
        ParseValues parseValues = new ParseValues(line, instructionType, argument, parsingData);
        return Stream.<Function<ParseValues, Optional<UnresolvedInstruction>>>of(
                this::findImplicitInstruction,
                this::findAccumulatorInstruction,
                this::findRelativeInstruction,
                this::findImmediateInstruction,
                this::findZeroPageAddressInstruction,
                this::findAbsoluteAddressInstruction,
                this::findXIndexedInstruction,
                this::findYIndexedInstruction,
                this::findIndirectInstruction)
                .map(function -> function.apply(parseValues))
                .flatMap(Optional::stream)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Invalid instruction argument: '" + argument + "'"));
    }

    private Optional<UnresolvedInstruction> findImplicitInstruction(ParseValues parseValues) {
        if (!parseValues.getArgument().isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(
                makeInstruction(parseValues.getLine(), parseValues.getInstructionType(), IMPLICIT, null)
        );
    }

    private Optional<UnresolvedInstruction> findAccumulatorInstruction(ParseValues parseValues) {
        if (!parseValues.getArgument().equalsIgnoreCase("A")) {
            return Optional.empty();
        }
        return Optional.of(
                makeInstruction(parseValues.getLine(), parseValues.getInstructionType(), ACCUMULATOR, null)
        );
    }

    private Optional<UnresolvedInstruction> findRelativeInstruction(ParseValues parseValues) {
        if (!isRelative(parseValues)) {
            return Optional.empty();
        }
        return Optional.of(
                makeInstruction(parseValues.getLine(), parseValues.getInstructionType(), RELATIVE, parseValues.getArgument())
        );
    }

    private boolean isRelative(ParseValues parseValues) {
        if (!parseValues.getInstructionType().isBranchInstruction()) {
            return false;
        }

        if (parseValues.getParsingData().isAddressLabel(parseValues.getArgument())) {
            return true;
        }

        // TODO maybe this shouldn't be handled here? Also note that this is more complicated with "*" being the current PC value (and optional)
        return parseValues.getArgument().matches("\\*[+-]\\d{1,3}");
    }

    private Optional<UnresolvedInstruction> findImmediateInstruction(ParseValues parseValues) {
        if (!isImmediate(parseValues.getArgument(), parseValues.getParsingData())) {
            return Optional.empty();
        }
        return Optional.of(
                makeInstruction(parseValues.getLine(), parseValues.getInstructionType(), IMMEDIATE, parseValues.getArgument().replaceFirst("#", ""))
        );
    }

    private boolean isImmediate(String input, ParsingData parsingData) {
        if (!input.startsWith("#")) {
            return false;
        }
        String value = input.substring(1);
        return numberParser.isValidSingleByteValue(value) || parsingData.isConstant(value);
    }

    private Optional<UnresolvedInstruction> findZeroPageAddressInstruction(ParseValues parseValues) {
        if (!isZeroPageAddress(parseValues.getArgument(), parseValues.getParsingData())) {
            return Optional.empty();
        }
        return Optional.of(
                makeInstruction(parseValues.getLine(), parseValues.getInstructionType(), ZERO_PAGE, parseValues.getArgument())
        );
    }

    private Optional<UnresolvedInstruction> findAbsoluteAddressInstruction(ParseValues parseValues) {
        if (!isAbsoluteAddress(parseValues.getArgument(), parseValues.getParsingData())) {
            return Optional.empty();
        }
        return Optional.of(
                makeInstruction(parseValues.getLine(), parseValues.getInstructionType(), ABSOLUTE, parseValues.getArgument())
        );
    }

    private Optional<UnresolvedInstruction> findXIndexedInstruction(ParseValues parseValues) {
        if (!isXIndexedAddress(parseValues.getArgument())) {
            return Optional.empty();
        }

        String indexArgument = getIndexedAddressArgument(parseValues.getArgument());
        if (isZeroPageAddress(indexArgument, parseValues.getParsingData())) {
            return Optional.of(makeInstruction(parseValues.getLine(), parseValues.getInstructionType(), ZERO_PAGE_X, indexArgument));
        }
        if (isAbsoluteAddress(indexArgument, parseValues.getParsingData())) {
            return Optional.of(makeInstruction(parseValues.getLine(), parseValues.getInstructionType(), ABSOLUTE_X, indexArgument));
        }
        return Optional.empty();
    }

    private Optional<UnresolvedInstruction> findYIndexedInstruction(ParseValues parseValues) {
        if (!isYIndexedAddress(parseValues.getArgument())) {
            return Optional.empty();
        }

        String indexArgument = getIndexedAddressArgument(parseValues.getArgument());
        if (isZeroPageAddress(indexArgument, parseValues.getParsingData())) {
            return Optional.of(makeInstruction(parseValues.getLine(), parseValues.getInstructionType(), ZERO_PAGE_Y, indexArgument));
        }
        if (isAbsoluteAddress(indexArgument, parseValues.getParsingData())) {
            return Optional.of(makeInstruction(parseValues.getLine(), parseValues.getInstructionType(), ABSOLUTE_Y, indexArgument));
        }
        if (isIndirectAddress(indexArgument)) {
            String indirectIndexedArgument = getIndirectAddressArgument(indexArgument);
            if (isAbsoluteAddress(indirectIndexedArgument, parseValues.getParsingData())) {
                return Optional.of(makeInstruction(parseValues.getLine(), parseValues.getInstructionType(), INDIRECT_INDEXED, indirectIndexedArgument));
            }
        }
        return Optional.empty();
    }

    private Optional<UnresolvedInstruction> findIndirectInstruction(ParseValues parseValues) {
        if (!isIndirectAddress(parseValues.getArgument())) {
            return Optional.empty();
        }

        String indirectArgument = getIndirectAddressArgument(parseValues.getArgument());
        if (isXIndexedAddress(indirectArgument)) {
            String indexedIndirectArgument = getIndexedAddressArgument(indirectArgument);
            if (isAbsoluteAddress(indexedIndirectArgument, parseValues.getParsingData())) {
                return Optional.of(makeInstruction(parseValues.getLine(), parseValues.getInstructionType(), INDEXED_INDIRECT, indexedIndirectArgument));
            }
        }
        if (isAbsoluteAddress(indirectArgument, parseValues.getParsingData())) {
            return Optional.of(makeInstruction(parseValues.getLine(), parseValues.getInstructionType(), INDIRECT, indirectArgument));
        }
        return Optional.empty();
    }

    private UnresolvedInstruction makeInstruction(String line, InstructionType instructionType, AddressingModeType addressingModeType, String argument) {
        return Opcode.of(instructionType, addressingModeType)
                .map(opcode -> new UnresolvedInstruction(opcode, argument))
                .orElseThrow(() -> new RuntimeException("Invalid instruction line: '" + line + "'"));
    }

    private boolean isZeroPageAddress(String input, ParsingData parsingData) {
        return numberParser.isZeroPageValue(input) || parsingData.isZeroPageSymbol(input);
    }

    private boolean isAbsoluteAddress(String input, ParsingData parsingData) {
        return numberParser.isAbsoluteValue(input) || parsingData.isAddressLabel(input);
    }

    private boolean isXIndexedAddress(String input) {
        return input.toUpperCase().endsWith(",X");
    }

    private boolean isYIndexedAddress(String input) {
        return input.toUpperCase().endsWith(",Y");
    }

    private String getIndexedAddressArgument(String input) {
        return input.replaceFirst(",[xyXY]$", "");
    }

    private boolean isIndirectAddress(String input) {
        return input.matches("\\(.*\\)");
    }

    private String getIndirectAddressArgument(String input) {
        return input.substring(1, input.length() - 1);
    }

    @Value
    private static class ParseValues {
        String line;
        InstructionType instructionType;
        String argument;
        ParsingData parsingData;
    }
}
