package com.gnopai.ji65.parser.statement;

import com.gnopai.ji65.directive.DirectiveType;
import com.gnopai.ji65.parser.Parser;
import com.gnopai.ji65.scanner.Token;
import com.gnopai.ji65.scanner.TokenType;

public class SegmentDirectiveParser implements StatementParselet {
    @Override
    public Statement parse(Token token, Parser parser) {
        Token name = parser.consume(TokenType.STRING, "Expected string with segment name");
        return DirectiveStatement.builder()
                .type(DirectiveType.SEGMENT)
                .name((String) name.getValue())
                .build();
    }
}
