package com.gnopai.ji65;

import com.gnopai.ji65.parser.Macro;
import com.gnopai.ji65.parser.statement.InstructionStatement;
import com.gnopai.ji65.parser.statement.MultiStatement;
import com.gnopai.ji65.parser.statement.Statement;
import com.gnopai.ji65.scanner.*;
import com.gnopai.ji65.util.ErrorHandler;
import com.google.common.collect.ImmutableList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import static com.gnopai.ji65.SourceFileProcessor.FILE_OPEN_ERROR;
import static com.gnopai.ji65.SourceFileProcessor.MAX_FILE_DEPTH_ERROR;
import static com.gnopai.ji65.parser.ParserTestUtil.token;
import static java.util.stream.Collectors.toList;
import static org.junit.jupiter.api.Assertions.*;
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
    void testSinglePath() {
        // given
        String fileName = "whee.s";
        SourceFile sourceFile = new SourceFile(null, "whee");
        Path path = Path.of(fileName);
        when(fileLoader.loadSourceFile(path)).thenReturn(Optional.of(sourceFile));

        when(scanner.scan(sourceFile)).thenReturn(List.of(
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
    void testMultiplePaths() {
        // given
        Path firstPath = Paths.get("file1.s");
        SourceFile firstSourceFile = new SourceFile(firstPath, "text1");
        when(fileLoader.loadSourceFile(firstPath))
                .thenReturn(Optional.of(firstSourceFile));
        when(scanner.scan(firstSourceFile)).
                thenReturn(includeFileTokens(List.of(), "/some/dir/file2.s", List.of()));

        Path secondPath = Paths.get("/some/dir/file2.s");
        SourceFile secondSourceFile = new SourceFile(secondPath, "text2");
        when(fileLoader.loadSourceFile(secondPath))
                .thenReturn(Optional.of(secondSourceFile));
        when(scanner.scan(secondSourceFile)).
                thenReturn(includeFileTokens(List.of(), "file3.s", List.of()));

        Path thirdPath = Paths.get("/some/dir/file3.s");
        SourceFile thirdSourceFile = new SourceFile(thirdPath, "text3");
        when(fileLoader.loadSourceFile(thirdPath))
                .thenReturn(Optional.of(thirdSourceFile));
        when(scanner.scan(thirdSourceFile)).thenReturn(instructionTokens(InstructionType.SEC));

        // when
        List<Statement> statements = testClass.loadAndParse("file1.s");

        // then
        List<Statement> expectedStatements = List.of(
                new MultiStatement(List.of(
                        new MultiStatement(List.of(
                                InstructionStatement.builder()
                                        .instructionType(InstructionType.SEC)
                                        .addressingModeType(AddressingModeType.IMPLICIT)
                                        .build()
                        ))
                ))
        );
        assertEquals(expectedStatements, statements);
    }

    @Test
    void testFileLoadFailure() {
        String fileName = "whee.s";
        Path path = Path.of(fileName);
        when(fileLoader.loadSourceFile(path)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> testClass.loadAndParse(fileName)
        );
        assertEquals(String.format(FILE_OPEN_ERROR, fileName), exception.getMessage());
    }

    @Test
    void testFileIncludeLoopErrorsOut() {
        // given
        String fileName = "whee.s";
        SourceFile sourceFile = new SourceFile(Paths.get(fileName), "whee");
        Path path = Path.of(fileName);
        when(fileLoader.loadSourceFile(path)).thenReturn(Optional.of(sourceFile));

        when(scanner.scan(sourceFile)).thenReturn(includeFileTokens(
                instructionTokens(InstructionType.SEC),
                "whee.s",
                instructionTokens(InstructionType.SED)
        ));

        // when/then
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> testClass.loadAndParse(fileName)
        );
        assertEquals(String.format(MAX_FILE_DEPTH_ERROR, fileName), exception.getMessage());
    }

    @Test
    void testMacro() {
        String macroName = "Whee";
        Macro macro = new Macro(macroName, List.of(token(TokenType.PLUS)), List.of());

        assertFalse(testClass.getMacro(macroName).isPresent());

        testClass.defineMacro(macro);

        assertEquals(Optional.of(macro), testClass.getMacro(macroName));
    }

    private List<Token> includeFileTokens(List<Token> tokensBefore, String fileNameString, List<Token> tokensAfter) {
        List<Token> fileNameTokens = fileNameString.chars()
                .mapToObj(c -> token(TokenType.CHAR, c))
                .collect(toList());

        return ImmutableList.<Token>builder()
                .addAll(tokensBefore)
                .add(token(TokenType.DIRECTIVE, DirectiveType.INCLUDE))
                .addAll(fileNameTokens)
                .add(token(TokenType.EOL))
                .addAll(tokensAfter)
                .add(token(TokenType.EOF))
                .build();
    }

    private List<Token> instructionTokens(InstructionType instructionType) {
        return List.of(
                token(TokenType.INSTRUCTION, instructionType),
                token(TokenType.EOL)
        );
    }
}