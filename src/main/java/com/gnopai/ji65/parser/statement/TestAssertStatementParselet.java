package com.gnopai.ji65.parser.statement;

import com.gnopai.ji65.parser.Parser;
import com.gnopai.ji65.parser.expression.Expression;
import com.gnopai.ji65.scanner.Token;
import com.gnopai.ji65.scanner.TokenType;

import java.util.Map;
import java.util.Optional;

public class TestAssertStatementParselet implements StatementParselet {
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
        TestStatement.TestStatementBuilder testStatementBuilder = TestStatement.builder()
                .target(target);
        return parseStatement(testStatementBuilder, parser);
    }

    private TestStatement parseMemorySetStatement(Parser parser) {
        Expression targetAddress = parser.expression();
        TestStatement.TestStatementBuilder testStatementBuilder = TestStatement.builder()
                .target(TestStatement.Target.MEMORY)
                .targetAddress(targetAddress);
        return parseStatement(testStatementBuilder, parser);
    }

    private TestStatement parseStatement(TestStatement.TestStatementBuilder testStatementBuilder, Parser parser) {
        Expression value = parser.expression();
        String message = parseOptionalMessage(parser);
        parser.consumeEndOfLine();

        return testStatementBuilder
                .type(TestStatement.Type.ASSERT)
                .value(value)
                .message(message)
                .build();
    }

    private String parseOptionalMessage(Parser parser) {
        if (parser.peekNextTokenType() == TokenType.CHAR) {
            return parser.consumeString("Expected string");
        }
        return null;
    }
}
