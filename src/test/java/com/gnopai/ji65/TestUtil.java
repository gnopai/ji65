package com.gnopai.ji65;

import com.gnopai.ji65.util.ErrorPrinter;

import java.util.Arrays;
import java.util.stream.Collectors;

public class TestUtil {

    public static void compileAndRun(Cpu cpu, String... lines) {
        String programText = Arrays.stream(lines)
                .map(line -> line + "\n")
                .collect(Collectors.joining(""));
        Ji65 ji65 = new Ji65(new ErrorPrinter());
        Program program = ji65.compile(programText);
        ji65.run(program, cpu);
    }
}
