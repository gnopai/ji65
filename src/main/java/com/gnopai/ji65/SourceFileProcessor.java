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

// TODO only load files once? Just need a little tracker thing here I think...
public class SourceFileProcessor {
    private final FileLoader fileLoader;
    private final Scanner scanner;
    private final ErrorHandler errorHandler;

    @Inject
    public SourceFileProcessor(FileLoader fileLoader, Scanner scanner, ErrorHandler errorHandler) {
        this.fileLoader = fileLoader;
        this.scanner = scanner;
        this.errorHandler = errorHandler;
    }

    public List<Statement> loadAndParse(String fileName) {
        return fileLoader.loadSourceFile(fileName)
                .map(this::parse)
                .orElseThrow(() -> new RuntimeException("Failed to open source file: " + fileName));
    }

    public List<Statement> parse(SourceFile sourceFile) {
        List<Token> tokens = scanner.scan(sourceFile.getText());
        TokenStream tokenStream = new TokenStream(errorHandler, tokens);
        Parser parser = new Parser(new ParseletFactory(this), tokenStream);
        return parser.parse();
    }
}
