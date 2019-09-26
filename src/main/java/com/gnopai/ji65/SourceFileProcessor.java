package com.gnopai.ji65;

import com.gnopai.ji65.parser.ParseletFactory;
import com.gnopai.ji65.parser.Parser;
import com.gnopai.ji65.parser.TokenConsumer;
import com.gnopai.ji65.parser.statement.Statement;
import com.gnopai.ji65.scanner.FileLoader;
import com.gnopai.ji65.scanner.Scanner;
import com.gnopai.ji65.scanner.SourceFile;
import com.gnopai.ji65.scanner.Token;
import com.gnopai.ji65.util.ErrorHandler;

import javax.inject.Inject;
import java.util.List;

public class SourceFileProcessor {
    private final FileLoader fileLoader;
    private final Scanner scanner;
    private final ParseletFactory parseletFactory;
    private final ErrorHandler errorHandler;

    @Inject
    public SourceFileProcessor(FileLoader fileLoader, Scanner scanner, ParseletFactory parseletFactory, ErrorHandler errorHandler) {
        this.fileLoader = fileLoader;
        this.scanner = scanner;
        this.parseletFactory = parseletFactory;
        this.errorHandler = errorHandler;
    }

    public List<Statement> loadAndParse(String fileName) {
        return fileLoader.loadSourceFile(fileName)
                .map(this::parse)
                .orElseThrow(() -> new RuntimeException("Failed to open source file: " + fileName));
    }

    public List<Statement> parse(SourceFile sourceFile) {
        List<Token> tokens = scanner.scan(sourceFile.getText());
        TokenConsumer tokenConsumer = new TokenConsumer(errorHandler, tokens);
        Parser parser = new Parser(parseletFactory, tokenConsumer);
        return parser.parse();
    }
}
