package com.gnopai.ji65.scanner;

import com.google.common.primitives.Bytes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

public class FileLoader {

    public Optional<SourceFile> loadSourceFile(String fileName) {
        try {
            List<String> lines = Files.readAllLines(Paths.get(fileName));
            String text = String.join("\n", lines);
            return Optional.of(new SourceFile(fileName, text));
        } catch (IOException e) {
            // TODO error?
            return Optional.empty();
        }
    }

    public Optional<List<Byte>> loadBinaryFile(String fileName) {
        try {
            byte[] bytes = Files.readAllBytes(Paths.get(fileName));
            return Optional.ofNullable(Bytes.asList(bytes));
        } catch (IOException e) {
            // TODO error?
            return Optional.empty();
        }
    }
}
