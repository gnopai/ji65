package com.gnopai.ji65;

import com.gnopai.ji65.parser.statement.InstructionStatement;
import com.gnopai.ji65.parser.statement.Statement;
import com.gnopai.ji65.scanner.FileLoader;
import com.gnopai.ji65.scanner.Scanner;
import com.gnopai.ji65.scanner.SourceFile;
import com.gnopai.ji65.scanner.TokenType;
import com.gnopai.ji65.util.ErrorHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static com.gnopai.ji65.SourceFileProcessor.FILE_OPEN_ERROR;
import static com.gnopai.ji65.SourceFileProcessor.MAX_FILE_DEPTH_ERROR;
import static com.gnopai.ji65.parser.ParserTestUtil.token;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SourceFileProcessorTest {
    private final FileLoader fileLoader = mock(FileLoader.class);
    private final Scanner scanner = mock(Scanner.class);
    private final ErrorHandler errorHandler = mock(ErrorHandler.class);

    private SourceFileProcessor testClass;

    @BeforeEach
    void setUp() {
        testClass = new SourceFileProcessor(fileLoader, scanner, errorHandler);
    }

    @Test
    void testHappyPath() {
        // given
        String fileName = "whee.s";
        String fileText = "whee";
        SourceFile sourceFile = new SourceFile(null, fileText);
        when(fileLoader.loadSourceFile(fileName)).thenReturn(Optional.of(sourceFile));

        when(scanner.scan(fileText)).thenReturn(List.of(
                token(TokenType.INSTRUCTION, InstructionType.SEC),
                token(TokenType.EOL),
                token(TokenType.EOF)
        ));

        // when
        List<Statement> statements = testClass.loadAndParse(fileName);

        // then
        List<Statement> expectedStatements = List.of(InstructionStatement.builder()
                .instructionType(InstructionType.SEC)
                .addressingModeType(AddressingModeType.IMPLICIT)
                .build()
        );
        assertEquals(expectedStatements, statements);
    }

    @Test
    void testFileLoadFailure() {
        String fileName = "whee.s";
        when(fileLoader.loadSourceFile(fileName)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> testClass.loadAndParse(fileName)
        );
        assertEquals(String.format(FILE_OPEN_ERROR, fileName), exception.getMessage());
    }

    @Test
    void testFileIncludeLoopErrorsOut() {
        // given
        String fileName = "whee.s";
        String fileText = "whee";
        SourceFile sourceFile = new SourceFile(null, fileText);
        when(fileLoader.loadSourceFile(fileName)).thenReturn(Optional.of(sourceFile));

        when(scanner.scan(fileText)).thenReturn(List.of(
                token(TokenType.INSTRUCTION, InstructionType.SEC),
                token(TokenType.EOL),
                token(TokenType.DIRECTIVE, DirectiveType.INCLUDE),
                token(TokenType.CHAR, (int) 'w'),
                token(TokenType.CHAR, (int) 'h'),
                token(TokenType.CHAR, (int) 'e'),
                token(TokenType.CHAR, (int) 'e'),
                token(TokenType.CHAR, (int) '.'),
                token(TokenType.CHAR, (int) 's'),
                token(TokenType.EOL),
                token(TokenType.INSTRUCTION, InstructionType.SED),
                token(TokenType.EOL)
        ));

        // when/then
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> testClass.loadAndParse(fileName)
        );
        assertEquals(String.format(MAX_FILE_DEPTH_ERROR, fileName), exception.getMessage());
    }
}