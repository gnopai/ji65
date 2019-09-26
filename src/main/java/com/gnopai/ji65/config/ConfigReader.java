package com.gnopai.ji65.config;

import com.gnopai.ji65.parser.TokenStream;
import com.gnopai.ji65.scanner.Token;
import com.gnopai.ji65.scanner.TokenReader;
import com.gnopai.ji65.util.ErrorHandler;

import java.util.List;
import java.util.Optional;

public class ConfigReader {
    private final ErrorHandler errorHandler;

    public ConfigReader(ErrorHandler errorHandler) {
        this.errorHandler = errorHandler;
    }

    public ProgramConfig read(String configText) {
        return Optional.of(configText)
                .map(this::scan)
                .map(this::parse)
                .map(this::interpret)
                .orElseThrow();
    }

    private List<Token> scan(String configText) {
        ConfigScanner configScanner = new ConfigScanner(new TokenReader(errorHandler));
        return configScanner.scan(configText);
    }

    private List<ConfigBlock> parse(List<Token> tokens) {
        ConfigParser configParser = new ConfigParser(new TokenStream(errorHandler, tokens));
        return configParser.parse();
    }

    private ProgramConfig interpret(List<ConfigBlock> blocks) {
        return new ConfigInterpreter().interpret(blocks);
    }
}
