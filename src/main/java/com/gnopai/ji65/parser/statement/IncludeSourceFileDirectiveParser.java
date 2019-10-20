package com.gnopai.ji65.parser.statement;

import com.gnopai.ji65.DirectiveType;
import com.gnopai.ji65.SourceFileProcessor;
import com.gnopai.ji65.parser.Parser;
import com.gnopai.ji65.scanner.Token;

import javax.inject.Inject;
import java.util.List;

public class IncludeSourceFileDirectiveParser implements StatementParselet {
    private final SourceFileProcessor sourceFileProcessor;

    @Inject
    public IncludeSourceFileDirectiveParser(SourceFileProcessor sourceFileProcessor) {
        this.sourceFileProcessor = sourceFileProcessor;
    }

    @Override
    public Statement parse(Token token, Parser parser) {
        String fileName = parser.consumeString("Expected file name");
        parser.consumeEndOfLine();
        List<Statement> statements = sourceFileProcessor.loadAndParse(fileName);
        return DirectiveStatement.builder()
                .type(DirectiveType.INCLUDE)
                .name(fileName)
                .statements(statements)
                .build();
    }
}
