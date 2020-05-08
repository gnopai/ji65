package com.gnopai.ji65.parser.statement;

import com.gnopai.ji65.assembler.Environment;
import com.gnopai.ji65.parser.expression.Expression;
import com.gnopai.ji65.test.Target;
import com.gnopai.ji65.test.TestMaker;
import com.gnopai.ji65.test.TestStep;
import com.gnopai.ji65.test.TestStepType;
import lombok.Builder;
import lombok.Singular;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class TestStatement implements Statement {
    TestStepType type;
    Target target;
    Expression targetAddress;
    @Singular
    List<Expression> values;
    String message;

    @Override
    public <T> T accept(StatementVisitor<T> statementVisitor) {
        return statementVisitor.visit(this);
    }

    public Expression getFirstValue() {
        return values.get(0);
    }

    public TestStep makeTestStep(TestMaker testMaker, Environment environment) {
        return type.makeTestStep(testMaker, this, environment);
    }

}
