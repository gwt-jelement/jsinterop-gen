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

package com.tenxdev.jsinterop.generator.processing.uniontypes;

import com.tenxdev.jsinterop.generator.logging.Logger;
import com.tenxdev.jsinterop.generator.model.InterfaceDefinition;
import com.tenxdev.jsinterop.generator.model.Method;
import com.tenxdev.jsinterop.generator.model.MethodArgument;
import com.tenxdev.jsinterop.generator.model.Model;
import com.tenxdev.jsinterop.generator.model.types.Type;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * This class generates new methods for methods with union type arguments, and removes the definition of methods with
 * union types
 * for example, given foo( (HTMLImageElement or SVGImageElement or HTMLVideoElement ) image, double x, double y)
 * The following methods will be created, keeping a pointer to the original method, and the original method will be
 * removed from the definition:
 * - foo(HTMLImageElement image, double x, double y)
 * - foo(SVGImageElement image, double x, double y)
 * - foo(HTMLVideoElement image, double x, double y)
 */
public class MethodUnionArgsExpander {
    private final Model model;
    private final Logger logger;
    private final UnionTypeReplacementVisitor unionTypeVisitor;
    private final HasUnionTypeVisitor hasUnionTypeVisitor;
    private final GetUnionTypesVisitor getUnionTypeVisitor;

    public MethodUnionArgsExpander(Model model, Logger logger) {
        this.model = model;
        this.logger = logger;
        this.unionTypeVisitor = new UnionTypeReplacementVisitor();
        this.hasUnionTypeVisitor = new HasUnionTypeVisitor();
        this.getUnionTypeVisitor = new GetUnionTypesVisitor();
    }

    public void processModel() {
        logger.info(() -> "Expanding methods with union type arguments");
        model.getInterfaceDefinitions().forEach(this::processInterface);
    }

    private void processInterface(InterfaceDefinition definition) {
        expandMethodArguments(definition, definition::getMethods);
        expandMethodArguments(definition, definition::getConstructors);
    }

    private <T extends Method> void expandMethodArguments(InterfaceDefinition definition,
                                                          Supplier<List<T>> methodsSupplier) {
        List<T> newMethods = processMethods(definition, methodsSupplier.get());
        methodsSupplier.get().clear();
        methodsSupplier.get().addAll(newMethods);
    }

    private <T extends Method> List<T> processMethods(InterfaceDefinition definition, List<T> methods) {
        List<T> newMethods = new ArrayList<>();
        methods.forEach(method -> processMethod(definition, method, newMethods));
        return newMethods;
    }

    private <T extends Method> void processMethod(InterfaceDefinition definition, T method, List<T> newMethods) {
        if (hasUnionTypeVisitor.accept(method.getReturnType())) {
            getUnionTypeVisitor.accept(method.getReturnType()).forEach(unionType ->
                    definition.addUnionReturnType(definition, unionType, method.getName()));
        }
        for (MethodArgument methodArgument : method.getArguments()) {
            List<Type> suggestedTypes = unionTypeVisitor.accept(methodArgument.getType());
            if (!suggestedTypes.isEmpty()) {
                processArgument(definition, method, methodArgument, suggestedTypes, newMethods);
                return;
            }
        }
        newMethods.add(method);
    }

    private <T extends Method> void processArgument(InterfaceDefinition definition,
                                                    T method, MethodArgument argument,
                                                    List<Type> suggestedTypes, List<T> newMethods) {
        int argumentIndex = method.getArguments().indexOf(argument);
        for (Type type : suggestedTypes) {
            List<MethodArgument> newArguments = new ArrayList<>(method.getArguments());
            newArguments.set(argumentIndex, argument.newMethodArgumentWithType(type));
            T newMethod = method.newMethodWithArguments(newArguments);
            processMethod(definition, newMethod, newMethods);
        }
    }


}
