package com.gnopai.ji65.assembler;

import com.gnopai.ji65.DirectiveType;
import com.gnopai.ji65.parser.expression.Expression;
import com.gnopai.ji65.parser.expression.ExpressionEvaluator;
import com.gnopai.ji65.parser.expression.PrimaryExpression;
import com.gnopai.ji65.parser.statement.DirectiveStatement;
import com.gnopai.ji65.scanner.FileLoader;
import com.gnopai.ji65.scanner.TokenType;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class DirectiveDataAssemblerTest {
    private final ExpressionEvaluator expressionEvaluator = mock(ExpressionEvaluator.class);
    private final FileLoader fileLoader = mock(FileLoader.class);
    private final Environment environment = new Environment();

    @Test
    void testReserveDirective() {
        int size = 44;
        Expression sizeExpression = new PrimaryExpression(TokenType.NUMBER, size);
        when(expressionEvaluator.evaluate(sizeExpression, environment)).thenReturn(size);
        DirectiveStatement statement = DirectiveStatement.builder()
                .type(DirectiveType.RESERVE)
                .expression(sizeExpression)
                .build();

        List<SegmentData> segmentData = assemble(statement);

        List<SegmentData> expectedSegmentData = List.of(
                new ReservedData(size)
        );
        assertEquals(expectedSegmentData, segmentData);
    }

    @Test
    void testByteDirective() {
        Expression expression1 = new PrimaryExpression(TokenType.NUMBER, 11);
        Expression expression2 = new PrimaryExpression(TokenType.NUMBER, 22);
        Expression expression3 = new PrimaryExpression(TokenType.NUMBER, 33);
        DirectiveStatement statement = DirectiveStatement.builder()
                .type(DirectiveType.BYTE)
                .expressions(List.of(expression1, expression2, expression3))
                .build();

        List<SegmentData> segmentData = assemble(statement);

        List<SegmentData> expectedSegmentData = List.of(
                new UnresolvedExpression(expression1, true),
                new UnresolvedExpression(expression2, true),
                new UnresolvedExpression(expression3, true)
        );
        assertEquals(expectedSegmentData, segmentData);
    }

    @Test
    void testWordDirective() {
        Expression expression1 = new PrimaryExpression(TokenType.NUMBER, 11);
        Expression expression2 = new PrimaryExpression(TokenType.NUMBER, 22);
        Expression expression3 = new PrimaryExpression(TokenType.NUMBER, 33);
        DirectiveStatement statement = DirectiveStatement.builder()
                .type(DirectiveType.WORD)
                .expressions(List.of(expression1, expression2, expression3))
                .build();

        List<SegmentData> segmentData = assemble(statement);

        List<SegmentData> expectedSegmentData = List.of(
                new UnresolvedExpression(expression1, false),
                new UnresolvedExpression(expression2, false),
                new UnresolvedExpression(expression3, false)
        );
        assertEquals(expectedSegmentData, segmentData);
    }

    @Test
    void testIncludeBinaryDirective() {
        String filename = "wheeee.chr";
        DirectiveStatement statement = DirectiveStatement.builder()
                .type(DirectiveType.INCLUDE_BINARY)
                .name(filename)
                .build();

        List<Byte> bytes = List.of((byte) 0x12, (byte) 0xFF);
        when(fileLoader.loadBinaryFile(filename)).thenReturn(Optional.of(bytes));

        List<SegmentData> segmentData = assemble(statement);

        List<SegmentData> expectedSegmentData = List.of(new RawData(bytes));
        assertEquals(expectedSegmentData, segmentData);
    }

    @Test
    void testIncludeBinaryDirective_badFile() {
        String filename = "whee.chr";
        DirectiveStatement statement = DirectiveStatement.builder()
                .type(DirectiveType.INCLUDE_BINARY)
                .name(filename)
                .build();

        when(fileLoader.loadBinaryFile(filename)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> assemble(statement));
        assertEquals("Failed to open binary file: whee.chr", exception.getMessage());
    }

    @Test
    void testUnsupportedDirectiveType() {
        DirectiveStatement statement = DirectiveStatement.builder()
                .type(DirectiveType.MACRO_END)
                .build();
        RuntimeException exception = assertThrows(RuntimeException.class, () -> assemble(statement));
        assertEquals("Directive type not supported: MACRO_END", exception.getMessage());
    }

    private List<SegmentData> assemble(DirectiveStatement statement) {
        return new DirectiveDataAssembler(expressionEvaluator, fileLoader)
                .assemble(statement, environment);
    }
}