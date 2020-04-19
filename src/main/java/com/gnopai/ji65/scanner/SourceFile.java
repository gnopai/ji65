package com.gnopai.ji65.scanner;

import lombok.Value;

import java.nio.file.Path;
import java.util.Optional;

@Value
public class SourceFile {
    Path path;
    String text;

    public Path getNormalizedPath() {
        return Optional.ofNullable(path)
                .map(Path::normalize)
                .orElse(null);
    }
}
