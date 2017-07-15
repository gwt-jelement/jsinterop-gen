package com.tenxdev.jsinterop.generator.processing;

import com.tenxdev.jsinterop.generator.errors.ErrorReporter;
import com.tenxdev.jsinterop.generator.model.Definition;
import com.tenxdev.jsinterop.generator.model.Model;
import com.tenxdev.jsinterop.generator.visitors.DefinitionsVisitor;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr4.webidl.WebIDLLexer;
import org.antlr4.webidl.WebIDLParser;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class ModelBuilder {
    private final ErrorReporter errorHandler;

    public ModelBuilder(ErrorReporter errorHandler) {
        this.errorHandler = errorHandler;
    }

    public Model buildFrom(String inputDirectory) throws IOException {
        Model model = new Model();
        List<File> fileList=new FileListBuilder(errorHandler).findFiles(inputDirectory);
        int offset = new File(inputDirectory).getAbsolutePath().length();
        for (File file : fileList) {
            String packageSuffix = file.getParentFile().getAbsolutePath().substring(offset).replace(File.separator, ".");
            List<Definition> definitions = scanFile(file);
            for (Definition definition : definitions) {
                try {
                    model.registerDefinition(definition, packageSuffix, file.getAbsolutePath());
                } catch (Model.ConflictingNameExcepton conflictingNameExcepton) {
                    errorHandler.formatError("Name collision detected:%n\t%s is defined in package %s in file %s%n" +
                                    "\t%s is also defined in package %s in file %s%n",
                            conflictingNameExcepton.getDefinitionInfo().getDefinition().getName(),
                            conflictingNameExcepton.getDefinitionInfo().getPackgeName(),
                            conflictingNameExcepton.getDefinitionInfo().getFilename(),
                            definition.getName(), packageSuffix, file.getAbsolutePath());
                    errorHandler.reportError("Definition 1:");
                    errorHandler.reportError(conflictingNameExcepton.getDefinitionInfo().getDefinition().toString());
                    errorHandler.reportError("Definition 2:");
                    errorHandler.reportError(definition.toString());
                    errorHandler.reportError("");
                }
            }
        }
        return model;
    }

    private List<Definition> scanFile(File file) throws IOException {
        try (FileReader reader = new FileReader(file)) {
            WebIDLLexer lexer = new WebIDLLexer(new ANTLRInputStream(reader));
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            WebIDLParser parser = new WebIDLParser(tokens);
            parser.removeErrorListeners();
            parser.addErrorListener(new FileAwareANTLRErrorListener(file, errorHandler));
            WebIDLParser.DefinitionsContext definitions = parser.definitions();
            return definitions.accept(new DefinitionsVisitor(file.getAbsolutePath()));
        }
    }

}
