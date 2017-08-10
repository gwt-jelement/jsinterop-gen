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

package com.tenxdev.jsinterop.generator.generator.jsondocs;

import com.google.gson.Gson;
import com.tenxdev.jsinterop.generator.generator.jsondocs.inpit.ApiInfo;
import com.tenxdev.jsinterop.generator.logging.Logger;
import com.tenxdev.jsinterop.generator.processing.FileListBuilder;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class DocumentationBuilder {
    private final Logger logger;

    public DocumentationBuilder(Logger logger) {
        this.logger = logger;
    }

    public Documentation loadFrom(String jsonDocsDirectory) {
        logger.info(() -> "Preparing Javadocs");
        Documentation documentation = new Documentation();
        File directory = new File(jsonDocsDirectory);
        if (!directory.exists()) {
            logger.formatError("JSON docs directory %s does not exist.", jsonDocsDirectory);
            return documentation;
        }
        FileListBuilder.INSTANCE.findFiles(directory, "json")
                .stream()
                .filter(file -> !"index.json".equals(file.getName()))
                .forEach(file -> loadDefinition(file, documentation));
        return documentation;
    }

    private void loadDefinition(File file, Documentation documentation) {
        try (FileReader fileReader = new FileReader(file)) {
            ApiInfo apiInfo = new Gson().fromJson(fileReader, ApiInfo.class);
            register(apiInfo, documentation);
        } catch (IOException e) {
            logger.formatError("Could not read documentation file %s", file.getAbsoluteFile());
        }
    }

    private void register(ApiInfo apiInfo, Documentation documentation) {
        ClassInfo classInfo = new ClassInfo(apiInfo.getName(), apiInfo.getDescription());
        apiInfo.getMembers().forEach(memberInfo -> {
            String name = extractName(memberInfo.getName());
            classInfo.addMember(name, memberInfo);
        });
        documentation.addClassInfo(classInfo);
    }

    //FIXME move to mdn-scraper
    private String extractName(String name) {
        String result = name;
        int lastDot = result.lastIndexOf('.');
        if (lastDot != -1) {
            result = result.substring(lastDot + 1);
        }
        return result.replace("()", "");
    }
}
