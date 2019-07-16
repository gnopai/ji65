package com.gnopai.ji65.parser.statement;

import com.gnopai.ji65.parser.Parser;
import com.gnopai.ji65.scanner.Token;
import com.gnopai.ji65.scanner.TokenType;

import java.util.ArrayList;
import java.util.List;

import static com.gnopai.ji65.DirectiveType.MACRO;
import static com.gnopai.ji65.DirectiveType.MACRO_END;

public class MacroDirectiveParser implements StatementParselet {
    @Override
    public Statement parse(Token token, Parser parser) {
        Token macroName = parser.consume(TokenType.IDENTIFIER, "Expected macro identifier");
        List<String> arguments = parseArguments(parser);
        List<Statement> statements = parseStatements(parser);

        return DirectiveStatement.builder()
                .type(MACRO)
                .name(macroName.getLexeme())
                .statements(statements)
                .arguments(arguments)
                .build();
    }

    private List<String> parseArguments(Parser parser) {
        List<String> arguments = new ArrayList<>();
        if (parser.match(TokenType.EOL)) {
            return arguments;
        }
        arguments.add(parseArgument(parser));

        while (parser.match(TokenType.COMMA)) {
            arguments.add(parseArgument(parser));
        }

        parser.consume(TokenType.EOL, "Expected end of line");
        return arguments;
    }

    private String parseArgument(Parser parser) {
        Token argument = parser.consume(TokenType.IDENTIFIER, "Expected macro argument");
        return argument.getLexeme();
    }

    private List<Statement> parseStatements(Parser parser) {
        List<Statement> statements = new ArrayList<>();
        while (!parser.matchDirective(MACRO_END)) {
            statements.add(parser.statement());
        }
        parser.consume(TokenType.EOL, "Expected end of line");
        return statements;
    }
}
