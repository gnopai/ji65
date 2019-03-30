package com.gnopai.ji65.assembler;

import com.gnopai.ji65.config.ProgramConfig;
import com.gnopai.ji65.config.SegmentConfig;
import com.gnopai.ji65.parser.statement.LabelStatement;
import com.gnopai.ji65.parser.statement.Statement;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class AssemblerTest {
    private final FirstPassResolver firstPassResolver = mock(FirstPassResolver.class);
    private final InstructionAssembler instructionAssembler = mock(InstructionAssembler.class);

    @Test
    void testAssemble() {
        Statement statement1 = mock(Statement.class);
        Statement statement2 = mock(Statement.class);
        Statement statement3 = mock(Statement.class);
        List<Statement> statements = List.of(statement1, statement2, statement3);

        SegmentConfig segmentConfig = SegmentConfig.builder().segmentName("CODE").build();
        ProgramConfig programConfig = ProgramConfig.builder()
                .segmentConfigs(List.of(
                        segmentConfig
                ))
                .build();
        Environment environment = new Environment();

        Assembler assembler = new Assembler(firstPassResolver, instructionAssembler);

        AssembledSegments result = assembler.assemble(statements, programConfig, environment);

        List<Segment> expectedSegments = List.of(new Segment(segmentConfig));
        AssembledSegments expectedResult = new AssembledSegments(expectedSegments, environment);
        assertEquals(expectedResult, result);
        verify(firstPassResolver).resolve(statements, environment);
        verify(statement1).accept(assembler);
        verify(statement2).accept(assembler);
        verify(statement3).accept(assembler);
    }

    @Test
    void testLabelStatement() {
        LabelStatement statement = new LabelStatement("derp");
        List<Statement> statements = List.of(statement);

        Environment environment = new Environment();
        Label expectedLabel = new Label("derp", false);
        environment.define("derp", expectedLabel);

        SegmentConfig segmentConfig = SegmentConfig.builder().segmentName("CODE").build();
        ProgramConfig programConfig = ProgramConfig.builder()
                .segmentConfigs(List.of(segmentConfig))
                .build();

        Assembler assembler = new Assembler(firstPassResolver, instructionAssembler);

        AssembledSegments result = assembler.assemble(statements, programConfig, environment);

        List<Segment> expectedSegments = List.of(new Segment(segmentConfig, List.of(expectedLabel)));
        AssembledSegments expectedResult = new AssembledSegments(expectedSegments, environment);
        assertEquals(expectedResult, result);
        verify(firstPassResolver).resolve(statements, environment);
    }
}