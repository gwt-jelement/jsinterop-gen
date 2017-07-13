package com.tenxdev.jsinterop.generator.processing;

import com.tenxdev.jsinterop.generator.DefinitionInfo;
import com.tenxdev.jsinterop.generator.errors.ErrorReporter;
import com.tenxdev.jsinterop.generator.model.Definition;
import com.tenxdev.jsinterop.generator.visitors.DefinitionsVisitor;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr4.webidl.WebIDLLexer;
import org.antlr4.webidl.WebIDLParser;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModelBuilder {
    private final ErrorReporter errorHandler;

    public ModelBuilder(ErrorReporter errorHandler) {
        this.errorHandler = errorHandler;
    }

    public Map<String, DefinitionInfo> buildFrom(String inputDirectory, List<File> fileList) throws IOException {
        Map<String, DefinitionInfo> definitions = processFiles(inputDirectory, fileList);
        new ModelPartialsMerger(errorHandler).mergePartials(definitions);
        return definitions;
    }

    private Map<String, DefinitionInfo> processFiles(String inputDirectory, List<File> fileList) throws IOException {
        Map<String, DefinitionInfo> definitionInfoMap = new HashMap<>();
        int offset = new File(inputDirectory).getAbsolutePath().length();
        for (File file : fileList) {
            String packageSuffix = file.getParentFile().getAbsolutePath().substring(offset).replace(File.separator, ".");
            List<Definition> definitions = scanFile(file);
            for (Definition definition : definitions) {
                registerDefinition(definitionInfoMap, definition, packageSuffix, file.getAbsolutePath());
            }
        }
        return definitionInfoMap;
    }

    private void registerDefinition(Map<String, DefinitionInfo> definitionInfoMap,
                                    Definition definition,String packageSuffix, String filename) {
        DefinitionInfo definitionInfo = definitionInfoMap.computeIfAbsent(definition.getName(), key -> new DefinitionInfo(key));
        if (definition.isPartial()) {
            definitionInfo.addPartial(definition);
        } else {
            if (definitionInfo.getDefinition() != null && ! definitionInfo.getDefinition().equals(definition)) {
                errorHandler.formatError("Name collision detected:%n\t%s is defined in package %s in file %s%n" +
                                "\t%s is also defined in package %s in file %s%n",
                        definitionInfo.getDefinition().getName(), definitionInfo.getPackgeName(), definitionInfo.getFilename(),
                        definition.getName(), packageSuffix, filename);
                errorHandler.reportError("Definition 1:");
                errorHandler.reportError(definitionInfo.getDefinition().toString());
                errorHandler.reportError("Definition 2:");
                errorHandler.reportError(definition.toString());
                errorHandler.reportError("");
            }
            definitionInfo.setDefinition(definition);
            definitionInfo.setPackgeName(packageSuffix);
            definitionInfo.setFilename(filename);
        }
    }

    private List<Definition> scanFile(File file) throws IOException{
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
