package com.gnopai.ji65.scanner;

public class ErrorPrinter implements ErrorHandler {
    private boolean hasError = false;

    @Override
    public void handleError(String message, int line) {
        hasError = true;
        System.out.printf("ERROR on line %d: %s\n", line, message);
    }

    public boolean hasError() {
        return hasError;
    }
}
