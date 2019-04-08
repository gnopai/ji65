package com.gnopai.ji65.assembler;

import com.gnopai.ji65.DirectiveType;
import com.gnopai.ji65.config.ProgramConfig;
import com.gnopai.ji65.parser.statement.*;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class Assembler implements StatementVisitor {
    private final InstructionAssembler instructionAssembler;
    private final FirstPassResolver firstPassResolver;
    private final DirectiveDataAssembler directiveDataAssembler;
    private AssembledSegments assembledSegments;
    private String currentSegment;

    public Assembler(FirstPassResolver firstPassResolver, InstructionAssembler instructionAssembler, DirectiveDataAssembler directiveDataAssembler) {
        this.instructionAssembler = instructionAssembler;
        this.firstPassResolver = firstPassResolver;
        this.directiveDataAssembler = directiveDataAssembler;
    }

    public AssembledSegments assemble(List<Statement> statements, ProgramConfig programConfig, Environment environment) {
        List<Segment> segments = programConfig.getSegmentConfigs().stream()
                .map(Segment::new)
                .collect(toList());
        assembledSegments = new AssembledSegments(segments, environment);
        firstPassResolver.resolve(statements, assembledSegments);
        statements.forEach(statement -> statement.accept(this));
        return assembledSegments;
    }

    @Override
    public void visit(InstructionStatement instructionStatement) {
        SegmentData segmentData = instructionAssembler.assemble(instructionStatement, assembledSegments.getEnvironment());
        assembledSegments.add(currentSegment, segmentData);
    }

    @Override
    public void visit(LabelStatement labelStatement) {
        Environment environment = assembledSegments.getEnvironment();
        String name = labelStatement.getName();
        Label label = environment.get(name)
                .filter(l -> l instanceof Label)
                .map(l -> (Label) l)
                .orElseThrow(() -> new RuntimeException("Expected label named \"" + name + "\""));
        assembledSegments.add(currentSegment, label);
    }

    @Override
    public void visit(DirectiveStatement directiveStatement) {
        if (directiveStatement.getType() == DirectiveType.SEGMENT) {
            currentSegment = directiveStatement.getName();
            return;
        }

        directiveDataAssembler.assemble(directiveStatement, assembledSegments.getEnvironment())
                .forEach(data -> assembledSegments.add(currentSegment, data));
    }

    @Override
    public void visit(AssignmentStatement assignmentStatement) {
        // first pass only
    }
}
