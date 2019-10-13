package com.gnopai.ji65.parser.statement;

import com.gnopai.ji65.DirectiveType;
import com.gnopai.ji65.SourceFileProcessor;
import com.gnopai.ji65.parser.ParseException;
import com.gnopai.ji65.parser.Parser;
import com.gnopai.ji65.scanner.Token;

import java.util.Map;

import static java.util.Optional.ofNullable;

public class DirectiveStatementParselet implements StatementParselet {
    private final Map<DirectiveType, StatementParselet> directiveParselets;

    public DirectiveStatementParselet(SourceFileProcessor sourceFileProcessor) {
        directiveParselets = Map.of(
                DirectiveType.SEGMENT, new SegmentDirectiveParser(),
                DirectiveType.RESERVE, new ReserveDirectiveParser(),
                DirectiveType.BYTE, new DataDirectiveParser(DirectiveType.BYTE),
                DirectiveType.WORD, new DataDirectiveParser(DirectiveType.WORD),
                DirectiveType.REPEAT, new RepeatDirectiveParser(),
                DirectiveType.MACRO, new MacroDirectiveParser(),
                DirectiveType.INCLUDE, new IncludeSourceFileDirectiveParser(sourceFileProcessor),
                DirectiveType.INCLUDE_BINARY, new IncludeBinaryDirectiveParser()
        );
    }

    @Override
    public Statement parse(Token token, Parser parser) {
        DirectiveType directiveType = (DirectiveType) token.getValue();
        return ofNullable(directiveParselets.get(directiveType))
                .map(directiveParslet -> directiveParslet.parse(token, parser))
                .orElseThrow(() -> new ParseException(token, "Unsupported directive type: " + directiveType.name()));
    }
}
