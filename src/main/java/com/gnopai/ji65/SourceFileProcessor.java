package com.gnopai.ji65;

import com.gnopai.ji65.parser.Parser;
import com.gnopai.ji65.parser.statement.Statement;
import com.gnopai.ji65.scanner.FileLoader;
import com.gnopai.ji65.scanner.Scanner;
import com.gnopai.ji65.scanner.SourceFile;
import com.gnopai.ji65.scanner.Token;

import javax.inject.Inject;
import java.util.List;

public class SourceFileProcessor {
    private final FileLoader fileLoader;
    private final Scanner scanner;
    private final Parser parser;

    @Inject
    public SourceFileProcessor(FileLoader fileLoader, Scanner scanner, Parser parser) {
        this.fileLoader = fileLoader;
        this.scanner = scanner;
        this.parser = parser;
    }

    public List<Statement> loadAndParse(String fileName) {
        return fileLoader.loadSourceFile(fileName)
                .map(this::parse)
                .orElseThrow(() -> new RuntimeException("Failed to open source file: " + fileName));
    }

    public List<Statement> parse(SourceFile sourceFile) {
        List<Token> tokens = scanner.scan(sourceFile.getText());
        return parser.parse(tokens);
    }
}
