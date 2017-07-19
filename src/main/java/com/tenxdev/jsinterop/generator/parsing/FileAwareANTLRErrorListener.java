package com.tenxdev.jsinterop.generator.parsing;

import com.tenxdev.jsinterop.generator.logging.Logger;
import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;

import java.io.File;

public class FileAwareANTLRErrorListener extends BaseErrorListener {
    private final File file;
    private final Logger logger;

    public FileAwareANTLRErrorListener(File file, Logger logger) {
        this.file = file;
        this.logger = logger;
    }

    @Override
    public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e) {
        logger.formatError("In file %s at line %d, position %d:%n\t%s%n", file.getAbsolutePath(), line, charPositionInLine, msg);
    }
}
