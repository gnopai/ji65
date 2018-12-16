package com.gnopai.ji65.scanner;

public enum TokenType {
    IDENTIFIER,
    STRING,
    NUMBER,
    INSTRUCTION,
    DIRECTIVE,
    X,
    Y,
    A,

    LEFT_PAREN,
    RIGHT_PAREN,
    COMMA,
    MINUS,
    PLUS,
    SLASH,
    STAR,
    PIPE,
    AMPERSAND,
    AT_SIGN,
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

    EOF
}
