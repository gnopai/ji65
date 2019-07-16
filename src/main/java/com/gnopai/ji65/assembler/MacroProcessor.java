package com.gnopai.ji65.assembler;

import com.gnopai.ji65.parser.statement.MacroStatement;
import com.gnopai.ji65.parser.statement.Statement;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class MacroProcessor {
    private final StatementValueSubstituter statementValueSubstituter;

    public MacroProcessor(StatementValueSubstituter statementValueSubstituter) {
        this.statementValueSubstituter = statementValueSubstituter;
    }

    public List<Statement> process(MacroStatement macroStatement, Environment environment) {
        Macro macro = environment.getMacro(macroStatement.getName())
                .orElseThrow(() -> new RuntimeException("Unknown macro referenced: " + macroStatement.getName()));

        return macro.getStatements().stream()
                .map(statement -> substituteArguments(statement, macro, macroStatement, environment))
                .collect(toList());

    }

    private Statement substituteArguments(Statement statement, Macro macro, MacroStatement macroStatement, Environment environment) {
        for (int i = 0; i < macro.getArgumentCount(); i++) {
            statement = statementValueSubstituter.substituteValuesInStatement(statement, macro.getArgumentName(i), macroStatement.getArgument(i), environment);
        }
        return statement;
    }
}
