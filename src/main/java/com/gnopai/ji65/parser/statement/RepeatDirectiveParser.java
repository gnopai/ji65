package com.gnopai.ji65.parser.statement;

import com.gnopai.ji65.parser.Parser;
import com.gnopai.ji65.parser.expression.Expression;
import com.gnopai.ji65.scanner.Token;
import com.gnopai.ji65.scanner.TokenType;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.gnopai.ji65.DirectiveType.REPEAT;
import static com.gnopai.ji65.DirectiveType.REPEAT_END;
import static java.util.stream.Collectors.toList;

public class RepeatDirectiveParser implements StatementParselet {
    @Override
    public Statement parse(Token token, Parser parser) {
        Expression countExpression = parser.expression();
        Optional<String> indexIdentifierArgument = parseIndexIdentifier(parser);
        parser.consume(TokenType.EOL, "Expected end of line");

        List<Statement> statements = new ArrayList<>();
        while (!parser.matchDirective(REPEAT_END)) {
            statements.add(parser.statement());
        }
        parser.consume(TokenType.EOL, "Expected end of line");

        return DirectiveStatement.builder()
                .type(REPEAT)
                .expression(countExpression)
                .statements(statements)
                .arguments(indexIdentifierArgument.stream().collect(toList()))
                .build();
    }

    private Optional<String> parseIndexIdentifier(Parser parser) {
        if (parser.match(TokenType.COMMA)) {
            Token argument = parser.consume(TokenType.IDENTIFIER, "Expected repeat index identifier");
            return Optional.of(argument.getLexeme());
        }
        return Optional.empty();
    }
}
