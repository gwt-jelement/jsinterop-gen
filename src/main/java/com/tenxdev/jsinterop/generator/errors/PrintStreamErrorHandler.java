package com.tenxdev.jsinterop.generator.errors;

import java.io.PrintStream;

public class PrintStreamErrorHandler extends AbstractErrorHandler {

    private final PrintStream printStream;

    public PrintStreamErrorHandler(PrintStream printStream) {
        this.printStream = printStream;
    }

    @Override
    public void reportError(String error) {
        printStream.println(error);
    }

    @Override
    public void formatError(String format, Object... args) {
        printStream.format(format, args);
    }

    @Override
    public PrintStream getPrintStream() {
        return printStream;
    }

}
