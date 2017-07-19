package com.tenxdev.jsinterop.generator.logging;

import java.io.PrintStream;
import java.util.function.Supplier;

public class PrintStreamLogger implements Logger {

    private final PrintStream printStream;
    private int logLevel;

    public PrintStreamLogger(PrintStream printStream) {
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

    @Override
    public void setLogLevel(int logLevel) {
        this.logLevel = logLevel;
    }

    @Override
    public void info(int level, Supplier<String> infoSupplier) {
        if (level <= logLevel) {
            printStream.println(infoSupplier.get());
        }
    }


}
