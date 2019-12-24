package com.gnopai.ji65.parser.statement;

import com.gnopai.ji65.parser.Parser;
import com.gnopai.ji65.parser.expression.Expression;
import com.gnopai.ji65.scanner.Token;
import com.gnopai.ji65.scanner.TokenType;

import java.util.Map;
import java.util.Optional;

public class TestSetStatementParselet implements StatementParselet {
    private static final Map<TokenType, TestStatement.Target> TARGETS = Map.of(
            TokenType.X, TestStatement.Target.X,
            TokenType.Y, TestStatement.Target.Y,
            TokenType.A, TestStatement.Target.A
    );

    @Override
    public Statement parse(Token token, Parser parser) {
        return parseTarget(parser)
                .map(target -> parseRegisterSetStatement(target, parser))
                .orElseGet(() -> parseMemorySetStatement(parser));
    }

    private Optional<TestStatement.Target> parseTarget(Parser parser) {
        Optional<TestStatement.Target> target = Optional.ofNullable(TARGETS.get(parser.peekNextTokenType()));
        if (target.isPresent()) {
            // our peek yielded a real token, so consume it for real
            parser.consume();
        }
        return target;
    }

    private TestStatement parseRegisterSetStatement(TestStatement.Target target, Parser parser) {
        Expression value = parser.expression();
        parser.consumeEndOfLine();

        return TestStatement.builder()
                .type(TestStatement.Type.SET)
                .target(target)
                .value(value)
                .build();
    }

    private TestStatement parseMemorySetStatement(Parser parser) {
        Expression targetAddress = parser.expression();
        Expression value = parser.expression();
        parser.consumeEndOfLine();

        return TestStatement.builder()
                .type(TestStatement.Type.SET)
                .target(TestStatement.Target.MEMORY)
                .targetAddress(targetAddress)
                .value(value)
                .build();
    }
}
