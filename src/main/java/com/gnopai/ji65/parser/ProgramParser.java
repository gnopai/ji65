package com.gnopai.ji65.parser;

import com.gnopai.ji65.Program;
import com.gnopai.ji65.RuntimeInstruction;

import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toUnmodifiableList;

public class ProgramParser {
    private final InstructionParser instructionParser;
    private final InstructionResolver instructionResolver;

    public ProgramParser(InstructionParser instructionParser, InstructionResolver instructionResolver) {
        this.instructionParser = instructionParser;
        this.instructionResolver = instructionResolver;
    }

    // TODO this is super-over-simplifying the whole multi-pass parsing process at the moment
    // TODO handle unparseable lines (just ignored for now)
    public Program parseProgram(List<String> lines) {
        ParsingData parsingData = new ParsingData();
        List<RuntimeInstruction> runtimeInstructions = lines.stream()
                .map(instruction -> instructionParser.parseInstruction(instruction, parsingData))
                .flatMap(Optional::stream)
                .map(unresolvedInstruction -> instructionResolver.resolve(unresolvedInstruction, parsingData))
                .collect(toUnmodifiableList());
        return new Program(runtimeInstructions);
    }
}
