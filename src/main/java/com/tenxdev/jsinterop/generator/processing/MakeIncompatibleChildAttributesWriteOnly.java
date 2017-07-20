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
import java.util.Optional;

import static org.eclipse.xtext.xbase.lib.StringExtensions.toFirstUpper;

public class MakeIncompatibleChildAttributesWriteOnly extends AbstractParentModelProcessor {

    public void process(Model model, Logger logger) {
        logger.info(Logger.LEVEL_INFO, () -> "Making incompatible children attributes write only");
        for (DefinitionInfo definitionInfo : model.getDefinitions()) {
            DefinitionInfo parentDefinitionInfo = getParentDefinition(model, definitionInfo);
            while (parentDefinitionInfo != null) {
                processInterfaces((InterfaceDefinition) definitionInfo.getDefinition(),
                        (InterfaceDefinition) parentDefinitionInfo.getDefinition());
                definitionInfo.getImplementsDefinitions().removeAll(parentDefinitionInfo.getImplementsDefinitions());
                parentDefinitionInfo = getParentDefinition(model, parentDefinitionInfo);
            }
        }
    }

    private void processInterfaces(InterfaceDefinition interfaceDefinition, InterfaceDefinition parentInterfaceDefinition) {
        List<Attribute> newAttributes = new ArrayList<>();
        for (Attribute attribute : interfaceDefinition.getAttributes()) {
            Optional<Attribute> potentialMatch = parentInterfaceDefinition.getAttributes().stream()
                    .filter(parentAtribute -> attribute.getName().equals(parentAtribute.getName()))
                    .findAny();
            if (potentialMatch.isPresent()) {
                Attribute matchingAttribute = potentialMatch.get();
                if (matchingAttribute.getType().equals(attribute.getType())) {
                    // not needed as already defined in parent with same type
                } else {
                    Attribute newAttribute = new Attribute(attribute);
                    newAttribute.setJavaName(attribute.getType().getTypeName() +
                            toFirstUpper(attribute.getName()));
                    newAttribute.setReadOnly(true);
                    attribute.setWriteOnly(true);
                    newAttributes.add(attribute);
                    newAttributes.add(newAttribute);
                }
            } else {
                newAttributes.add(attribute);
            }
        }
        interfaceDefinition.getAttributes().clear();
        interfaceDefinition.getAttributes().addAll(newAttributes);
    }
}
