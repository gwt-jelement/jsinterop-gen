package com.tenxdev.jsinterop.generator.logging;

import java.io.OutputStream;
import java.util.function.Supplier;

public interface Logger {

    int LEVEL_ERROR = 0;

    int LEVEL_INFO = 1;

    void reportError(String error);

    void formatError(String format, Object... args);

    OutputStream getPrintStream();

    void setLogLevel(int logLevel);

    void info(int level, Supplier<String> infoSupplier);
}
