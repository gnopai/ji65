package com.gnopai.ji65;

import com.gnopai.ji65.util.ErrorHandler;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;

import javax.inject.Singleton;

public class Ji65Module extends AbstractModule {
    private final ErrorHandler errorHandler;

    public Ji65Module(ErrorHandler errorHandler) {
        this.errorHandler = errorHandler;
    }

    @Provides
    @Singleton
    public ErrorHandler errorHandler() {
        return errorHandler;
    }
}
