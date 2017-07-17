package com.tenxdev.jsinterop.generator.parsing;

import com.tenxdev.jsinterop.generator.errors.ErrorReporter;
import com.tenxdev.jsinterop.generator.model.types.Type;

public class ParsingContext {

    private TypeFactory typeFactory;
    private ErrorReporter errorReporter;
    private String packageSuffix;

    public ParsingContext(ErrorReporter errorReporter) {
        this.errorReporter = errorReporter;
        this.typeFactory=new TypeFactory(errorReporter);
    }

    public TypeFactory getTypeFactory() {
        return typeFactory;
    }

    public ErrorReporter getErrorReporter() {
        return errorReporter;
    }

    public String getPackageSuffix() {
        return packageSuffix;
    }

    public void setPackageSuffix(String packageSuffix) {
        this.packageSuffix = packageSuffix;
    }

}
