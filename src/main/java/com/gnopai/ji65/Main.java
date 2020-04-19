package com.gnopai.ji65;

import com.gnopai.ji65.config.*;
import com.gnopai.ji65.scanner.FileLoader;
import com.gnopai.ji65.scanner.SourceFile;
import com.gnopai.ji65.test.StdoutTestReporter;
import com.gnopai.ji65.test.TestReport;
import com.gnopai.ji65.util.ErrorHandler;
import com.gnopai.ji65.util.ErrorPrinter;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

import java.io.File;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Callable;

@Command(
        name = "ji65",
        description = "Assemble and test 6502 code",
        versionProvider = com.gnopai.ji65.Main.ManifestVersionProvider.class,
        mixinStandardHelpOptions = true
)
public class Main implements Callable<Integer> {

    @Parameters(index = "0", paramLabel = "FILE", description = "the file to assemble and run tests for")
    private File sourceFile;

    @Option(names = "-C", paramLabel = "CONFIG_FILE", description = "the program config file to use")
    private File programConfigFile;

    @Option(names = "-p", description = "display passing tests and assertions")
    private boolean showPassingTests;

    public static void main(String[] args) {
        Main main = new Main();
        int exitCode = new CommandLine(main).execute(args);
        System.exit(exitCode);
    }

    @Override
    public Integer call() {
        ErrorPrinter errorHandler = new ErrorPrinter();

        SourceFile programFile = loadFile(sourceFile);
        ProgramConfig programConfig = readProgramConfig(errorHandler);

        Ji65 ji65 = new Ji65(errorHandler);
        Program program = ji65.assemble(programFile, programConfig);

        TestReport testReport = ji65.runTests(program);

        StdoutTestReporter.builder()
                .showPassingTests(showPassingTests)
                .build()
                .reportResults(testReport);

        return testReport.allTestsPassed() ? 0 : 1;
    }

    private SourceFile loadFile(File file) {
        return new FileLoader().loadSourceFile(file.toPath())
                .orElseThrow(() -> new RuntimeException("Error loading file: " + file));
    }

    private ProgramConfig readProgramConfig(ErrorHandler errorHandler) {
        ConfigReader configReader = new ConfigReader(errorHandler);

        return Optional.ofNullable(programConfigFile)
                .map(this::loadFile)
                .map(configReader::read)
                .orElse(getDefaultProgramConfig());
    }

    private ProgramConfig getDefaultProgramConfig() {
        Address programStartAddress = new Address(0x8000);
        return ProgramConfig.builder()
                .memoryConfigs(List.of(
                        MemoryConfig.builder()
                                .name("ZP")
                                .startAddress(new Address(0))
                                .size(0x100)
                                .memoryType(MemoryType.READ_WRITE)
                                .build(),
                        MemoryConfig.builder()
                                .name("PRG")
                                .startAddress(programStartAddress)
                                .size(0x8000)
                                .memoryType(MemoryType.READ_ONLY)
                                .build()
                ))
                .segmentConfigs(List.of(
                        SegmentConfig.builder()
                                .segmentName("ZEROPAGE")
                                .memoryConfigName("ZP")
                                .segmentType(SegmentType.ZERO_PAGE)
                                .build(),
                        SegmentConfig.builder()
                                .segmentName("CODE")
                                .memoryConfigName("PRG")
                                .segmentType(SegmentType.READ_ONLY)
                                .startAddress(programStartAddress)
                                .build()
                ))
                .build();
    }

    public static class ManifestVersionProvider implements CommandLine.IVersionProvider {
        @Override
        public String[] getVersion() {
            String version = Main.class.getPackage().getImplementationVersion();
            return new String[]{ version };
        }
    }
}
