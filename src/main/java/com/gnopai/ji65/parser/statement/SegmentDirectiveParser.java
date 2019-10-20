package com.gnopai.ji65.parser.statement;

import com.gnopai.ji65.DirectiveType;
import com.gnopai.ji65.parser.Parser;
import com.gnopai.ji65.scanner.Token;

public class SegmentDirectiveParser implements StatementParselet {
    @Override
    public Statement parse(Token token, Parser parser) {
        String name = parser.consumeString("Expected string with segment name");
        parser.consumeEndOfLine();
        return DirectiveStatement.builder()
                .type(DirectiveType.SEGMENT)
                .name(name)
                .build();
    }
}
