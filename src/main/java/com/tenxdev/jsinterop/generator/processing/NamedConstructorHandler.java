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
import com.tenxdev.jsinterop.generator.model.Constructor;
import com.tenxdev.jsinterop.generator.model.ExtendedAttributes;
import com.tenxdev.jsinterop.generator.model.InterfaceDefinition;
import com.tenxdev.jsinterop.generator.model.Model;
import com.tenxdev.jsinterop.generator.model.types.ObjectType;
import com.tenxdev.jsinterop.generator.model.types.Type;

import java.util.*;
import java.util.stream.Collectors;

public class NamedConstructorHandler {

    private final Model model;
    private final Logger logger;

    public NamedConstructorHandler(Model model, Logger logger) {
        this.model = model;
        this.logger = logger;
    }

    public void process() {
        logger.info(() -> "Processing named constructors");
        List<InterfaceDefinition> newDefinitions = model.getInterfaceDefinitions().stream()
                .filter(definition -> !definition.getConstructors().isEmpty())
                .map(this::processInterface)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
        newDefinitions.forEach(definition -> {
            try {
                model.registerDefinition(definition, definition.getPackageName(), definition.getFilename());
            } catch (Model.ConflictingNameException conflictingNameException) {
                logger.formatError("Name collision detected:%n\t%s is defined in package %s in file %s" +
                                "\t%s is also defined in package %s in file %s",
                        conflictingNameException.getDefinition().getName(),
                        conflictingNameException.getDefinition().getPackageName(),
                        conflictingNameException.getDefinition().getFilename(),
                        definition.getName(), definition.getPackageName(), definition.getFilename());
                logger.reportError("Definition 1:");
                logger.reportError(conflictingNameException.getDefinition().toString());
                logger.reportError("Definition 2:");
                logger.reportError(definition.toString());
                logger.reportError("");
            }
        });
    }

    private Collection<InterfaceDefinition> processInterface(InterfaceDefinition definition) {
        List<Constructor> namedConstructors = definition.getConstructors().stream()
                .filter(constructor -> constructor.getName() != null
                        && !constructor.getName().equals(definition.getName()))
                .collect(Collectors.toList());
        if (!namedConstructors.isEmpty()) {
            Map<String, InterfaceDefinition> newInterfaceDefinitions = new HashMap<>();
            for (Constructor namedConstructor : namedConstructors) {
                InterfaceDefinition interfaceDefinition =
                        newInterfaceDefinitions.computeIfAbsent(namedConstructor.getName(),
                                constructorName -> createdNewInterface(definition, constructorName));
                interfaceDefinition.getConstructors().add(namedConstructor);
            }
            definition.getConstructors().removeAll(namedConstructors);
            return newInterfaceDefinitions.values();
        }
        return Collections.emptyList();
    }

    private InterfaceDefinition createdNewInterface(InterfaceDefinition parentDefinition, String name) {
        if (model.getTypeFactory().hasType(name)){
            logger.formatError("Found already existing type %s for named constructor in %s", name, parentDefinition.getName());
        }
        Type parentType = model.getTypeFactory().getTypeNoParse(parentDefinition.getName());
        if (!(parentType instanceof ObjectType)) {
            logger.reportError("Unexpected type found for interface " + parentDefinition.getName() + ": " + parentType);
            parentType = new ObjectType(parentDefinition.getName(), parentDefinition.getPackageName());
        }
        Type newType = new ObjectType(name, parentDefinition.getPackageName());
        model.getTypeFactory().registerType(name, newType);
        InterfaceDefinition newInterfaceDefinition = new InterfaceDefinition(name, parentType, new ArrayList<>(), Collections.emptyList(),
                new ExtendedAttributes(null));
        newInterfaceDefinition.setFilename(parentDefinition.getFilename());
        newInterfaceDefinition.setPackageName(parentDefinition.getPackageName());
        return newInterfaceDefinition;
    }
}
