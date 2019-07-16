package com.gnopai.ji65.parser.statement;

import com.gnopai.ji65.DirectiveType;
import com.gnopai.ji65.parser.Parser;
import com.gnopai.ji65.scanner.Token;
import com.gnopai.ji65.scanner.TokenType;

public class IncludeBinaryDirectiveParser implements StatementParselet {
    @Override
    public Statement parse(Token token, Parser parser) {
        String fileName = parser.consumeString("Expected file name");
        parser.consume(TokenType.EOL, "Expected end of line");
        return DirectiveStatement.builder()
                .type(DirectiveType.INCLUDE_BINARY)
                .name(fileName)
                .build();
    }
}
