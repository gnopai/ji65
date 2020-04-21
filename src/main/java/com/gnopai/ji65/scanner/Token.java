package com.gnopai.ji65.scanner;

import lombok.Value;

@Value
public class Token {
    TokenType type;
    String lexeme;
    Object value;
    int line;
    SourceFile sourceFile;
}
