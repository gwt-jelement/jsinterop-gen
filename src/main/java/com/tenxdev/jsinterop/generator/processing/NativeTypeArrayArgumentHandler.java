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
import com.tenxdev.jsinterop.generator.model.types.ArrayType;
import com.tenxdev.jsinterop.generator.model.types.NativeType;
import com.tenxdev.jsinterop.generator.model.types.Type;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public class NativeTypeArrayArgumentHandler {

    private final Type arrayType;
    private final Model model;
    private final Logger logger;

    public NativeTypeArrayArgumentHandler(Model model, Logger logger) {
        this.model = model;
        this.logger = logger;
        this.arrayType = model.getTypeFactory().getType("Array");
    }

    public void process() {
        logger.info(() -> "Creating method overloads for methods with native array types");
        model.getInterfaceDefinitions().forEach(this::processInterface);
    }

    private void processInterface(InterfaceDefinition definition) {
        processMethods(definition::getMethods);
        processMethods(definition::getConstructors);
    }

    private <T extends Method> void processMethods(Supplier<List<T>> methodsSupplier) {
        ArrayList<T> newMethods = new ArrayList<>();
        methodsSupplier.get().forEach(method -> {
            T newMethod = processMethod(method);
            while (newMethod != null) {
                newMethods.add(newMethod);
                newMethod = processMethod(newMethod);
            }
        });
        methodsSupplier.get().addAll(newMethods);
    }

    private <T extends Method> T processMethod(T method) {
        return getNativeArrayArgument(method).<T>map(argument -> {
            ArrayList<MethodArgument> newArguments = new ArrayList<>(method.getArguments());
            MethodArgument newArgument = argument.newMethodArgumentWithType(arrayType);
            newArguments.replaceAll(argument1 -> argument1 == argument ? newArgument : argument1);
            return method.newMethodWithArguments(newArguments);
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
