package com.tenxdev.jsinterop.generator.errors;

public abstract class AbstractErrorHandler implements ErrorReporter{
    @Override
    public void reportFatalError(String error) {
        reportError(error);
        System.exit(1);
    }

    @Override
    public void formatFatalError(String format, Object... args) {
        formatError(format,args);
        System.exit(1);
    }
}
