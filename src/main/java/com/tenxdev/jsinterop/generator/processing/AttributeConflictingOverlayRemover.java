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
import com.tenxdev.jsinterop.generator.model.Attribute;
import com.tenxdev.jsinterop.generator.model.DefinitionInfo;
import com.tenxdev.jsinterop.generator.model.InterfaceDefinition;
import com.tenxdev.jsinterop.generator.model.Model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AttributeConflictingOverlayRemover {

    private Model model;
    private Logger logger;

    public AttributeConflictingOverlayRemover(Model model, Logger logger) {
        this.model = model;
        this.logger = logger;
    }

    public void process() {
        model.getDefinitions().stream()
                .filter(definitionInfo -> definitionInfo.getDefinition() instanceof InterfaceDefinition)
                .map(definitionInfo -> (InterfaceDefinition) definitionInfo.getDefinition())
                .filter(definition -> definition.getParent() != null)
                .forEach(this::process);
    }

    private void process(InterfaceDefinition definition) {
        String parentTypeName = definition.getParent().getTypeName();
        DefinitionInfo parentDefinition = model.getDefinitionInfo(parentTypeName);
        if (parentDefinition == null || !(parentDefinition.getDefinition() instanceof InterfaceDefinition)) {
            logger.formatError("AttributeConflictingOverlayRemover: inconsistent parent %s for %s%n",
                    parentTypeName, definition.getName());
        } else {
            process(definition, (InterfaceDefinition) parentDefinition.getDefinition());
        }
    }

    private void process(InterfaceDefinition definition, InterfaceDefinition parentDefinition) {
        List<String> parentWritableAttributes = parentDefinition.getAttributes().stream()
                .filter(attribute -> !attribute.isReadOnly())
                .map(Attribute::getName)
                .collect(Collectors.toList());
        List<String> parentReadOnlyAttributes = parentDefinition.getAttributes().stream()
                .filter(Attribute::isReadOnly)
                .map(Attribute::getName)
                .collect(Collectors.toList());
        // could also do write only but not needed so far
        List<Attribute> attributesToBeRemoved = new ArrayList<>();
        definition.getAttributes().stream()
                .forEach(attribute -> {
                    if (parentWritableAttributes.contains(attribute.getName())) {
                        attributesToBeRemoved.add(attribute);
                    } else if (parentReadOnlyAttributes.contains(attribute.getName())) {
                        attribute.setWriteOnly(true);
                    }
                });
        definition.getAttributes().removeAll(attributesToBeRemoved);
        //could recurse into parent but not needed so far
    }

}
