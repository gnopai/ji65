package com.gnopai.ji65.parser;

// TODO fill this in
public enum Precedence {
    PRIMARY;

    public static Precedence lowest() {
        return values()[0];
    }
}
