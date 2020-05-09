package com.gnopai.ji65.parser.statement;

import com.gnopai.ji65.parser.Parser;
import com.gnopai.ji65.parser.expression.Expression;
import com.gnopai.ji65.scanner.Token;
import com.gnopai.ji65.test.TestStepType;

public class TestRunStatementParselet implements StatementParselet {
    @Override
    public Statement parse(Token token, Parser parser) {
        Expression targetAddress = parser.expression();
        parser.consumeEndOfLine();
        return TestStatement.builder()
                .type(TestStepType.RUN)
                .targetAddress(targetAddress)
                .build();
    }
}
