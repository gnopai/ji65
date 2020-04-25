package com.gnopai.ji65.parser.statement;

import com.gnopai.ji65.parser.Parser;
import com.gnopai.ji65.parser.ParsingService;
import com.gnopai.ji65.scanner.Token;

import javax.inject.Inject;
import java.util.List;

public class IncludeSourceFileDirectiveParser implements StatementParselet {
    private final ParsingService parsingService;

    @Inject
    public IncludeSourceFileDirectiveParser(ParsingService parsingService) {
        this.parsingService = parsingService;
    }

    @Override
    public Statement parse(Token token, Parser parser) {
        String fileName = parser.consumeString("Expected file name");
        parser.consumeEndOfLine();
        List<Statement> statements = parsingService.loadAndParse(fileName);
        return new MultiStatement(statements);
    }
}
