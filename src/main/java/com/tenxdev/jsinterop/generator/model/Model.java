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

import com.tenxdev.jsinterop.generator.model.interfaces.Definition;
import com.tenxdev.jsinterop.generator.model.interfaces.PartialDefinition;
import com.tenxdev.jsinterop.generator.processing.TypeFactory;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Model {

    private final Map<String, DefinitionInfo<?>> definitions = new HashMap<>();
    private TypeFactory typeFactory;

    public Collection<DefinitionInfo<?>> getDefinitions() {
        return definitions.values();
    }

    public DefinitionInfo getDefinitionInfo(String name) {
        return definitions.get(name);
    }

    public void registerDefinition(Definition definition, String packageSuffix, String filename) throws ConflictingNameException {
        DefinitionInfo<Definition> definitionInfo = (DefinitionInfo<Definition>) definitions.computeIfAbsent(definition.getName(), DefinitionInfo::new);
        if (definition instanceof PartialDefinition) {
            definitionInfo.addPartialDefinition((PartialDefinition) definition);
        } else if (definition instanceof ImplementsDefinition) {
            definitionInfo.addImplementsDefinition((ImplementsDefinition) definition);
        } else {
            if (definitionInfo.getDefinition() != null && !definitionInfo.getDefinition().equals(definition)) {
                throw new ConflictingNameException(definitionInfo);
            }
            definitionInfo.setDefinition(definition);
            definitionInfo.setPackageName(packageSuffix);
            definitionInfo.setFilename(filename);
        }
    }

    public TypeFactory getTypeFactory() {
        return typeFactory;
    }

    public void setTypeFactory(TypeFactory typeFactory) {
        this.typeFactory = typeFactory;
    }

    public class ConflictingNameException extends Exception {
        private final transient DefinitionInfo definitionInfo;

        private ConflictingNameException(DefinitionInfo definitionInfo) {
            this.definitionInfo = definitionInfo;
        }

        public DefinitionInfo getDefinitionInfo() {
            return definitionInfo;
        }
    }


}