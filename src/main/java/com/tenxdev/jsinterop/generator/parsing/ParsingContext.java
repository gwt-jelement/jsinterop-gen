package com.tenxdev.jsinterop.generator.parsing;

import com.tenxdev.jsinterop.generator.logging.Logger;
import com.tenxdev.jsinterop.generator.processing.TypeFactory;

public class ParsingContext {

    private final TypeFactory typeFactory;
    private final Logger logger;
    private String packageSuffix;

    public ParsingContext(Logger logger) {
        this.logger = logger;
        this.typeFactory = new TypeFactory(logger);
    }

    public TypeFactory getTypeFactory() {
        return typeFactory;
    }

    public Logger getlogger() {
        return logger;
    }

    public String getPackageSuffix() {
        return packageSuffix;
    }

    public void setPackageSuffix(String packageSuffix) {
        this.packageSuffix = packageSuffix;
    }

}
