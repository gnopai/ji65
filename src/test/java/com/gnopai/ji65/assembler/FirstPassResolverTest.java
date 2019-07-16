package com.gnopai.ji65.assembler;

import com.gnopai.ji65.DirectiveType;
import com.gnopai.ji65.config.SegmentConfig;
import com.gnopai.ji65.config.SegmentType;
import com.gnopai.ji65.parser.expression.ExpressionEvaluator;
import com.gnopai.ji65.parser.expression.PrefixExpression;
import com.gnopai.ji65.parser.expression.PrimaryExpression;
import com.gnopai.ji65.parser.statement.*;
import com.gnopai.ji65.scanner.TokenType;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static com.gnopai.ji65.assembler.AssembledSegments.DEFAULT_SEGMENT_NAME;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class FirstPassResolverTest {
    private final ExpressionEvaluator expressionEvaluator = mock(ExpressionEvaluator.class);

    @Test
    void testAssignmentStatement() {
        Environment environment = new Environment();
        PrefixExpression expression = new PrefixExpression(TokenType.MINUS, new PrimaryExpression(TokenType.NUMBER, 77));
        when(expressionEvaluator.evaluate(expression, environment)).thenReturn(44);
        AssembledSegments assembledSegments = new AssembledSegments(List.of(segment(DEFAULT_SEGMENT_NAME, SegmentType.READ_ONLY)), environment);

        AssignmentStatement statement = new AssignmentStatement("derp", expression);

        FirstPassResolver testClass = new FirstPassResolver(expressionEvaluator);

        testClass.resolve(List.of(statement), assembledSegments);

        assertEquals(Optional.of(new PrimaryExpression(TokenType.NUMBER, 44)), environment.get("derp"));
    }

    @Test
    void testLabelStatement_zeroPage() {
        String zeroPageSegment = "ZEROPAGE";
        DirectiveStatement segmentDirectiveStatement = DirectiveStatement.builder()
                .type(DirectiveType.SEGMENT)
                .name(zeroPageSegment)
                .build();
        LabelStatement labelStatement = new LabelStatement("derp");
        Environment environment = new Environment();
        AssembledSegments assembledSegments = new AssembledSegments(List.of(
                segment(DEFAULT_SEGMENT_NAME, SegmentType.READ_ONLY),
                segment(zeroPageSegment, SegmentType.ZERO_PAGE)
        ), environment);

        FirstPassResolver testClass = new FirstPassResolver(expressionEvaluator);

        testClass.resolve(List.of(segmentDirectiveStatement, labelStatement), assembledSegments);

        Label expectedLabel = Label.builder()
                .name("derp")
                .zeroPage(true)
                .build();
        assertEquals(Optional.of(expectedLabel), environment.get("derp"));
    }

    @Test
    void testLabelStatement_notZeroPage() {
        LabelStatement statement = new LabelStatement("derp");
        Environment environment = new Environment();
        AssembledSegments assembledSegments = new AssembledSegments(List.of(segment(DEFAULT_SEGMENT_NAME, SegmentType.READ_ONLY)), environment);

        FirstPassResolver testClass = new FirstPassResolver(expressionEvaluator);

        testClass.resolve(List.of(statement), assembledSegments);

        Label expectedLabel = Label.builder()
                .name("derp")
                .zeroPage(false)
                .build();
        assertEquals(Optional.of(expectedLabel), environment.get("derp"));
    }

    @Test
    void testLocalLabelStatement() {
        LabelStatement labelStatement = new LabelStatement("parent");
        LocalLabelStatement localLabelStatement = new LocalLabelStatement("child");
        Environment environment = new Environment();
        AssembledSegments assembledSegments = new AssembledSegments(List.of(segment(DEFAULT_SEGMENT_NAME, SegmentType.READ_ONLY)), environment);

        FirstPassResolver testClass = new FirstPassResolver(expressionEvaluator);

        testClass.resolve(List.of(labelStatement, localLabelStatement), assembledSegments);

        Label expectedLabel = Label.builder()
                .name("child")
                .zeroPage(false)
                .local(true)
                .build();

        environment.goToRootScope();
        assertEquals(Optional.empty(), environment.get("child"));
        environment.enterChildScope("parent");
        assertEquals(Optional.of(expectedLabel), environment.get("child"));
    }

    @Test
    void testLocalLabelStatement_missingParent() {
        LocalLabelStatement localLabelStatement = new LocalLabelStatement("child");
        Environment environment = new Environment();
        AssembledSegments assembledSegments = new AssembledSegments(List.of(segment(DEFAULT_SEGMENT_NAME, SegmentType.READ_ONLY)), environment);

        FirstPassResolver testClass = new FirstPassResolver(expressionEvaluator);

        testClass.resolve(List.of(localLabelStatement), assembledSegments);

        Label expectedLabel = Label.builder()
                .name("child")
                .zeroPage(false)
                .local(true)
                .build();
        assertEquals(Optional.of(expectedLabel), environment.get("child"));
    }

    @Test
    void testResolve() {
        Statement statement1 = mock(Statement.class);
        Statement statement2 = mock(Statement.class);
        Statement statement3 = mock(Statement.class);
        Environment environment = new Environment();
        AssembledSegments assembledSegments = new AssembledSegments(List.of(segment(DEFAULT_SEGMENT_NAME, SegmentType.READ_ONLY)), environment);

        FirstPassResolver testClass = new FirstPassResolver(expressionEvaluator);

        testClass.resolve(List.of(statement1, statement2, statement3), assembledSegments);

        verify(statement1).accept(testClass);
        verify(statement2).accept(testClass);
        verify(statement3).accept(testClass);
    }

    @Test
    void testDirectiveStatement_macro() {
        Statement statement1 = mock(Statement.class);
        Statement statement2 = mock(Statement.class);
        String macroName = "Whee";
        DirectiveStatement directiveStatement = DirectiveStatement.builder()
                .type(DirectiveType.MACRO)
                .name(macroName)
                .statement(statement1)
                .statement(statement2)
                .argument("a")
                .build();
        Environment environment = new Environment();
        AssembledSegments assembledSegments = new AssembledSegments(List.of(segment(DEFAULT_SEGMENT_NAME, SegmentType.READ_ONLY)), environment);

        FirstPassResolver testClass = new FirstPassResolver(expressionEvaluator);

        testClass.resolve(List.of(directiveStatement), assembledSegments);

        Macro expectedMacro = new Macro(macroName, List.of(statement1, statement2), List.of("a"));
        assertEquals(Optional.of(expectedMacro), environment.getMacro(macroName));
    }

    private Segment segment(String name, SegmentType type) {
        return new Segment(SegmentConfig.builder()
                .segmentName(name)
                .segmentType(type)
                .build()
        );
    }
}