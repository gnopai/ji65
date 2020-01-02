package com.gnopai.ji65.linker;

import com.gnopai.ji65.Address;
import com.gnopai.ji65.Program;
import com.gnopai.ji65.assembler.*;
import com.gnopai.ji65.config.ProgramConfig;
import com.gnopai.ji65.parser.expression.ExpressionEvaluator;
import com.gnopai.ji65.test.Test;
import com.gnopai.ji65.test.TestMaker;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;

public class Linker implements SegmentDataVisitor {
    private final SegmentMapper segmentMapper;
    private final LabelResolver labelResolver;
    private final ExpressionEvaluator expressionEvaluator;
    private final TestMaker testMaker;
    private ProgramBuilder programBuilder;
    private Environment environment;

    @Inject
    public Linker(SegmentMapper segmentMapper, LabelResolver labelResolver, ExpressionEvaluator expressionEvaluator, TestMaker testMaker) {
        this.segmentMapper = segmentMapper;
        this.labelResolver = labelResolver;
        this.expressionEvaluator = expressionEvaluator;
        this.testMaker = testMaker;
    }

    public Program link(AssembledSegments assembledSegments, ProgramConfig programConfig) {
        this.environment = assembledSegments.getEnvironment();
        MappedSegments mappedSegments = segmentMapper.mapSegments(programConfig, assembledSegments.getSegments());
        labelResolver.resolve(mappedSegments, environment);

        environment.goToRootScope();
        programBuilder = new ProgramBuilder();
        mappedSegments.getSegments().forEach(this::link);
        return programBuilder.build();
    }

    private void link(MappedSegment segment) {
        programBuilder.segment(segment);
        segment.getData().forEach(this::link);
    }

    private void link(SegmentData segmentData) {
        segmentData.accept(this);
    }

    @Override
    public void visit(InstructionData instructionData) {
        updateEnvironmentProgramAddress(); // TODO do this elsewhere? Right???
        programBuilder.bytes(instructionData.getOpcode().getOpcode());
        link(instructionData.getOperand());
    }

    @Override
    public void visit(RawData rawData) {
        programBuilder.bytes(rawData.getBytes());
    }

    @Override
    public void visit(Label label) {
        if (label.isNamed() && !label.isLocal()) {
            int address = expressionEvaluator.evaluate(label, environment); // FIXME not really an expression, just a weird thing
            programBuilder.label(label.getName(), address);
            environment.goToRootScope().enterChildScope(label.getName());
        }
    }

    @Override
    public void visit(UnresolvedExpression unresolvedExpression) {
        int value = expressionEvaluator.evaluate(unresolvedExpression.getExpression(), environment);
        if (unresolvedExpression.isSingleByte()) {
            programBuilder.bytes((byte) value);
        } else {
            Address address = new Address(value);
            programBuilder.bytes(List.of(address.getLowByte(), address.getHighByte()));
        }
    }

    @Override
    public void visit(RelativeUnresolvedExpression relativeUnresolvedExpression) {
        int operandValue = expressionEvaluator.evaluate(relativeUnresolvedExpression.getExpression(), environment);
        int indexOfInstructionEnd = programBuilder.getCurrentIndex() + 1;
        int relativeOffset = operandValue - indexOfInstructionEnd;
        programBuilder.bytes((byte) relativeOffset);
    }

    @Override
    public void visit(ReservedData reservedData) {
        List<Byte> bytes = IntStream.range(0, reservedData.getSize())
                .mapToObj(i -> (byte) 0)
                .collect(toList());
        programBuilder.bytes(bytes);
    }

    @Override
    public void visit(TestData testData) {
        Test test = testMaker.makeTest(testData, environment);
        programBuilder.test(test);
    }

    private void updateEnvironmentProgramAddress() {
        environment.setCurrentAddress(programBuilder.getCurrentIndex());
    }
}
