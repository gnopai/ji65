package com.gnopai.ji65.parser.statement;

import com.gnopai.ji65.SourceFileProcessor;
import com.gnopai.ji65.assembler.Macro;
import com.gnopai.ji65.parser.Parser;
import com.gnopai.ji65.scanner.Token;
import com.gnopai.ji65.scanner.TokenType;

import java.util.*;
import java.util.stream.IntStream;

import static com.gnopai.ji65.scanner.TokenType.*;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

public class MacroCallParselet implements StatementParselet {
    private final SourceFileProcessor sourceFileProcessor;

    public MacroCallParselet(SourceFileProcessor sourceFileProcessor) {
        this.sourceFileProcessor = sourceFileProcessor;
    }

    @Override
    public Statement parse(Token token, Parser parser) {
        Macro macro = getMacro(token.getLexeme(), parser);
        List<List<Token>> arguments = parseArguments(parser);
        List<Token> tokens = substituteArguments(macro, arguments);
        List<Statement> statements = sourceFileProcessor.parseTokensFromCurrentFile(tokens);
        return new MultiStatement(statements);
    }

    private Macro getMacro(String name, Parser parser) {
        return sourceFileProcessor.getMacro(name)
                .orElseThrow(() -> parser.error("Unknown macro referenced: " + name));
    }

    private List<List<Token>> parseArguments(Parser parser) {
        List<List<Token>> allArguments = new ArrayList<>();
        List<Token> argumentTokens = new ArrayList<>();
        boolean keepGoing = true;
        boolean previousTokenIsComma = false;
        while (keepGoing) {
            Token token = parser.consume();
            switch (token.getType()) {
                case LEFT_BRACE:
                    if (argumentTokens.isEmpty()) {
                        argumentTokens = parseArgumentInBraces(parser);
                    } else {
                        argumentTokens.add(token);
                    }
                    break;
                case COMMA:
                    allArguments.add(argumentTokens);
                    argumentTokens = new ArrayList<>();
                    break;
                case EOL:
                    if (previousTokenIsComma) {
                        // the line ended in a comma, which means more args on the next line
                        break;
                    }
                    allArguments.add(argumentTokens);
                    keepGoing = false;
                    break;
                case EOF:
                    allArguments.add(argumentTokens);
                    keepGoing = false;
                    break;
                default:
                    argumentTokens.add(token);
            }

            previousTokenIsComma = TokenType.COMMA.equals(token.getType());
        }
        
        return allArguments;
    }

    private List<Token> parseArgumentInBraces(Parser parser) {
        List<Token> argumentTokens = new ArrayList<>();
        Token argToken = parser.consume();

        Set<TokenType> endTokenTypes = EnumSet.of(RIGHT_BRACE, EOL, EOF);

        while (!endTokenTypes.contains(argToken.getType())) {
            argumentTokens.add(argToken);
            argToken = parser.consume();
        }

        if (!argToken.getType().equals(RIGHT_BRACE)) {
            throw parser.error("Expected end brace at end of macro argument");
        }

        return argumentTokens;
    }

    private List<Token> substituteArguments(Macro macro, List<List<Token>> arguments) {
        Map<String, List<Token>> argumentTokens = IntStream.range(0, macro.getArgumentCount())
                .boxed()
                .collect(toMap(macro::getArgumentName, i -> i < arguments.size() ? arguments.get(i) : List.of()));

        return macro.getTokens().stream()
                .flatMap(token -> substituteArgument(token, argumentTokens).stream())
                .collect(toList());
    }

    private List<Token> substituteArgument(Token token, Map<String, List<Token>> argumentTokens) {
        return Optional.of(token)
                .filter(t -> TokenType.IDENTIFIER.equals(t.getType()))
                .map(Token::getLexeme)
                .map(argumentTokens::get)
                .orElse(List.of(token));
    }
}
