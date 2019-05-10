package com.gnopai.ji65.scanner;

public enum TokenType {
    IDENTIFIER,
    STRING,
    CHAR,
    NUMBER,
    INSTRUCTION,
    DIRECTIVE,
    X,
    Y,
    A,

    LEFT_PAREN,
    RIGHT_PAREN,
    LEFT_BRACE,
    RIGHT_BRACE,
    COMMA,
    MINUS,
    PLUS,
    SLASH,
    STAR,
    PIPE,
    AMPERSAND,
    SEMICOLON,
    COLON,
    POUND,
    EQUAL,
    CARET,
    TILDE,
    GREATER_THAN,
    LESS_THAN,
    SHIFT_LEFT,
    SHIFT_RIGHT,

    // TODO boolean operators? (!, <>, =, <=, >=, <, >, &&, ||)

    EOL,
    EOF
}
