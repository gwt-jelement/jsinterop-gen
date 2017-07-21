/*
 * Copyright 2017 Abed Tony BenBrahim <tony.benrahim@10xdev.com>
 *     and Gwt-JElement project contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.tenxdev.jsinterop.generator.generator;

import com.tenxdev.jsinterop.generator.logging.Logger;
import com.tenxdev.jsinterop.generator.model.*;
import com.tenxdev.jsinterop.generator.model.interfaces.Definition;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class SourceGenerator extends XtendTemplate {

    private static final String VERSION = "0.0.1-SNAPSHOT";
    private Logger logger;

    public SourceGenerator(Logger logger) {
        this.logger = logger;
    }

    public void processModel(Model model, String outputDirectory, String basePackageName) throws IOException {
        logger.info(Logger.LEVEL_INFO, () -> "Generating Java source code");
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
