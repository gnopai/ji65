package com.gnopai.ji65;

import com.gnopai.ji65.assembler.*;
import com.gnopai.ji65.config.ProgramConfig;
import com.gnopai.ji65.interpreter.InstructionExecutor;
import com.gnopai.ji65.interpreter.Interpreter;
import com.gnopai.ji65.interpreter.NeverEndProgram;
import com.gnopai.ji65.interpreter.ProgramEndStrategy;
import com.gnopai.ji65.interpreter.address.AddressingModeFactory;
import com.gnopai.ji65.interpreter.instruction.InstructionFactory;
import com.gnopai.ji65.linker.LabelResolver;
import com.gnopai.ji65.linker.Linker;
import com.gnopai.ji65.linker.SegmentAddressCalculator;
import com.gnopai.ji65.linker.SegmentMapper;
import com.gnopai.ji65.parser.ParseletFactory;
import com.gnopai.ji65.parser.Parser;
import com.gnopai.ji65.parser.TokenConsumer;
import com.gnopai.ji65.parser.expression.ExpressionEvaluator;
import com.gnopai.ji65.parser.statement.Statement;
import com.gnopai.ji65.scanner.FileLoader;
import com.gnopai.ji65.scanner.Scanner;
import com.gnopai.ji65.scanner.Token;
import com.gnopai.ji65.scanner.TokenReader;
import com.gnopai.ji65.util.ErrorHandler;

import java.util.List;
import java.util.Optional;

public class Ji65 {
    private final Interpreter interpreter;
    private final ErrorHandler errorHandler;

    public Ji65(ErrorHandler errorHandler) {
        this.errorHandler = errorHandler;
        interpreter = new Interpreter(
                new InstructionExecutor(
                        new InstructionFactory(),
                        new AddressingModeFactory()
                )
        );
    }

    public void run(Program program, Cpu cpu) {
        run(program, cpu, new NeverEndProgram());
    }

    public void run(Program program, Cpu cpu, ProgramEndStrategy programEndStrategy) {
        interpreter.run(program, cpu, programEndStrategy);
    }

    public Program assemble(String programText, ProgramConfig programConfig) {
        return Optional.of(programText)
                .map(this::scan)
                .map(this::parse)
                .map(statements -> assemble(statements, programConfig))
                .map(assembledSegments -> link(assembledSegments, programConfig))
                .orElseThrow();
    }

    private List<Token> scan(String programText) {
        Scanner scanner = new Scanner(new TokenReader(errorHandler));
        return scanner.scan(programText);
    }

    private List<Statement> parse(List<Token> tokens) {
        Parser parser = new Parser(new ParseletFactory(), new TokenConsumer(errorHandler, tokens));
        return parser.parse();
    }

    private AssembledSegments assemble(List<Statement> statements, ProgramConfig programConfig) {
        Environment environment = new Environment();
        ExpressionEvaluator expressionEvaluator = new ExpressionEvaluator();
        StatementValueSubstituter statementValueSubstituter = new StatementValueSubstituter(new ExpressionValueSubstituter());
        Assembler assembler = new Assembler(
                new FirstPassResolver(expressionEvaluator),
                new InstructionAssembler(new ExpressionZeroPageChecker()),
                new DirectiveDataAssembler(expressionEvaluator, new FileLoader()),
                new RepeatDirectiveProcessor(
                        statementValueSubstituter,
                        expressionEvaluator
                ),
                new MacroProcessor(statementValueSubstituter));
        return assembler.assemble(statements, programConfig, environment);
    }

    private Program link(AssembledSegments assembledSegments, ProgramConfig programConfig) {
        Linker linker = new Linker(
                new SegmentMapper(new SegmentAddressCalculator()),
                new LabelResolver(),
                new ExpressionEvaluator());
        return linker.link(assembledSegments, programConfig);
    }
}
