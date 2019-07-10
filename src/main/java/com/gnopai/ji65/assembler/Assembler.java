package com.gnopai.ji65.assembler;

import com.gnopai.ji65.DirectiveType;
import com.gnopai.ji65.config.ProgramConfig;
import com.gnopai.ji65.parser.statement.*;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class Assembler implements StatementVisitor<Void> {
    private final InstructionAssembler instructionAssembler;
    private final FirstPassResolver firstPassResolver;
    private final DirectiveDataAssembler directiveDataAssembler;
    private AssembledSegments assembledSegments;
    private String currentSegment;
    private Environment environment;

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
        this.environment = environment;
        firstPassResolver.resolve(statements, assembledSegments);
        environment.goToRootScope();
        statements.forEach(statement -> statement.accept(this));
        return assembledSegments;
    }

    @Override
    public Void visit(InstructionStatement instructionStatement) {
        SegmentData segmentData = instructionAssembler.assemble(instructionStatement, environment);
        assembledSegments.add(currentSegment, segmentData);
        return null;
    }

    @Override
    public Void visit(LabelStatement labelStatement) {
        Label label = environment.getLabel(labelStatement.getName());
        assembledSegments.add(currentSegment, label);
        environment.goToRootScope().enterChildScope(labelStatement.getName());
        return null;
    }

    @Override
    public Void visit(LocalLabelStatement localLabelStatement) {
        Label label = environment.getLabel(localLabelStatement.getName());
        assembledSegments.add(currentSegment, label);
        return null;
    }

    @Override
    public Void visit(DirectiveStatement directiveStatement) {
        if (directiveStatement.getType() == DirectiveType.SEGMENT) {
            currentSegment = directiveStatement.getName();
            environment.goToRootScope();
            return null;
        }

        directiveDataAssembler.assemble(directiveStatement, environment)
                .forEach(data -> assembledSegments.add(currentSegment, data));
        return null;
    }

    @Override
    public Void visit(AssignmentStatement assignmentStatement) {
        // first pass only
        return null;
    }
}
