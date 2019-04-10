package com.gnopai.ji65.parser.statement;

import com.gnopai.ji65.DirectiveType;
import com.gnopai.ji65.parser.Parser;
import com.gnopai.ji65.scanner.Token;

import static com.gnopai.ji65.scanner.TokenType.COMMA;
import static com.gnopai.ji65.scanner.TokenType.EOL;

public class DataDirectiveParser implements StatementParselet {
    private final DirectiveType type;

    public DataDirectiveParser(DirectiveType type) {
        this.type = type;
    }

    @Override
    public Statement parse(Token token, Parser parser) {
        DirectiveStatement.DirectiveStatementBuilder builder = DirectiveStatement.builder().type(type);
        builder.expression(parser.expression());
        while (!parser.match(EOL)) {
            parser.match(COMMA); // optional commas between expressions
            builder.expression(parser.expression());
        }
        return builder.build();
    }
}
