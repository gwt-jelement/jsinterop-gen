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
import com.tenxdev.jsinterop.generator.model.AbstractDefinition;
import com.tenxdev.jsinterop.generator.model.Constructor;
import com.tenxdev.jsinterop.generator.model.InterfaceDefinition;
import com.tenxdev.jsinterop.generator.model.Model;
import com.tenxdev.jsinterop.generator.model.types.ExtensionObjectType;
import com.tenxdev.jsinterop.generator.model.types.ParameterisedType;
import com.tenxdev.jsinterop.generator.model.types.Type;

import java.util.Collections;

/**
 * find an appropriate super() call for constructors of inherited classes
 */
public class SuperCallConstructorProcessor {

    private final Model model;
    private final Logger logger;

    public SuperCallConstructorProcessor(Model model, Logger logger) {
        this.model = model;
        this.logger = logger;
    }

    public void process() {
        logger.info(() -> "Finding super call arguments for constructors of inherited classes");
        model.getInterfaceDefinitions().stream()
                .filter(interfaceDefinition -> interfaceDefinition.getParent() != null)
                .forEach(this::processInterfaceDefinition);
    }

    private void processInterfaceDefinition(InterfaceDefinition interfaceDefinition) {
        InterfaceDefinition parentInterface = findParentInterface(interfaceDefinition);
        if (parentInterface != null) {
            processInterfaceDefinition(parentInterface);
            if (!parentInterface.getConstructors().isEmpty()) {
                if (interfaceDefinition.getConstructors().isEmpty()) {
                    addDefaultConstructor(interfaceDefinition);
                }
                Constructor parentConstructor = parentInterface.getConstructors().get(0);
                interfaceDefinition.getConstructors().stream()
                        .filter(constructor -> constructor.getSuperArguments().isEmpty())
                        .forEach(constructor ->
                                constructor.getSuperArguments().addAll(parentConstructor.getArguments()));
            }
        }
    }

    private InterfaceDefinition findParentInterface(InterfaceDefinition interfaceDefinition) {
        if (interfaceDefinition.getParent() == null) {
            return null;
        }
        Type parentType = interfaceDefinition.getParent() instanceof ParameterisedType ?
                ((ParameterisedType) interfaceDefinition.getParent()).getBaseType() : interfaceDefinition.getParent();
        if (parentType instanceof ExtensionObjectType) {
            return null;
        }
        AbstractDefinition parentDefinition = model.getDefinition(parentType.getTypeName());
        if (parentDefinition == null || !(parentDefinition instanceof InterfaceDefinition)) {
            logger.formatError("SuperCallConstructorProcessor: could not find parent for %s",
                    interfaceDefinition.getName());
            return null;
        }
        InterfaceDefinition parentInterface = (InterfaceDefinition) parentDefinition;
        if (parentInterface.getParent() != null && parentInterface.getConstructors().isEmpty()) {
            processInterfaceDefinition(parentInterface);
        }
        return parentInterface;
    }

    private void addDefaultConstructor(InterfaceDefinition interfaceDefinition) {
        Constructor newConstructor = getDefaultConstructor();
        interfaceDefinition.getConstructors().add(newConstructor);
    }

    private Constructor getDefaultConstructor() {
        return new Constructor(null, null, Collections.emptyList());
    }

}
