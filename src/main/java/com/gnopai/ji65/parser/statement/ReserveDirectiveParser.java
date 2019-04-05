package com.gnopai.ji65.parser.statement;

import com.gnopai.ji65.directive.DirectiveType;
import com.gnopai.ji65.parser.Parser;
import com.gnopai.ji65.parser.expression.Expression;
import com.gnopai.ji65.scanner.Token;
import com.gnopai.ji65.scanner.TokenType;

public class ReserveDirectiveParser implements StatementParselet {
    @Override
    public Statement parse(Token token, Parser parser) {
        Expression sizeExpression = parser.expression();
        parser.consume(TokenType.EOL, "Expected end of line");
        return DirectiveStatement.builder()
                .type(DirectiveType.RESERVE)
                .expression(sizeExpression)
                .build();
    }
}
