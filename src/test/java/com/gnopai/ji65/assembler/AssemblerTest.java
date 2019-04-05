package com.gnopai.ji65.assembler;

import com.gnopai.ji65.InstructionType;
import com.gnopai.ji65.config.ProgramConfig;
import com.gnopai.ji65.config.SegmentConfig;
import com.gnopai.ji65.directive.DirectiveType;
import com.gnopai.ji65.parser.expression.Expression;
import com.gnopai.ji65.parser.expression.ExpressionEvaluator;
import com.gnopai.ji65.parser.expression.PrimaryExpression;
import com.gnopai.ji65.parser.statement.DirectiveStatement;
import com.gnopai.ji65.parser.statement.InstructionStatement;
import com.gnopai.ji65.parser.statement.LabelStatement;
import com.gnopai.ji65.parser.statement.Statement;
import com.gnopai.ji65.scanner.TokenType;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.gnopai.ji65.assembler.AssembledSegments.DEFAULT_SEGMENT_NAME;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class AssemblerTest {
    private final InstructionAssembler instructionAssembler = mock(InstructionAssembler.class);
    private final FirstPassResolver firstPassResolver = mock(FirstPassResolver.class);
    private final ExpressionEvaluator expressionEvaluator = mock(ExpressionEvaluator.class);

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

        Assembler assembler = new Assembler(firstPassResolver, instructionAssembler, expressionEvaluator);

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

        Label expectedLabel = new Label("derp", false);
        environment.define("derp", expectedLabel);

        Assembler assembler = new Assembler(firstPassResolver, instructionAssembler, expressionEvaluator);

        AssembledSegments result = assembler.assemble(statements, programConfig, environment);

        List<Segment> expectedSegments = List.of(new Segment(segmentConfig, List.of(expectedLabel)));
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

        Assembler assembler = new Assembler(firstPassResolver, instructionAssembler, expressionEvaluator);

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
    void testDirectiveStatement_reserve() {
        Expression sizeExpression = new PrimaryExpression(TokenType.NUMBER, 55);
        when(expressionEvaluator.evaluate(sizeExpression, environment)).thenReturn(55);

        DirectiveStatement directiveStatement = DirectiveStatement.builder()
                .type(DirectiveType.RESERVE)
                .expression(sizeExpression)
                .build();
        List<Statement> statements = List.of(directiveStatement);

        Assembler assembler = new Assembler(firstPassResolver, instructionAssembler, expressionEvaluator);

        AssembledSegments result = assembler.assemble(statements, programConfig, environment);

        List<Segment> expectedSegments = List.of(
                new Segment(segmentConfig, List.of(new ReservedData(55)))
        );
        AssembledSegments expectedResult = new AssembledSegments(expectedSegments, environment);
        assertEquals(expectedResult, result);
        verify(firstPassResolver).resolve(statements, expectedResult);
    }
}