package com.gnopai.ji65;

import com.gnopai.ji65.assembler.Assembler;
import com.gnopai.ji65.assembler.Environment;
import com.gnopai.ji65.config.ProgramConfig;
import com.gnopai.ji65.interpreter.Interpreter;
import com.gnopai.ji65.interpreter.NeverEndProgram;
import com.gnopai.ji65.interpreter.ProgramEndStrategy;
import com.gnopai.ji65.linker.Linker;
import com.gnopai.ji65.scanner.SourceFile;
import com.gnopai.ji65.util.ErrorHandler;
import com.google.inject.Guice;
import com.google.inject.Injector;

import java.util.Optional;

public class Ji65 {
    private final SourceFileProcessor sourceFileProcessor;
    private final Assembler assembler;
    private final Linker linker;
    private final Interpreter interpreter;

    public Ji65(ErrorHandler errorHandler) {
        Injector injector = Guice.createInjector(new Ji65Module(errorHandler));
        sourceFileProcessor = injector.getInstance(SourceFileProcessor.class);
        assembler = injector.getInstance(Assembler.class);
        linker = injector.getInstance(Linker.class);
        interpreter = injector.getInstance(Interpreter.class);
    }

    public void run(Program program, Cpu cpu) {
        run(program, cpu, new NeverEndProgram());
    }

    public void run(Program program, Cpu cpu, ProgramEndStrategy programEndStrategy) {
        interpreter.run(program, cpu, programEndStrategy);
    }

    public Program assemble(SourceFile sourceFile, ProgramConfig programConfig) {
        Environment environment = new Environment();
        return Optional.of(sourceFile)
                .map(sourceFileProcessor::parse)
                .map(statements -> assembler.assemble(statements, programConfig, environment))
                .map(assembledSegments -> linker.link(assembledSegments, programConfig))
                .orElseThrow();
    }
}
