package com.gnopai.ji65.parser.statement;

import com.gnopai.ji65.DirectiveType;
import com.gnopai.ji65.parser.ParseException;
import com.gnopai.ji65.parser.Parser;
import com.gnopai.ji65.parser.ParsingService;
import com.gnopai.ji65.scanner.Token;

import java.util.Map;

import static java.util.Map.entry;
import static java.util.Optional.ofNullable;

public class DirectiveStatementParselet implements StatementParselet {
    private final Map<DirectiveType, StatementParselet> directiveParselets;

    public DirectiveStatementParselet(ParsingService parsingService) {
        directiveParselets = Map.ofEntries(
                entry(DirectiveType.SEGMENT, new SegmentDirectiveParser()),
                entry(DirectiveType.RESERVE, new ReserveDirectiveParser()),
                entry(DirectiveType.BYTE, new DataDirectiveParser(DirectiveType.BYTE)),
                entry(DirectiveType.WORD, new DataDirectiveParser(DirectiveType.WORD)),
                entry(DirectiveType.REPEAT, new RepeatDirectiveParser()),
                entry(DirectiveType.MACRO, new MacroDirectiveParser(parsingService)),
                entry(DirectiveType.INCLUDE, new IncludeSourceFileDirectiveParser(parsingService)),
                entry(DirectiveType.INCLUDE_BINARY, new IncludeBinaryDirectiveParser()),
                entry(DirectiveType.TEST, new TestDirectiveParser()),
                entry(DirectiveType.TEST_ASSERT, new TestAssertStatementParselet()),
                entry(DirectiveType.TEST_SET, new TestSetStatementParselet()),
                entry(DirectiveType.TEST_RUN, new TestRunStatementParselet())
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
