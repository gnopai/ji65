package com.gnopai.ji65.parser.statement;

import com.gnopai.ji65.parser.Parser;
import com.gnopai.ji65.parser.expression.Expression;
import com.gnopai.ji65.parser.expression.PrimaryExpression;
import com.gnopai.ji65.scanner.Token;
import com.gnopai.ji65.test.TestStepType;

import static com.gnopai.ji65.scanner.TokenType.NUMBER;

public class TestVerifyReadStatementParselet implements StatementParselet {

    @Override
    public Statement parse(Token token, Parser parser) {
        Expression address = parser.expression();
        Expression value = parseValue(parser);
        parser.consumeEndOfLine();

        return TestStatement.builder()
                .type(TestStepType.VERIFY_READ)
                .targetAddress(address)
                .value(value)
                .build();
    }

    private Expression parseValue(Parser parser) {
        if (NUMBER.equals(parser.peekNextTokenType())) {
            return parser.expression();
        }
        return new PrimaryExpression(NUMBER, 1);
    }
}
