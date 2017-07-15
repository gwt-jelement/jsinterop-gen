package com.tenxdev.jsinterop.generator.errors;

import java.io.PrintStream;

public class DevNullErrorrHandler extends AbstractErrorHandler {

    @Override
    public void reportError(String error) {
        // do nothing
    }

    @Override
    public void formatError(String format, Object... args) {
        // do nothing
    }

}
