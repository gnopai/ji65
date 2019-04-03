package com.gnopai.ji65.assembler;

import com.gnopai.ji65.config.ProgramConfig;
import com.gnopai.ji65.directive.DirectiveType;
import com.gnopai.ji65.parser.statement.*;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class Assembler implements StatementVisitor {
    private final InstructionAssembler instructionAssembler;
    private final FirstPassResolver firstPassResolver;
    private AssembledSegments assembledSegments;
    private String currentSegment;

    public Assembler(FirstPassResolver firstPassResolver, InstructionAssembler instructionAssembler) {
        this.instructionAssembler = instructionAssembler;
        this.firstPassResolver = firstPassResolver;
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
        if (DirectiveType.SEGMENT.equals(directiveStatement.getType())) {
            currentSegment = directiveStatement.getName();
        }
    }

    @Override
    public void visit(AssignmentStatement assignmentStatement) {
        // first pass only
    }
}
