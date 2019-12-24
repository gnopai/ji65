package com.gnopai.ji65.parser.statement;

import com.gnopai.ji65.parser.Parser;
import com.gnopai.ji65.scanner.Token;
import com.gnopai.ji65.scanner.TokenType;

import java.util.ArrayList;
import java.util.List;

import static com.gnopai.ji65.DirectiveType.TEST;
import static com.gnopai.ji65.DirectiveType.TEST_END;

public class TestDirectiveParser implements StatementParselet {
    @Override
    public Statement parse(Token token, Parser parser) {
        Token testName = parser.consume(TokenType.IDENTIFIER, "Expected test identifier");
        parser.consumeEndOfLine();
        List<Statement> statements = parseStatements(parser);

        return DirectiveStatement.builder()
                .type(TEST)
                .name(testName.getLexeme())
                .statements(statements)
                .build();
    }

    private List<Statement> parseStatements(Parser parser) {
        List<Statement> statements = new ArrayList<>();
        while (!parser.matchDirective(TEST_END)) {
            statements.add(parseStatement(parser));
        }
        parser.consumeEndOfLine();
        return statements;
    }

    private Statement parseStatement(Parser parser) {
        // TODO use separate test parser that's more specific? Maybe get rid of the instanceof?
        // This would also be helpful for making sure we only parse test statements inside test blocks, and not elsewhere.
        Statement statement = parser.statement();
        if (!(statement instanceof TestStatement)) {
            throw parser.error("Only test statements are allowed in test blocks");
        }
        return statement;
    }
}
