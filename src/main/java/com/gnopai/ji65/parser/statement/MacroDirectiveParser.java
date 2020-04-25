package com.gnopai.ji65.parser.statement;

import com.gnopai.ji65.parser.Macro;
import com.gnopai.ji65.parser.Parser;
import com.gnopai.ji65.parser.ParsingService;
import com.gnopai.ji65.scanner.Token;
import com.gnopai.ji65.scanner.TokenType;

import java.util.ArrayList;
import java.util.List;

import static com.gnopai.ji65.DirectiveType.MACRO;
import static com.gnopai.ji65.DirectiveType.MACRO_END;
import static com.gnopai.ji65.scanner.TokenType.DIRECTIVE;
import static com.gnopai.ji65.scanner.TokenType.EOF;

public class MacroDirectiveParser implements StatementParselet {
    private final ParsingService parsingService;

    public MacroDirectiveParser(ParsingService parsingService) {
        this.parsingService = parsingService;
    }

    @Override
    public Statement parse(Token token, Parser parser) {
        Token macroName = parser.consume(TokenType.IDENTIFIER, "Expected macro identifier");
        List<String> arguments = parseArguments(parser);
        List<Token> tokens = parseTokens(parser);

        Macro macro = new Macro(macroName.getLexeme(), tokens, arguments);
        parsingService.defineMacro(macro);

        return DirectiveStatement.builder()
                .type(MACRO)
                .name(macroName.getLexeme())
                .build();
    }

    private List<String> parseArguments(Parser parser) {
        List<String> arguments = new ArrayList<>();
        if (parser.matchEndOfLine()) {
            return arguments;
        }
        arguments.add(parseArgument(parser));

        while (parser.match(TokenType.COMMA)) {
            arguments.add(parseArgument(parser));
        }

        parser.consumeEndOfLine();
        return arguments;
    }

    private String parseArgument(Parser parser) {
        Token argument = parser.consume(TokenType.IDENTIFIER, "Expected macro argument");
        return argument.getLexeme();
    }

    private List<Token> parseTokens(Parser parser) {
        List<Token> tokens = new ArrayList<>();
        Token token = parser.consume();
        while (!isEndOfMacro(token)) {
            tokens.add(token);
            token = parser.consume();
        }

        if (!MACRO_END.equals(token.getValue())) {
            throw parser.error("Expected macro end");
        }

        parser.consumeEndOfLine();
        return tokens;
    }

    private boolean isEndOfMacro(Token token) {
        TokenType tokenType = token.getType();
        if (EOF.equals(tokenType)) {
            return true;
        }
        return DIRECTIVE.equals(tokenType) && MACRO_END.equals(token.getValue());
    }
}
