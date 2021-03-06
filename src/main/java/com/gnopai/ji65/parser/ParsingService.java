package com.gnopai.ji65.parser;

import com.gnopai.ji65.parser.statement.Statement;
import com.gnopai.ji65.scanner.FileLoader;
import com.gnopai.ji65.scanner.Scanner;
import com.gnopai.ji65.scanner.SourceFile;
import com.gnopai.ji65.scanner.Token;
import com.gnopai.ji65.util.ErrorHandler;

import javax.inject.Inject;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class ParsingService {
    private static final int MAX_FILE_DEPTH = 16;
    static final String FILE_OPEN_ERROR = "Failed to open source file: %s";
    static final String MAX_FILE_DEPTH_ERROR = "Maximum file depth of " + MAX_FILE_DEPTH + " reached while parsing %s";

    private final FileLoader fileLoader;
    private final Scanner scanner;
    private final ErrorHandler errorHandler;
    private final Deque<SourceFile> fileStack = new ArrayDeque<>();
    private final Map<String, Macro> macros = new HashMap<>();

    @Inject
    public ParsingService(FileLoader fileLoader, Scanner scanner, ErrorHandler errorHandler) {
        this.fileLoader = fileLoader;
        this.scanner = scanner;
        this.errorHandler = errorHandler;
    }

    public List<Statement> loadAndParse(String fileName) {
        Path path = resolveFilePath(fileName);
        return fileLoader.loadSourceFile(path)
                .map(this::loadAndParse)
                .orElseThrow(() -> new RuntimeException(String.format(FILE_OPEN_ERROR, fileName)));
    }

    public List<Statement> loadAndParse(SourceFile sourceFile) {
        addToStack(sourceFile);
        List<Token> tokens = scanner.scan(sourceFile);
        List<Statement> statements = parseTokensFromCurrentFile(tokens);
        removeTopOfStack();
        return statements;
    }

    public List<Statement> parseTokensFromCurrentFile(List<Token> tokens) {
        TokenStream tokenStream = new TokenStream(errorHandler, tokens);
        Parser parser = new Parser(new ParseletFactory(this), tokenStream);
        return parser.parse();
    }

    public Path resolveFilePath(String fileName) {
        if (fileStack.isEmpty() || fileName.startsWith(File.separator)) {
            return Paths.get(fileName);
        }

        return Optional.ofNullable(fileStack.peek())
                .map(SourceFile::getPath)
                .map(Path::getParent)
                .map(parent -> parent.resolve(fileName))
                .orElse(Paths.get(fileName));
    }

    private void addToStack(SourceFile sourceFile) {
        if (fileStack.size() >= MAX_FILE_DEPTH) {
            throw new RuntimeException(String.format(MAX_FILE_DEPTH_ERROR, sourceFile.getPath()));
        }
        fileStack.push(sourceFile);
    }

    private void removeTopOfStack() {
        fileStack.pop();
    }

    public Optional<Macro> getMacro(String name) {
        return Optional.ofNullable(macros.get(name));
    }

    public void defineMacro(Macro macro) {
        macros.put(macro.getName(), macro);
    }
}
