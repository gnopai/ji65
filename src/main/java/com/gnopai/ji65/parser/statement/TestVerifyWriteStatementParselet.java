package com.gnopai.ji65.parser.statement;

import com.gnopai.ji65.parser.Parser;
import com.gnopai.ji65.parser.expression.Expression;
import com.gnopai.ji65.scanner.Token;
import com.gnopai.ji65.test.TestStepType;

import java.util.ArrayList;
import java.util.List;

import static com.gnopai.ji65.scanner.TokenType.COMMA;

public class TestVerifyWriteStatementParselet implements StatementParselet {

    @Override
    public Statement parse(Token token, Parser parser) {
        Expression address = parser.expression();
        List<Expression> values = parseValues(parser);
        parser.consumeEndOfLine();

        return TestStatement.builder()
                .type(TestStepType.VERIFY_WRITE)
                .targetAddress(address)
                .values(values)
                .build();
    }

    private List<Expression> parseValues(Parser parser) {
        List<Expression> values = new ArrayList<>();
        values.add(parser.expression());
        while (parser.match(COMMA)) {
            values.add(parser.expression());
        }
        return values;
    }
}
