package com.gnopai.ji65.parser.statement;

import com.gnopai.ji65.parser.Parser;
import com.gnopai.ji65.parser.expression.Expression;
import com.gnopai.ji65.scanner.Token;
import com.gnopai.ji65.scanner.TokenType;

import java.util.ArrayList;
import java.util.List;

public class MacroStatementParselet implements StatementParselet {
    @Override
    public Statement parse(Token token, Parser parser) {
        String name = token.getLexeme();
        List<Expression> arguments = parseArguments(parser);
        return new MacroStatement(name, arguments);
    }

    private List<Expression> parseArguments(Parser parser) {
        List<Expression> arguments = new ArrayList<>();
        if (parser.match(TokenType.EOL)) {
            return arguments;
        }

        arguments.add(parser.expression());
        while (parser.match(TokenType.COMMA)) {
            arguments.add(parser.expression());
        }
        parser.consume(TokenType.EOL, "Expected end of line");
        return arguments;
    }
}
