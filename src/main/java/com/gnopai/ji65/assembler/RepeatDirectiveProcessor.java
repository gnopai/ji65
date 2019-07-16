package com.gnopai.ji65.assembler;

import com.gnopai.ji65.parser.expression.ExpressionEvaluator;
import com.gnopai.ji65.parser.expression.PrimaryExpression;
import com.gnopai.ji65.parser.statement.DirectiveStatement;
import com.gnopai.ji65.parser.statement.Statement;
import com.gnopai.ji65.scanner.TokenType;

import java.util.List;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;

public class RepeatDirectiveProcessor {
    private final StatementValueSubstituter statementValueSubstituter;
    private final ExpressionEvaluator expressionEvaluator;

    public RepeatDirectiveProcessor(StatementValueSubstituter statementValueSubstituter, ExpressionEvaluator expressionEvaluator) {
        this.statementValueSubstituter = statementValueSubstituter;
        this.expressionEvaluator = expressionEvaluator;
    }

    public List<Statement> process(DirectiveStatement directiveStatement, Environment environment) {
        int count = expressionEvaluator.evaluate(directiveStatement.getExpression(), environment);
        return IntStream.range(0, count)
                .mapToObj(i -> getStatementsForIteration(directiveStatement, environment, i))
                .flatMap(List::stream)
                .collect(toList());
    }

    private List<Statement> getStatementsForIteration(DirectiveStatement directiveStatement, Environment environment, int i) {
        if (!directiveStatement.hasArguments()) {
            return directiveStatement.getStatements();
        }
        String argumentName = directiveStatement.getArgument(0);

        return directiveStatement.getStatements().stream()
                .map(statement -> {
                            PrimaryExpression indexValue = new PrimaryExpression(TokenType.NUMBER, i);
                            return statementValueSubstituter.substituteValuesInStatement(statement, argumentName, indexValue, environment);
                        }
                )
                .collect(toList());
    }
}
