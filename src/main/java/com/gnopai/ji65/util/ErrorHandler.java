package com.gnopai.ji65.util;

import com.gnopai.ji65.scanner.SourceFile;

public interface ErrorHandler {
    void handleError(String message, SourceFile sourceFile, int line);
}
