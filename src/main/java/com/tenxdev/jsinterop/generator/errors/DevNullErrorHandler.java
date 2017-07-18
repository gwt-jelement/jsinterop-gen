package com.tenxdev.jsinterop.generator.errors;

public class DevNullErrorHandler extends AbstractErrorHandler {

    @Override
    public void reportError(String error) {
        // do nothing
    }

    @Override
    public void formatError(String format, Object... args) {
        // do nothing
    }

}
