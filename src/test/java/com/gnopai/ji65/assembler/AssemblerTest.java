package com.gnopai.ji65.assembler;

import com.gnopai.ji65.DirectiveType;
import com.gnopai.ji65.InstructionType;
import com.gnopai.ji65.config.ProgramConfig;
import com.gnopai.ji65.config.SegmentConfig;
import com.gnopai.ji65.parser.statement.*;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.gnopai.ji65.assembler.AssembledSegments.DEFAULT_SEGMENT_NAME;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class AssemblerTest {
    private final InstructionAssembler instructionAssembler = mock(InstructionAssembler.class);
    private final FirstPassResolver firstPassResolver = mock(FirstPassResolver.class);
    private final DirectiveDataAssembler directiveDataAssembler = mock(DirectiveDataAssembler.class);

    private final SegmentConfig segmentConfig = SegmentConfig.builder().segmentName(DEFAULT_SEGMENT_NAME).build();
    private final ProgramConfig programConfig = ProgramConfig.builder()
            .segmentConfigs(List.of(segmentConfig))
            .build();
    private final Environment environment = new Environment();

    @Test
    void testAssemble() {
        Statement statement1 = mock(Statement.class);
        Statement statement2 = mock(Statement.class);
        Statement statement3 = mock(Statement.class);
        List<Statement> statements = List.of(statement1, statement2, statement3);

        Assembler assembler = new Assembler(firstPassResolver, instructionAssembler, directiveDataAssembler);

        AssembledSegments result = assembler.assemble(statements, programConfig, environment);

        List<Segment> expectedSegments = List.of(new Segment(segmentConfig));
        AssembledSegments assembledSegments = new AssembledSegments(expectedSegments, environment);
        assertEquals(assembledSegments, result);
        verify(firstPassResolver).resolve(statements, assembledSegments);
        verify(statement1).accept(assembler);
        verify(statement2).accept(assembler);
        verify(statement3).accept(assembler);
    }

    @Test
    void testLabelStatement() {
        LabelStatement statement = new LabelStatement("derp");
        List<Statement> statements = List.of(statement);

        Label expectedLabel = new Label("derp");
        environment.define("derp", expectedLabel);

        Assembler assembler = new Assembler(firstPassResolver, instructionAssembler, directiveDataAssembler);

        AssembledSegments result = assembler.assemble(statements, programConfig, environment);

        List<Segment> expectedSegments = List.of(new Segment(segmentConfig, List.of(expectedLabel)));
        AssembledSegments assembledSegments = new AssembledSegments(expectedSegments, environment);
        assertEquals(assembledSegments, result);
        verify(firstPassResolver).resolve(statements, assembledSegments);
    }

    @Test
    void testLocalLabelStatement() {
        String parentLabelName = "parent";
        String localLabelName = "local";
        LabelStatement labelStatement = new LabelStatement(parentLabelName);
        LocalLabelStatement localLabelStatement = new LocalLabelStatement(localLabelName);
        List<Statement> statements = List.of(labelStatement, localLabelStatement);

        Label parentLabel = new Label(parentLabelName);
        environment.define(parentLabelName, parentLabel);

        Label localLabel = new Label(localLabelName);
        environment.enterChildScope(parentLabelName);
        environment.define(localLabelName, localLabel);

        Assembler assembler = new Assembler(firstPassResolver, instructionAssembler, directiveDataAssembler);

        AssembledSegments result = assembler.assemble(statements, programConfig, environment);

        List<Segment> expectedSegments = List.of(new Segment(segmentConfig, List.of(parentLabel, localLabel)));
        AssembledSegments assembledSegments = new AssembledSegments(expectedSegments, environment);
        assertEquals(assembledSegments, result);
        verify(firstPassResolver).resolve(statements, assembledSegments);
    }

    @Test
    void testLocalLabelStatement_noParent() {
        String localLabelName = "local";
        LocalLabelStatement localLabelStatement = new LocalLabelStatement(localLabelName);
        List<Statement> statements = List.of(localLabelStatement);

        Label localLabel = new Label(localLabelName);
        environment.define(localLabelName, localLabel);

        Assembler assembler = new Assembler(firstPassResolver, instructionAssembler, directiveDataAssembler);

        AssembledSegments result = assembler.assemble(statements, programConfig, environment);

        List<Segment> expectedSegments = List.of(new Segment(segmentConfig, List.of(localLabel)));
        AssembledSegments assembledSegments = new AssembledSegments(expectedSegments, environment);
        assertEquals(assembledSegments, result);
        verify(firstPassResolver).resolve(statements, assembledSegments);
    }

    @Test
    void testDirectiveStatement_segment() {
        DirectiveStatement directiveStatement = DirectiveStatement.builder()
                .type(DirectiveType.SEGMENT)
                .name("HRM")
                .build();

        InstructionStatement statement1 = InstructionStatement.builder().instructionType(InstructionType.LDA).build();
        InstructionStatement statement2 = InstructionStatement.builder().instructionType(InstructionType.LDX).build();
        InstructionStatement statement3 = InstructionStatement.builder().instructionType(InstructionType.LDY).build();
        SegmentData data1 = new RawData((byte) 1);
        SegmentData data2 = new RawData((byte) 2);
        SegmentData data3 = new RawData((byte) 3);
        when(instructionAssembler.assemble(statement1, environment)).thenReturn(data1);
        when(instructionAssembler.assemble(statement2, environment)).thenReturn(data2);
        when(instructionAssembler.assemble(statement3, environment)).thenReturn(data3);

        List<Statement> statements = List.of(statement1, statement2, directiveStatement, statement3);

        SegmentConfig segmentConfig1 = SegmentConfig.builder().segmentName(DEFAULT_SEGMENT_NAME).build();
        SegmentConfig segmentConfig2 = SegmentConfig.builder().segmentName("HRM").build();
        ProgramConfig programConfig = ProgramConfig.builder()
                .segmentConfigs(List.of(segmentConfig1, segmentConfig2))
                .build();

        Assembler assembler = new Assembler(firstPassResolver, instructionAssembler, directiveDataAssembler);

        AssembledSegments result = assembler.assemble(statements, programConfig, environment);

        List<Segment> expectedSegments = List.of(
                new Segment(segmentConfig1, List.of(data1, data2)),
                new Segment(segmentConfig2, List.of(data3))
        );
        AssembledSegments assembledSegments = new AssembledSegments(expectedSegments, environment);
        assertEquals(assembledSegments, result);
        verify(firstPassResolver).resolve(statements, assembledSegments);
    }

    @Test
    void testDirectiveStatement_allOthers() {
        DirectiveStatement directiveStatement = DirectiveStatement.builder()
                .type(DirectiveType.RESERVE)
                .build();
        List<SegmentData> segmentData = List.of(new ReservedData(55), new ReservedData(66));
        when(directiveDataAssembler.assemble(directiveStatement, environment)).thenReturn(segmentData);

        List<Statement> statements = List.of(directiveStatement);

        Assembler assembler = new Assembler(firstPassResolver, instructionAssembler, directiveDataAssembler);

        AssembledSegments result = assembler.assemble(statements, programConfig, environment);

        List<Segment> expectedSegments = List.of(
                new Segment(segmentConfig, segmentData)
        );
        AssembledSegments expectedResult = new AssembledSegments(expectedSegments, environment);
        assertEquals(expectedResult, result);
        verify(firstPassResolver).resolve(statements, expectedResult);
    }
}