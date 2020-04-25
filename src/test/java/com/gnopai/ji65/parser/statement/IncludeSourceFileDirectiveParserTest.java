package com.gnopai.ji65.parser.statement;

import com.gnopai.ji65.DirectiveType;
import com.gnopai.ji65.SourceFileProcessor;
import com.gnopai.ji65.util.ErrorHandler;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.gnopai.ji65.parser.ParserTestUtil.parse;
import static com.gnopai.ji65.parser.ParserTestUtil.token;
import static com.gnopai.ji65.scanner.TokenType.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class IncludeSourceFileDirectiveParserTest {
    private final ErrorHandler errorHandler = mock(ErrorHandler.class);
    private final SourceFileProcessor sourceFileProcessor = mock(SourceFileProcessor.class);

    @Test
    void test() {
        Statement statement1 = mock(Statement.class);
        Statement statement2 = mock(Statement.class);
        Statement statement3 = mock(Statement.class);
        List<Statement> statements = List.of(statement1, statement2, statement3);
        when(sourceFileProcessor.loadAndParse("whee.s")).thenReturn(statements);

        Statement result = parse(errorHandler, sourceFileProcessor,
                token(DIRECTIVE, DirectiveType.INCLUDE),
                token(CHAR, (int) 'w'),
                token(CHAR, (int) 'h'),
                token(CHAR, (int) 'e'),
                token(CHAR, (int) 'e'),
                token(CHAR, (int) '.'),
                token(CHAR, (int) 's'),
                token(EOL)
        );

        MultiStatement expectedResult = new MultiStatement(statements);
        assertEquals(expectedResult, result);
    }
}