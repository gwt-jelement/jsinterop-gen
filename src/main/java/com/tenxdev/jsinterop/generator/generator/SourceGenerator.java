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
import com.tenxdev.jsinterop.generator.processing.TemplateFiller;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Collections;
import java.util.List;

public class SourceGenerator extends XtendTemplate {

    private static final String VERSION = "0.0.1-SNAPSHOT";
    private final Logger logger;

    public SourceGenerator(Logger logger) {
        this.logger = logger;
    }

    public void processModel(Model model, String outputDirectory, String basePackageName) throws IOException {
        logger.info(() -> "Generating Java source code");

        outputFile(Paths.get(outputDirectory, "pom.xml"), new PomGenerator().generate(VERSION));
        outputFile(Paths.get(outputDirectory, ".gitignore"), new GitIgnoreGenerator().generate());
        outputFile(Paths.get(outputDirectory, "src", "main", "java", packageNameToPath(basePackageName),
                "JElement.gwt.xml"), new GwtModuleGenerator().generate());
        EnumGenerator enumGenerator = new EnumGenerator();
        CallbackGenerator callbackGenerator = new CallbackGenerator();
        InterfaceGenerator interfaceGenerator = new InterfaceGenerator();
        DictionaryGenerator dictionaryGenerator = new DictionaryGenerator();
        TemplateFiller templateFiller=new TemplateFiller(model, logger);
        int numOutput = 0;
        for (AbstractDefinition definition : model.getDefinitions()) {
            Path filePath = getSourcePath(outputDirectory, definition.getPackageName(), definition.getName(),
                    basePackageName);
            if (definition instanceof EnumDefinition) {
                outputFile(filePath, enumGenerator.generate(basePackageName, (EnumDefinition) definition));
                ++numOutput;
            } else if (definition instanceof CallbackDefinition) {
                outputFile(filePath, callbackGenerator.generate(basePackageName, (CallbackDefinition) definition));
                ++numOutput;
            } else if (definition instanceof InterfaceDefinition) {
                if (!((InterfaceDefinition)definition).isSupressed()) {
                    outputFile(filePath, interfaceGenerator.generate(basePackageName,
                            (InterfaceDefinition) definition, templateFiller));
                    ++numOutput;
                }
            } else if (definition instanceof DictionaryDefinition) {
                outputFile(filePath, dictionaryGenerator.generate(basePackageName, (DictionaryDefinition) definition));
                ++numOutput;
            }
        }
        Path filePath = getSourcePath(outputDirectory, ".core","Js", basePackageName);
        outputFile(filePath, new JsGenerator().generate(basePackageName));
        int count = numOutput;
        logger.info(() -> String.format("Generated %d Java file%s in %s", count, count != 1 ? "s" : "", outputDirectory));
    }

    private void outputResource(String outputDirectory, String javaResourcePath) throws IOException {
        Path filePath = Paths.get(outputDirectory, "src", "main", "java",
                javaResourcePath.replace("/", File.separator));
        File parentDirectory = filePath.toFile().getParentFile();
        if (!parentDirectory.exists() && !parentDirectory.mkdirs()) {
            throw new IOException(String.format("Unable to create path %s", parentDirectory.getAbsolutePath()));
        }
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(javaResourcePath)) {
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        }
    }

    private Path getSourcePath(String outputDirectory, String definitionPackage, String definitionName, String basePackageName) {
        return Paths.get(outputDirectory, "src", "main", "java",
                packageNameToPath(basePackageName + definitionPackage),
                definitionName + ".java");
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
