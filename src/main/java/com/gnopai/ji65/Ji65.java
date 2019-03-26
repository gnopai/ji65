package com.gnopai.ji65;

import com.gnopai.ji65.assembler.*;
import com.gnopai.ji65.interpreter.InstructionExecutor;
import com.gnopai.ji65.interpreter.Interpreter;
import com.gnopai.ji65.interpreter.address.AddressingModeFactory;
import com.gnopai.ji65.interpreter.instruction.InstructionFactory;
import com.gnopai.ji65.linker.LabelResolver;
import com.gnopai.ji65.linker.Linker;
import com.gnopai.ji65.parser.ParseletFactory;
import com.gnopai.ji65.parser.Parser;
import com.gnopai.ji65.parser.TokenConsumer;
import com.gnopai.ji65.parser.expression.ExpressionEvaluator;
import com.gnopai.ji65.parser.statement.Statement;
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
        interpreter.run(program, cpu);
    }

    public Program assemble(String programText) {
        return Optional.of(programText)
                .map(this::scan)
                .map(this::parse)
                .map(this::assemble)
                .map(this::link)
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

    private AssembledSegments assemble(List<Statement> statements) {
        Environment environment = new Environment();
        Assembler assembler = new Assembler(
                new FirstPassResolver(new ExpressionEvaluator()),
                new InstructionAssembler(new ExpressionZeroPageChecker()));
        return assembler.assemble(statements, environment);
    }

    private Program link(AssembledSegments assembledSegments) {
        Linker linker = new Linker(new LabelResolver(), new ExpressionEvaluator());
        return linker.link(assembledSegments);
    }
}
