package com.gnopai.ji65.parser.statement;

import com.gnopai.ji65.parser.Parser;
import com.gnopai.ji65.parser.expression.Expression;
import com.gnopai.ji65.scanner.Token;
import com.gnopai.ji65.scanner.TokenType;

public class AssignmentStatementParselet implements StatementParselet {
    @Override
    public Statement parse(Token token, Parser parser) {
        String name = token.getLexeme();
        parser.consume(TokenType.EQUAL, "Expected equal in assignment");
        Expression expression = parser.expression();
        parser.consume(TokenType.EOL, "Expected end of line");
        return new AssignmentStatement(name, expression);
    }
}
