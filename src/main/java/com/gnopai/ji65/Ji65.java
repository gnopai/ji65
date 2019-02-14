package com.gnopai.ji65;

import com.gnopai.ji65.address.AddressingModeFactory;
import com.gnopai.ji65.compiler.CompiledSegments;
import com.gnopai.ji65.compiler.Compiler;
import com.gnopai.ji65.compiler.ExpressionEvaluator;
import com.gnopai.ji65.compiler.InstructionCompiler;
import com.gnopai.ji65.instruction.InstructionFactory;
import com.gnopai.ji65.interpreter.InstructionExecutor;
import com.gnopai.ji65.interpreter.Interpreter;
import com.gnopai.ji65.linker.Linker;
import com.gnopai.ji65.parser.ParseletFactory;
import com.gnopai.ji65.parser.Parser;
import com.gnopai.ji65.parser.TokenConsumer;
import com.gnopai.ji65.parser.statement.Statement;
import com.gnopai.ji65.scanner.ErrorHandler;
import com.gnopai.ji65.scanner.Scanner;
import com.gnopai.ji65.scanner.Token;

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

    public Program compile(String programText) {
        return Optional.of(programText)
                .map(this::scan)
                .map(this::parse)
                .map(this::compile)
                .map(this::link)
                .orElseThrow();
    }

    private List<Token> scan(String programText) {
        return new Scanner(errorHandler).scan(programText);
    }

    private List<Statement> parse(List<Token> tokens) {
        Parser parser = new Parser(new ParseletFactory(), new TokenConsumer(errorHandler, tokens));
        return parser.parse();
    }

    private CompiledSegments compile(List<Statement> statements) {
        ExpressionEvaluator expressionEvaluator = new ExpressionEvaluator();
        Compiler compiler = new Compiler(new InstructionCompiler(expressionEvaluator), expressionEvaluator);
        return compiler.compile(statements);
    }

    private Program link(CompiledSegments compiledSegments) {
        Linker linker = new Linker();
        return linker.link(compiledSegments);
    }
}
