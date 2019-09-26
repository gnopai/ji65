package com.gnopai.ji65.scanner;

import lombok.Value;

import java.nio.file.Path;

@Value
public class SourceFile {
    Path path;
    String text;
}
