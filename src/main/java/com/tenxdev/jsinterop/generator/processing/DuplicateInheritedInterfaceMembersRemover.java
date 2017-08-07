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
import com.tenxdev.jsinterop.generator.model.types.ExtensionObjectType;
import com.tenxdev.jsinterop.generator.model.types.ParameterisedType;
import com.tenxdev.jsinterop.generator.model.types.Type;

import java.util.ArrayList;
import java.util.List;

public class DuplicateInheritedInterfaceMembersRemover {

    private final Model model;
    private final Logger logger;

    public DuplicateInheritedInterfaceMembersRemover(Model model, Logger logger) {
        this.model = model;
        this.logger = logger;
    }

    public void process() {
        logger.info(() -> "Removing duplicate inherited attributes from interfaces");
        model.getInterfaceDefinitions().stream()
                .filter(definition -> definition.getParent() != null)
                .forEach(this::processInterface);
    }

    private void processInterface(InterfaceDefinition definition) {
        processAttributes(definition);
        processMethods(definition);

    }

    private void processAttributes(InterfaceDefinition definition) {
        List<Attribute> attributesToRemove = new ArrayList<>();
        for (Attribute attribute : definition.getAttributes()) {
            if (parentHasAttribute(definition, attribute)) {
                attributesToRemove.add(attribute);
            }
        }
        definition.getAttributes().removeAll(attributesToRemove);
    }

    private void processMethods(InterfaceDefinition definition) {
        List<Method> methodsToRemove = new ArrayList<>();
        for (Method method : definition.getMethods()) {
            if (parentHasMethod(definition, method)) {
                methodsToRemove.add(method);
            }
        }
        definition.getMethods().removeAll(methodsToRemove);
    }

    private boolean parentHasAttribute(InterfaceDefinition definition, Attribute attribute) {
        InterfaceDefinition parentDefinition = getParentDefinition(definition);
        return parentDefinition != null &&
                (parentDefinition.getAttributes().contains(attribute)
                        || parentHasAttribute(parentDefinition, attribute));
    }

    private boolean parentHasMethod(InterfaceDefinition definition, Method method) {
        InterfaceDefinition parentDefinition = getParentDefinition(definition);
        return parentDefinition != null &&
                (parentDefinition.getMethods().contains(method)
                        || parentHasMethod(parentDefinition, method));
    }

    private InterfaceDefinition getParentDefinition(InterfaceDefinition definition) {
        if (definition.getParent() != null && !(definition.getParent() instanceof ExtensionObjectType)) {
            Type parentType= definition.getParent() instanceof ParameterisedType?
                    ((ParameterisedType)definition.getParent()).getBaseType(): definition.getParent();
            AbstractDefinition parentDefinition = model.getDefinition(parentType.getTypeName());
            if (parentDefinition instanceof InterfaceDefinition) {
                return (InterfaceDefinition) parentDefinition;
            }
            logger.formatError("InheritedInterfaceAttributeRemover: Inconsistent parent type for %s "+definition.getParent(),
                    definition.getName());
        }
        return null;
    }

}
