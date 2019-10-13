package com.gnopai.ji65;

import com.gnopai.ji65.parser.ParseletFactory;
import com.gnopai.ji65.parser.Parser;
import com.gnopai.ji65.parser.TokenStream;
import com.gnopai.ji65.parser.statement.Statement;
import com.gnopai.ji65.scanner.FileLoader;
import com.gnopai.ji65.scanner.Scanner;
import com.gnopai.ji65.scanner.SourceFile;
import com.gnopai.ji65.scanner.Token;
import com.gnopai.ji65.util.ErrorHandler;

import javax.inject.Inject;
import java.util.List;

public class SourceFileProcessor {
    private static final int MAX_FILE_DEPTH = 16;
    static final String FILE_OPEN_ERROR = "Failed to open source file: %s";
    static final String MAX_FILE_DEPTH_ERROR = "Maximum file depth of " + MAX_FILE_DEPTH + " reached while parsing %s";

    private final FileLoader fileLoader;
    private final Scanner scanner;
    private final ErrorHandler errorHandler;
    private int fileDepth = 0;

    @Inject
    public SourceFileProcessor(FileLoader fileLoader, Scanner scanner, ErrorHandler errorHandler) {
        this.fileLoader = fileLoader;
        this.scanner = scanner;
        this.errorHandler = errorHandler;
    }

    public List<Statement> loadAndParse(String fileName) {
        incrementFileDepth(fileName);
        List<Statement> statements = fileLoader.loadSourceFile(fileName)
                .map(this::parse)
                .orElseThrow(() -> new RuntimeException(String.format(FILE_OPEN_ERROR, fileName)));
        decrementFileDepth();
        return statements;
    }

    private void incrementFileDepth(String fileName) {
        if (fileDepth >= MAX_FILE_DEPTH) {
            throw new RuntimeException(String.format(MAX_FILE_DEPTH_ERROR, fileName));
        }
        fileDepth++;
    }

    private void decrementFileDepth() {
        fileDepth--;
    }

    public List<Statement> parse(SourceFile sourceFile) {
        List<Token> tokens = scanner.scan(sourceFile.getText());
        TokenStream tokenStream = new TokenStream(errorHandler, tokens);
        Parser parser = new Parser(new ParseletFactory(this), tokenStream);
        return parser.parse();
    }
}
