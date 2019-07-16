package com.gnopai.ji65.assembler;

import com.gnopai.ji65.parser.statement.DirectiveStatement;
import com.gnopai.ji65.parser.statement.Statement;
import lombok.Value;

import java.util.List;

@Value
public class Macro {
    String name;
    List<Statement> statements;
    List<String> argumentNames;

    public static Macro of(DirectiveStatement directiveStatement) {
        return new Macro(directiveStatement.getName(), directiveStatement.getStatements(), directiveStatement.getArguments());
    }

    public int getArgumentCount() {
        return argumentNames.size();
    }

    public String getArgumentName(int i) {
        return argumentNames.get(i);
    }
}
