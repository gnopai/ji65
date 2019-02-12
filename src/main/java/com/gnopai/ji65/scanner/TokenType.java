package com.gnopai.ji65.scanner;

public enum TokenType {
    IDENTIFIER,
    STRING, // TODO I think these need to be chars, not strings...since it's one-char-per byte you know
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

    EOL,
    EOF
}
