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

package com.tenxdev.jsinterop.generator.model;

import com.tenxdev.jsinterop.generator.model.interfaces.PartialDefinition;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractDefinition {

    private final String name;
    private final List<PartialDefinition> partialDefinitions = new ArrayList<>();
    private final List<ImplementsDefinition> implementsDefinitions = new ArrayList<>();
    private final List<String> importedPackages = new ArrayList<>();
    private String packageName;
    private String filename;
    private Extension extension;

    AbstractDefinition(String name) {
        this.name = name;
    }

    public Extension getExtension() {
        return extension;
    }

    public void setExtension(Extension extension) {
        this.extension = extension;
    }

    public List<String> getImportedPackages() {
        return importedPackages;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public final String getName() {
        return name;
    }

    public List<PartialDefinition> getPartialDefinitions() {
        return partialDefinitions;
    }

    public List<ImplementsDefinition> getImplementsDefinitions() {
        return implementsDefinitions;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

}
