package com.gnopai.ji65.parser.statement;

import com.gnopai.ji65.parser.Parser;
import com.gnopai.ji65.parser.expression.Expression;
import com.gnopai.ji65.scanner.Token;

public class TestRunStatementParselet implements StatementParselet {
    @Override
    public Statement parse(Token token, Parser parser) {
        Expression targetAddress = parser.expression();
        parser.consumeEndOfLine();
        return TestStatement.builder()
                .type(TestStatement.Type.RUN)
                .targetAddress(targetAddress)
                .build();
    }
}
