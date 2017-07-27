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

import java.util.Collections;

/**
 * find an appropriate super() call for constructors of inherited classes
 */
public class SuperCallConstructorProcessor {

    private Model model;
    private Logger logger;

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
        if (interfaceDefinition.getConstructors().isEmpty()) {
            addDefaultConstructor(interfaceDefinition);
        }
        InterfaceDefinition parentInterface = findParentInterface(interfaceDefinition);
        interfaceDefinition.getConstructors().forEach(
                constructor -> findSuperCallArguments(constructor, parentInterface));
    }

    private InterfaceDefinition findParentInterface(InterfaceDefinition interfaceDefinition) {
        AbstractDefinition parentDefinition = model.getDefinition(interfaceDefinition.getParent().getTypeName());
        if (parentDefinition == null || !(parentDefinition instanceof InterfaceDefinition)) {
            logger.formatError("SuperCallConstructorProcessor: could not find parent for %s",
                    interfaceDefinition.getParent().getTypeName());
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
        return new Constructor(null, null, Collections.emptyList(),
                false, null, null);
    }

    private void findSuperCallArguments(Constructor constructor, InterfaceDefinition parentInterface) {
        if (constructor.getSuperArguments().isEmpty() && parentInterface != null
                && !parentInterface.getConstructors().isEmpty()) {
            Constructor parentConstructor = parentInterface.getConstructors().get(0);
            constructor.getSuperArguments().addAll(parentConstructor.getArguments());
        }
    }
}
