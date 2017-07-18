package com.tenxdev.jsinterop.generator.generator;

import com.tenxdev.jsinterop.generator.model.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class SourceGenerator extends Template {

    private static final String VERSION = "0.0.1-SNAPSHOT";

    public void processModel(Model model, String outputDirectory, String basePackageName) throws IOException {
        outputFile(Paths.get(outputDirectory, "pom.xml"), new PomGenerator().generate(VERSION));
        outputFile(Paths.get(outputDirectory, ".gitignore"), new GitIgnoreGenerator().generate());
        outputFile(Paths.get(outputDirectory, "src", "main", "java", packageNameToPath(basePackageName),
                "JElement.gwt.xml"), new GwtModuleGenerator().generate());
        EnumGenerator enumGenerator = new EnumGenerator();
        CallbackGenerator callbackGenerator = new CallbackGenerator();
        InterfaceGenerator interfaceGenerator = new InterfaceGenerator();
        DictionaryGenerator dictionaryGenerator = new DictionaryGenerator();
        for (DefinitionInfo definitionInfo : model.getDefinitions()) {
            Definition definition = definitionInfo.getDefinition();
            Path filePath = getSourcePath(outputDirectory, definitionInfo, basePackageName);
            if (definition instanceof EnumDefinition) {
                outputFile(filePath, enumGenerator.generate(basePackageName, definitionInfo));
            } else if (definition instanceof CallbackDefinition) {
                outputFile(filePath, callbackGenerator.generate(basePackageName, definitionInfo));
            } else if (definition instanceof InterfaceDefinition) {
                outputFile(filePath, interfaceGenerator.generate(basePackageName, definitionInfo));
            } else if (definition instanceof DictionaryDefinition) {
                outputFile(filePath, dictionaryGenerator.generate(basePackageName, definitionInfo));
            }
        }
    }

    private Path getSourcePath(String outputDirectory, DefinitionInfo definitionInfo, String basePackageName) {
        return Paths.get(outputDirectory, "src", "main", "java",
                packageNameToPath(basePackageName + definitionInfo.getPackageName()),
                definitionInfo.getName() + ".java");
    }

    private String packageNameToPath(String packageName) {
        return packageName.replace(".", File.separator);
    }

    private void outputFile(Path filePath, CharSequence contents) throws IOException {
        File parentDirectory = filePath.toFile().getParentFile();
        if (!parentDirectory.exists() && !parentDirectory.mkdirs()) {
            throw new IOException(String.format("Unable to create path %s", parentDirectory.getAbsolutePath()));
        }
        try (FileWriter fileWriter = new FileWriter(filePath.toFile())) {
            fileWriter.write(contents.toString());
        }
    }
}
