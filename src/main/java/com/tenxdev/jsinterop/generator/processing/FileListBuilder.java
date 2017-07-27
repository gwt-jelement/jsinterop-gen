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

import java.io.File;
import java.util.ArrayList;
import java.util.List;

class FileListBuilder {

    private final Logger logger;

    public FileListBuilder(Logger logger) {
        this.logger = logger;
    }

    public List<File> findFiles(String baseDirectory) {
        File directory = new File(baseDirectory);
        if (!directory.exists()) {
            logger.formatError("Input directory %s does not exist.", baseDirectory);
            System.exit(-1);
        }
        List<File> fileList = new ArrayList<>();
        findFiles(directory, fileList);
        if (fileList.isEmpty()) {
            logger.formatError("No idl files were found in input directory %s", baseDirectory);
            System.exit(-1);
        }
        return fileList;
    }

    private void findFiles(File baseDirectory, List<File> fileList) {
        File[] files = baseDirectory.listFiles((dir, name) -> {
            File file = new File(dir, name);
            return file.isDirectory() || (file.isFile() && name.toLowerCase().endsWith(".idl"));
        });
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    findFiles(file, fileList);
                } else {
                    fileList.add(file);
                }
            }
        }
    }
}
