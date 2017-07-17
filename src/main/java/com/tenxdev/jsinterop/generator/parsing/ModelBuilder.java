package com.tenxdev.jsinterop.generator.parsing;

import com.tenxdev.jsinterop.generator.errors.ErrorReporter;
import com.tenxdev.jsinterop.generator.model.Definition;
import com.tenxdev.jsinterop.generator.model.Model;
import com.tenxdev.jsinterop.generator.parsing.visitors.firstpass.DefinitionsScanner;
import com.tenxdev.jsinterop.generator.parsing.visitors.secondpass.DefinitionsVisitor;
import com.tenxdev.jsinterop.generator.processing.FileListBuilder;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr4.webidl.TypesLexer;
import org.antlr4.webidl.TypesParser;
import org.antlr4.webidl.WebIDLLexer;
import org.antlr4.webidl.WebIDLParser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class ModelBuilder {
    private final ErrorReporter errorHandler;

    public ModelBuilder(ErrorReporter errorHandler) {
        this.errorHandler = errorHandler;
    }

    public Model buildFrom(String inputDirectory) throws IOException {
        ParsingContext parsingContext = performFirstPass(inputDirectory);
        return performSecondPass(inputDirectory, parsingContext);
    }

    /**
     * first pass is to locate all object types and type definitions, to
     * register them in the type factory
     *
     * @param inputDirectory the input directory
     * @return a parsing context
     */
    private ParsingContext performFirstPass(String inputDirectory) throws IOException {
        System.err.println("************ PASS 1 **********");
        List<File> fileList = new FileListBuilder(errorHandler).findFiles(inputDirectory);
        int offset = new File(inputDirectory).getAbsolutePath().length();
        ParsingContext parsingContext = new ParsingContext(errorHandler);
        for (File file : fileList) {
            parsingContext.setPackageSuffix(getPackageSuffix(offset, file));
            scanFile(file, parsingContext);
        }
        parsingContext.getTypeFactory().regisiterTypeDefs();
        return parsingContext;
    }

    private Model performSecondPass(String inputDirectory, ParsingContext parsingContext) throws IOException {
        System.err.println("************ PASS 2 **********");
        Model model = new Model();
        List<File> fileList = new FileListBuilder(errorHandler).findFiles(inputDirectory);
        int offset = new File(inputDirectory).getAbsolutePath().length();
        for (File file : fileList) {
            String packageSuffix = getPackageSuffix(offset, file);
            parsingContext.setPackageSuffix(packageSuffix);
            List<Definition> definitions = parseFile(file, parsingContext);
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

    private void scanFile(File file, ParsingContext parsingContext) throws IOException {
        try (FileReader reader = new FileReader(file)) {
            WebIDLLexer lexer = new WebIDLLexer(new ANTLRInputStream(reader));
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            WebIDLParser parser = new WebIDLParser(tokens);
            parser.removeErrorListeners();
            parser.addErrorListener(new FileAwareANTLRErrorListener(file, errorHandler));
            WebIDLParser.DefinitionsContext definitions = parser.definitions();
            definitions.accept(new DefinitionsScanner(parsingContext));
        }

    }

    private List<Definition> parseFile(File file, ParsingContext parsingContext) throws IOException {
        try (FileReader reader = new FileReader(file)) {
            WebIDLLexer lexer = new WebIDLLexer(new ANTLRInputStream(reader));
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            WebIDLParser parser = new WebIDLParser(tokens);
            parser.removeErrorListeners();
            parser.addErrorListener(new FileAwareANTLRErrorListener(file, errorHandler));
            WebIDLParser.DefinitionsContext definitions = parser.definitions();
            return definitions.accept(new DefinitionsVisitor(parsingContext));
        }
    }

    private String getPackageSuffix(int offset, File file) {
        return file.getParentFile().getAbsolutePath().substring(offset).replace(File.separator, ".");
    }

}
