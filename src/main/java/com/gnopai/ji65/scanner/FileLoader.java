package com.gnopai.ji65.scanner;

import com.google.common.primitives.Bytes;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

public class FileLoader {

    public Optional<SourceFile> loadSourceFile(File file) {
        try {
            return loadSourceFile(file.toPath());
        } catch (Exception e) {
            // TODO error?
            return Optional.empty();
        }
    }

    public Optional<SourceFile> loadSourceFile(String fileName) {
        try {
            return loadSourceFile(Paths.get(fileName));
        } catch (Exception e) {
            // TODO error?
            return Optional.empty();
        }
    }

    private Optional<SourceFile> loadSourceFile(Path path) throws Exception {
        List<String> lines = Files.readAllLines(path);
        String text = String.join("\n", lines);
        return Optional.of(new SourceFile(path, text));
    }

    public Optional<List<Byte>> loadBinaryFile(String fileName) {
        try {
            Path path = Paths.get(fileName);
            byte[] bytes = Files.readAllBytes(path);
            return Optional.ofNullable(Bytes.asList(bytes));
        } catch (IOException e) {
            // TODO error?
            return Optional.empty();
        }
    }
}
