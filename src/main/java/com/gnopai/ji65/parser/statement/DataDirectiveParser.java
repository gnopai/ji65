package com.gnopai.ji65.parser.statement;

import com.gnopai.ji65.directive.DirectiveType;
import com.gnopai.ji65.parser.Parser;
import com.gnopai.ji65.scanner.Token;
import com.gnopai.ji65.scanner.TokenType;

public class DataDirectiveParser implements StatementParselet {
    private final DirectiveType type;

    public DataDirectiveParser(DirectiveType type) {
        this.type = type;
    }

    @Override
    public Statement parse(Token token, Parser parser) {
        DirectiveStatement.DirectiveStatementBuilder builder = DirectiveStatement.builder().type(type);
        builder.expression(parser.expression());
        while (parser.match(TokenType.COMMA)) {
            builder.expression(parser.expression());
        }
        parser.consume(TokenType.EOL, "Expected end of line");
        return builder.build();
    }
}
