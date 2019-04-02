package com.gnopai.ji65.parser.statement;

import com.gnopai.ji65.directive.DirectiveType;
import com.gnopai.ji65.parser.ParseException;
import com.gnopai.ji65.parser.Parser;
import com.gnopai.ji65.scanner.Token;

import java.util.Map;

import static java.util.Optional.ofNullable;

public class DirectiveStatementParselet implements StatementParselet {
    private final Map<DirectiveType, StatementParselet> directiveParslets;

    public DirectiveStatementParselet() {
        directiveParslets = Map.of(
                DirectiveType.SEGMENT, new SegmentDirectiveParser()
        );
    }

    @Override
    public Statement parse(Token token, Parser parser) {
        DirectiveType directiveType = (DirectiveType) token.getValue();
        return ofNullable(directiveParslets.get(directiveType))
                .map(directiveParslet -> directiveParslet.parse(token, parser))
                .orElseThrow(() -> new ParseException(token, "Unsupported directive type: " + directiveType.name()));
    }
}
