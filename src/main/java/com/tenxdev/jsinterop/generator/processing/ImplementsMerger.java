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
import com.tenxdev.jsinterop.generator.model.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;


/**
 * We need to order the application of implements, so if A extends B,
 * and B extends C, C is merged into B before B is merged into A
 */
public class ImplementsMerger extends AbstractDefinitionMerger {

    private final Model model;
    private final Map<String, List<String>> implementsMap = new HashMap<>();

    public ImplementsMerger(Model model, Logger logger) {
        super(logger);
        this.model = model;
    }

    public void processModel() {
        logger.info(Logger.LEVEL_INFO, () -> "Merging 'implements' definitions");
        model.getDefinitions().forEach(definitionInfo ->
                definitionInfo.getImplementsDefinitions().forEach((ImplementsDefinition implementsDefinition) -> {
                    String definitionName = implementsDefinition.getName();
                    String implementsName = implementsDefinition.getParent();
                    implementsMap.computeIfAbsent(definitionName, key -> new ArrayList<>()).add(implementsName);
                }));
        implementsMap.keySet().forEach(this::processImplements);
    }

    private void processImplements(String definitionName) {
        List<String> implementsNames = implementsMap.get(definitionName);
        while (!implementsNames.isEmpty()) {
            String implementsName = implementsNames.remove(0);
            if (implementsInterfaces(implementsName)) {
                processImplements(implementsName);
            }
            processModel(definitionName, implementsName);
        }
    }

    private void getDefinitionByName(String name, Consumer<Definition> consumer) {
        DefinitionInfo definitionInfo = model.getDefinitionInfo(name);
        if (definitionInfo == null) {
            logger.formatError("Unknown definition %s implements %n", name);
        } else {
            consumer.accept(definitionInfo.getDefinition());
        }
    }

    private void processModel(String definitionName, String implementsName) {
        getDefinitionByName(definitionName, primaryDefinition ->
                getDefinitionByName(implementsName, implementsDefinition -> {
                    if (primaryDefinition instanceof InterfaceDefinition && implementsDefinition instanceof InterfaceDefinition) {
                        mergeInterfaces((InterfaceDefinition) primaryDefinition, (InterfaceDefinition) implementsDefinition);
                    } else if (primaryDefinition instanceof DictionaryDefinition && implementsDefinition instanceof DictionaryDefinition) {
                        mergeDictionaries((DictionaryDefinition) primaryDefinition, (DictionaryDefinition) implementsDefinition);
                    } else {
                        reportTypeMismatch(primaryDefinition, implementsDefinition);
                    }
                }));
    }

    private boolean implementsInterfaces(String implementsName) {
        return implementsMap.containsKey(implementsName);
    }

}
