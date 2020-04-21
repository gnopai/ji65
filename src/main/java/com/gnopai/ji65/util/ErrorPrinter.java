package com.gnopai.ji65.util;

import com.gnopai.ji65.scanner.SourceFile;

public class ErrorPrinter implements ErrorHandler {
    private boolean hasError = false;

    @Override
    public void handleError(String message, SourceFile sourceFile, int line) {
        hasError = true;
        System.out.printf("ERROR at %s:%d: %s\n", sourceFile.getNormalizedPath(), line, message);
    }

    public boolean hasError() {
        return hasError;
    }
}
