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

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

enum FileListBuilder {

    INSTANCE;

    public List<File> findFiles(File directory, String... extensions) {
        List<File> fileList = new ArrayList<>();
        findFiles(directory, fileList, Arrays.asList(extensions));
        return fileList;
    }

    private void findFiles(File baseDirectory, List<File> fileList, List<String> extensions) {
        File[] files = baseDirectory.listFiles((dir, name) -> {
            File file = new File(dir, name);
            return file.isDirectory() || (file.isFile() && extensions.contains(getFileExtension(name)));
        });
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    findFiles(file, fileList, extensions);
                } else {
                    fileList.add(file);
                }
            }
        }
    }

    private String getFileExtension(String filename) {
        int lastDot = filename.lastIndexOf(".");
        return lastDot == -1 ? "" : filename.substring(lastDot + 1);
    }
}
