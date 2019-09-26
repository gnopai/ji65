package com.gnopai.ji65.assembler;

import com.gnopai.ji65.parser.expression.Expression;
import com.gnopai.ji65.parser.statement.*;

import javax.inject.Inject;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class StatementValueSubstituter implements StatementVisitor<Statement> {
    private final ExpressionValueSubstituter expressionValueSubstituter;
    private String name;
    private Expression value;
    private Environment environment;

    @Inject
    public StatementValueSubstituter(ExpressionValueSubstituter expressionValueSubstituter) {
        this.expressionValueSubstituter = expressionValueSubstituter;
    }

    public Statement substituteValuesInStatement(Statement statement, String name, Expression value, Environment environment) {
        this.name = name;
        this.value = value;
        this.environment = environment;
        return substituteValuesInStatement(statement);
    }

    private Statement substituteValuesInStatement(Statement statement) {
        return statement.accept(this);
    }

    @Override
    public Statement visit(InstructionStatement instructionStatement) {
        return instructionStatement.withAddressExpression(
                substituteExpression(instructionStatement.getAddressExpression())
        );
    }

    @Override
    public Statement visit(LabelStatement labelStatement) {
        return labelStatement;
    }

    @Override
    public Statement visit(UnnamedLabelStatement unnamedLabelStatement) {
        return unnamedLabelStatement;
    }

    @Override
    public Statement visit(LocalLabelStatement localLabelStatement) {
        return localLabelStatement;
    }

    @Override
    public Statement visit(DirectiveStatement directiveStatement) {
        return directiveStatement
                .withExpressions(substituteExpressions(directiveStatement.getExpressions()))
                .withStatements(substituteValuesInStatements(directiveStatement.getStatements()));
    }

    @Override
    public Statement visit(AssignmentStatement assignmentStatement) {
        return assignmentStatement.withExpression(
                substituteExpression(assignmentStatement.getExpression())
        );
    }

    @Override
    public Statement visit(MacroStatement macroStatement) {
        return macroStatement.withArguments(substituteExpressions(macroStatement.getArguments()));
    }

    private List<Statement> substituteValuesInStatements(List<Statement> statements) {
        return statements.stream()
                .map(this::substituteValuesInStatement)
                .collect(toList());
    }

    private List<Expression> substituteExpressions(List<Expression> expressions) {
        return expressions.stream()
                .map(this::substituteExpression)
                .collect(toList());
    }

    private Expression substituteExpression(Expression expression) {
        if (expression == null) {
            return null;
        }
        return expressionValueSubstituter.substituteValue(name, value, expression, environment);
    }
}
