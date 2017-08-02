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
import com.tenxdev.jsinterop.generator.model.types.ArrayType;
import com.tenxdev.jsinterop.generator.model.types.NativeType;
import com.tenxdev.jsinterop.generator.model.types.Type;

import java.util.ArrayList;
import java.util.Optional;

public class NativeTypeArrayArgumentHandler {

    private Model model;
    private Logger logger;

    public NativeTypeArrayArgumentHandler(Model model, Logger logger) {
        this.model = model;
        this.logger = logger;
    }

    public void process() {
        logger.info(() -> "Creating method overloads for methods with native array types");
        model.getInterfaceDefinitions().forEach(definition -> processInterface(model, definition));
    }

    private void processInterface(Model model, InterfaceDefinition definition) {
        processMethods(model, definition);
        processConstructors(model, definition);
    }

    private void processConstructors(Model model, InterfaceDefinition definition) {
        ArrayList<Constructor> newConstructors = new ArrayList<>();
        definition.getConstructors().forEach(constructor -> {
            Constructor newConstructor = processConstructor(model, definition, constructor);
            while (newConstructor != null) {
                newConstructors.add(newConstructor);
                newConstructor = processConstructor(model, definition, newConstructor);
            }
        });
        definition.getConstructors().addAll(newConstructors);

    }

    private void processMethods(Model model, InterfaceDefinition definition) {
        ArrayList<Method> newMethods = new ArrayList<>();
        definition.getMethods().forEach(method -> {
            Method newMethod = processMethod(model, definition, method);
            while (newMethod != null) {
                newMethods.add(newMethod);
                newMethod = processMethod(model, definition, newMethod);
            }
        });
        definition.getMethods().addAll(newMethods);
    }

    private Method processMethod(Model model, InterfaceDefinition definition, Method method) {
        return getNativeArrayArgument(method).<Method>map(argument -> {
            ArrayList<MethodArgument> newArguments = new ArrayList<>(method.getArguments());
            Type arrayType = model.getTypeFactory().getType("Array");
            MethodArgument newArgument = argument.newMethodArgumentWithType(arrayType);
            newArguments.replaceAll(argument1 -> argument1 == argument ? newArgument : argument1);
            return method.newMethodWithArguments(newArguments);
        }).orElse(null);
    }

    private Constructor processConstructor(Model model, InterfaceDefinition definition, Constructor constructor) {
        return getNativeArrayArgument(constructor).<Constructor>map(argument -> {
            ArrayList<MethodArgument> newArguments = new ArrayList<>(constructor.getArguments());
            Type arrayType = model.getTypeFactory().getType("Array");
            MethodArgument newArgument = argument.newMethodArgumentWithType(arrayType);
            newArguments.replaceAll(argument1 -> argument1 == argument ? newArgument : argument1);
            return constructor.newMethodWithArguments(newArguments);
        }).orElse(null);
    }

    private Optional<MethodArgument> getNativeArrayArgument(Method method) {
        return method.getArguments().stream()
                .filter(this::hasNativeArrayType)
                .findAny();
    }

    private boolean hasNativeArrayType(MethodArgument argument) {
        return argument.getType() instanceof ArrayType &&
                ((ArrayType) argument.getType()).getType() instanceof NativeType;
    }
}
