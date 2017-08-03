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

package com.tenxdev.jsinterop.generator.processing;

import com.tenxdev.jsinterop.generator.logging.Logger;
import com.tenxdev.jsinterop.generator.model.AbstractDefinition;
import com.tenxdev.jsinterop.generator.model.Extension;
import com.tenxdev.jsinterop.generator.model.Model;
import com.tenxdev.jsinterop.generator.model.types.ExtensionObjectType;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum ExtensionsUtils {

    INSTANCE;

    private Pattern packagePattern = Pattern.compile("package\\s+\\$\\{basePackage\\}((?>[^;\\s])+)\\s*;");
    private Pattern partialClassPattern = Pattern.compile("partial\\s+class\\s+((?>[^\\s{])+)\\s*\\{((?>.|\\s)*)\\}");
    private Pattern importPattern = Pattern.compile("import\\s+([^\\s;]+)\\s*;");

    public boolean isPartialClass(File file) {
        return file.getName().contains(".partial.");
    }

    public boolean isTemplateFile(File file) {
        return file.getName().endsWith(".vm");
    }

    public void registerExtensionType(File file, TypeFactory typeFactory, Logger logger) throws IOException {
        String content = new String(Files.readAllBytes(file.toPath()), StandardCharsets.UTF_8);
        String className = extractClassName(file.getName());
        String packageSuffix = extractPackageSuffix(content);
        if (packageSuffix == null) {
            logger.formatError("Missing package declation for extension %s in file %s",
                    className, file.getName());
        } else {
            typeFactory.registerType(className, new ExtensionObjectType(className, packageSuffix));
        }
    }

    public void registerPartialClassExtension(File file, Model model, Logger logger) throws IOException {
        String content = new String(Files.readAllBytes(file.toPath()), StandardCharsets.UTF_8);
        if (isPartialClass(file)) {
            processPartialFile(file, content, model, logger);
        } else {
            processFullClass(file, content, model, logger);
        }
    }

    private void processFullClass(File file, String content, Model model, Logger logger) {
        String className = extractClassName(file.getName());
        String packageSuffix = extractPackageSuffix(content);
        if (packageSuffix == null) {
            logger.formatError("Missing package declation for extension %s in file %s",
                    className, file.getName());
        } else {
            model.getExtensions().add(new Extension(className, packageSuffix, content));
        }
    }

    private void processPartialFile(File file, String content, Model model, Logger logger) {
        Matcher matcher = partialClassPattern.matcher(content);
        if (matcher.find() && matcher.groupCount() == 2) {
            String className = matcher.group(1);
            String template = matcher.group(2);
            List<String> imports = extractImports(content);
            checkFilename(file, className, logger);
            registerPartialClassExtension(model, new Extension(className, template, imports), logger);
        } else {
            logger.formatError("Unable to find a partial class definition in %s", file.getName());
        }
    }

    private String extractPackageSuffix(String content) {
        Matcher matcher = packagePattern.matcher(content);
        if (matcher.find() && matcher.groupCount() == 1) {
            return matcher.group(1);
        }
        return null;
    }

    private void registerPartialClassExtension(Model model, Extension extension, Logger logger) {
        AbstractDefinition definition = model.getDefinition(extension.getClassName());
        if (definition != null) {
            definition.setExtension(extension);
        } else {
            logger.formatError("Unable to find definition for %s while processing extensions",
                    extension.getClassName());
        }
    }

    private void checkFilename(File file, String className, Logger logger) {
        if (!file.getName().startsWith(className + ".")) {
            logger.formatError("WARNING: filename %s does not correspond to partial class %s",
                    file.getName(), className);
        }
    }

    private List<String> extractImports(String content) {
        List<String> imports = new ArrayList<>();
        Matcher matcher = importPattern.matcher(content);
        if (matcher.find()) {
            for (int groupIndex = 1; groupIndex < matcher.groupCount(); ++groupIndex) {
                imports.add(matcher.group(groupIndex).trim());
            }
        }
        return imports.isEmpty() ? Collections.emptyList() : imports;
    }

    private String extractClassName(String name) {
        int firstDot = name.indexOf(".");
        return firstDot == -1 ? name : name.substring(0, firstDot);
    }

}
