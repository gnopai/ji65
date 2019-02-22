package com.gnopai.ji65.parser.statement;

import com.gnopai.ji65.parser.Parser;
import com.gnopai.ji65.parser.expression.Expression;
import com.gnopai.ji65.scanner.Token;
import com.gnopai.ji65.scanner.TokenType;

public class AssignmentStatementParselet implements StatementParselet {
    @Override
    public Statement parse(Token token, Parser parser) {
        // current token should be "=", previous token is the identifier
        String name = parser.getPrevious().getLexeme();
        Expression expression = parser.expression();
        parser.consume(TokenType.EOL, "Expected end of line");
        return new AssignmentStatement(name, expression);
    }
}
