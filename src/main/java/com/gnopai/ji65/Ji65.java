package com.gnopai.ji65;

import com.gnopai.ji65.assembler.Assembler;
import com.gnopai.ji65.assembler.Environment;
import com.gnopai.ji65.config.ProgramConfig;
import com.gnopai.ji65.interpreter.Interpreter;
import com.gnopai.ji65.interpreter.NeverEndProgram;
import com.gnopai.ji65.interpreter.ProgramEndStrategy;
import com.gnopai.ji65.linker.Linker;
import com.gnopai.ji65.parser.ParsingService;
import com.gnopai.ji65.scanner.SourceFile;
import com.gnopai.ji65.test.TestReport;
import com.gnopai.ji65.test.TestResult;
import com.gnopai.ji65.test.TestRunner;
import com.gnopai.ji65.util.ErrorHandler;
import com.google.inject.Guice;
import com.google.inject.Injector;

import java.util.List;
import java.util.Optional;

public class Ji65 {
    private final ParsingService parsingService;
    private final Assembler assembler;
    private final Linker linker;
    private final Interpreter interpreter;
    private final TestRunner testRunner;

    public Ji65(ErrorHandler errorHandler) {
        Injector injector = Guice.createInjector(new Ji65Module(errorHandler));
        parsingService = injector.getInstance(ParsingService.class);
        assembler = injector.getInstance(Assembler.class);
        linker = injector.getInstance(Linker.class);
        interpreter = injector.getInstance(Interpreter.class);
        testRunner = injector.getInstance(TestRunner.class);
    }

    public void run(Program program, Address startAddress, Cpu cpu) {
        run(program, startAddress, cpu, new NeverEndProgram());
    }

    public void run(Program program, Address startAddress, Cpu cpu, ProgramEndStrategy programEndStrategy) {
        cpu.load(program);
        interpreter.run(startAddress, cpu, programEndStrategy);
    }

    public TestReport runTests(Program program) {
        List<TestResult> testResults = testRunner.runTests(program);
        return new TestReport(testResults);
    }

    public Program assemble(SourceFile sourceFile, ProgramConfig programConfig) {
        Environment environment = new Environment();
        return Optional.of(sourceFile)
                .map(parsingService::loadAndParse)
                .map(statements -> assembler.assemble(statements, programConfig, environment))
                .map(assembledSegments -> linker.link(assembledSegments, programConfig))
                .orElseThrow();
    }
}
