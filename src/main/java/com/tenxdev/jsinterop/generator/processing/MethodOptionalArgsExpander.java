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
import com.tenxdev.jsinterop.generator.model.InterfaceDefinition;
import com.tenxdev.jsinterop.generator.model.Method;
import com.tenxdev.jsinterop.generator.model.MethodArgument;
import com.tenxdev.jsinterop.generator.model.Model;

import java.util.ArrayList;
import java.util.List;

/**
 * This class creates nee methods without optional arguments for methods with optional arguments, and removes all method
 * definitions with optional arguments.
 * For example, given foo(a, b?, c?)
 * the following methods will be added:
 * foo(a)
 * foo(a, b)
 * foo(a, b, c)
 * and the original method will be removed
 */
public class MethodOptionalArgsExpander {

    private final Model model;
    private Logger logger;

    public MethodOptionalArgsExpander(Model model, Logger logger) {
        this.model = model;
        this.logger = logger;
    }

    public void processModel() {
        logger.info(Logger.LEVEL_INFO, () -> "Expanding methods with optional arguments");
        model.getDefinitions().forEach(definitionInfo -> {
            if (definitionInfo.getDefinition() instanceof InterfaceDefinition) {
                processInterface((InterfaceDefinition) definitionInfo.getDefinition());
            }
        });
    }

    private void processInterface(InterfaceDefinition definition) {
        List<Method> newMethods = processMethods(definition.getMethods());
        definition.getMethods().clear();
        definition.getMethods().addAll(newMethods);

        List<Method> newConstructors = processMethods(definition.getConstructors());
        definition.getConstructors().clear();
        definition.getConstructors().addAll(newConstructors);
    }

    private List<Method> processMethods(List<Method> methods) {
        List<Method> newMethods = new ArrayList<>();
        methods.forEach(method -> {
            if (hasOptionalArgs(method)) {
                List<Method> expandedMethods = expandMethod(method);
                expandedMethods.removeAll(newMethods);
                newMethods.addAll(expandedMethods);
            } else {
                newMethods.add(method);
            }
        });
        return newMethods;
    }

    private boolean hasOptionalArgs(Method method) {
        return method.getArguments().stream().anyMatch(MethodArgument::isOptional);
    }

    private List<Method> expandMethod(Method method) {
        List<Method> expandedMethods = new ArrayList<>();
        expandMethod(method, expandedMethods);
        return expandedMethods;
    }

    private void expandMethod(Method method, List<Method> expandedMethods) {
        boolean hasOptions = false;
        List<MethodArgument> newArguments = new ArrayList<>();
        for (MethodArgument argument : method.getArguments()) {
            if (argument.isOptional() && !hasOptions) {
                Method newMethod = method.newMethodWithArguments(new ArrayList<>(newArguments));
                expandedMethods.add(newMethod);
                hasOptions = true;
                newArguments.add(new MethodArgument(argument.getName(), argument.getType(), argument.isVararg(), false, argument.getDefaultValue()));
            } else {
                newArguments.add(argument);
            }
        }
        Method newMethod = method.newMethodWithArguments(newArguments);
        if (hasOptions) {
            expandMethod(newMethod, expandedMethods);
        } else {
            expandedMethods.add(newMethod);
        }
    }

}
