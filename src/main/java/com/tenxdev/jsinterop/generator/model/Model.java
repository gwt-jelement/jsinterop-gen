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

import java.util.*;
import java.util.stream.Collectors;

public class Model {

    private final Map<String, AbstractDefinition> definitions = new HashMap<>();
    private final Map<String, List<PartialDefinition>> deferredPartials = new HashMap<>();
    private final Map<String, List<ImplementsDefinition>> deferredImplements = new HashMap<>();

    public Collection<AbstractDefinition> getDefinitions() {
        return definitions.values();
    }

    public AbstractDefinition getDefinition(String name) {
        return definitions.get(name);
    }

    public List<InterfaceDefinition> getInterfaceDefinitions() {
        return definitions.values().stream()
                .filter(definition -> definition instanceof InterfaceDefinition)
                .map(definition -> (InterfaceDefinition) definition)
                .collect(Collectors.toList());
    }

    public List<DictionaryDefinition> getDictionaryDefinitions() {
        return definitions.values().stream()
                .filter(definition -> definition instanceof DictionaryDefinition)
                .map(definition -> (DictionaryDefinition) definition)
                .collect(Collectors.toList());
    }

    public void registerDefinition(AbstractDefinition definition, String packageSuffix, String filename) throws ConflictingNameException {
        definition.setPackageName(packageSuffix);
        definition.setFilename(filename);
        AbstractDefinition exisitingDefinition = definitions.get(definition.getName());
        if (definition instanceof PartialDefinition) {
            if (exisitingDefinition == null) {
                deferredPartials.computeIfAbsent(definition.getName(), key -> new ArrayList<>())
                        .add((PartialDefinition) definition);
            } else {
                exisitingDefinition.getPartialDefinitions().add((PartialDefinition) definition);
            }
        } else if (definition instanceof ImplementsDefinition) {
            if (exisitingDefinition == null) {
                deferredImplements.computeIfAbsent(definition.getName(), key -> new ArrayList<>())
                        .add((ImplementsDefinition) definition);
            } else {
                exisitingDefinition.getImplementsDefinitions().add((ImplementsDefinition) definition);
            }
        } else if (!definition.equals(exisitingDefinition)) {
            if (exisitingDefinition != null) {
                throw new ConflictingNameException(exisitingDefinition);
            }
            definitions.put(definition.getName(), definition);
            List<PartialDefinition> partials = deferredPartials.remove(definition.getName());
            if (partials != null) {
                definition.getPartialDefinitions().addAll(partials);
            }
            List<ImplementsDefinition> implementsDefinitions = deferredImplements.remove(definition.getName());
            if (implementsDefinitions != null) {
                definition.getImplementsDefinitions().addAll(implementsDefinitions);
            }
        }

    }

    @Override
    public String toString() {
        return "Model{" +
                "definitions=" + definitions +
                '}';
    }

    public class ConflictingNameException extends Exception {

        private final transient AbstractDefinition definition;

        private ConflictingNameException(AbstractDefinition definition) {
            this.definition = definition;
        }

        public AbstractDefinition getDefinition() {
            return definition;
        }
    }


}