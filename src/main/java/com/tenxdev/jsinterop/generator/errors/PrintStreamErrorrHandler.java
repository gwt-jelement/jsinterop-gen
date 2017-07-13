package com.tenxdev.jsinterop.generator.errors;

import com.tenxdev.jsinterop.generator.errors.ErrorReporter;

import java.io.PrintStream;

public class PrintStreamErrorrHandler extends AbstractErrorHandler{

    private final PrintStream printStream;

    public PrintStreamErrorrHandler(PrintStream printStream) {
        this.printStream=printStream;
    }

    @Override
    public void reportError(String error) {
        printStream.println(error);
    }

    @Override
    public void formatError(String format, Object... args) {
        printStream.format(format, args);
    }

}
