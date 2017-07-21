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
import com.tenxdev.jsinterop.generator.model.interfaces.Definition;
import com.tenxdev.jsinterop.generator.model.interfaces.PartialDefinition;

public class PartialsMerger extends AbstractDefinitionMerger {
    private final Model model;

    public PartialsMerger(Model model, Logger logger) {
        super(logger);
        this.model = model;
    }

    public void processModel() {
        processPartials();
    }

    private void processPartials() {
        logger.info(Logger.LEVEL_INFO, () -> "Merging partial definitions");
        model.getDefinitions().stream().filter(info -> !info.getPartialDefinitions().isEmpty()).forEach(definitionInfo -> {
            Definition primaryDefinition = definitionInfo.getDefinition();
            for (PartialDefinition partialDefinition : definitionInfo.getPartialDefinitions()) {
                if (partialDefinition instanceof PartialInterfaceDefinition && primaryDefinition instanceof InterfaceDefinition) {
                    mergeInterfaces((InterfaceDefinition) primaryDefinition, (InterfaceDefinition) partialDefinition);
                } else if (partialDefinition instanceof PartialDictionaryDefinition && primaryDefinition instanceof DictionaryDefinition) {
                    mergeDictionaries((DictionaryDefinition) primaryDefinition, (DictionaryDefinition) partialDefinition);
                } else {
                    reportTypeMismatch(primaryDefinition, partialDefinition);
                }
            }
        });
    }

}
