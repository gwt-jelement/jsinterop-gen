package com.tenxdev.jsinterop.generator.processing;

import com.tenxdev.jsinterop.generator.errors.ErrorReporter;
import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;

import java.io.File;

class FileAwareANTLRErrorListener extends BaseErrorListener {
    private final File file;
    private final ErrorReporter errorHandler;

    public FileAwareANTLRErrorListener(File file, ErrorReporter errorHandler) {
        this.file = file;
        this.errorHandler=errorHandler;
    }

    @Override
    public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e) {
        errorHandler.formatError("In file %s at line %d, position %d:%n\t%s%n", file.getAbsolutePath(), line, charPositionInLine, msg);
    }
}
