package com.tenxdev.jsinterop.generator.errors;

public interface ErrorReporter {
    void reportError(String error);
    void formatError(String format, Object... args);
    void reportFatalError(String error);
    void formatFatalError(String format, Object... args);
}
