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
import com.tenxdev.jsinterop.generator.model.*;
import com.tenxdev.jsinterop.generator.model.types.Type;
import com.tenxdev.jsinterop.generator.model.types.UnionType;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
    private final GetUnionTypesVisitor getUnionTypesVisitor = new GetUnionTypesVisitor();
    private final RemoveEnumUnionTypeVisitor removeEnumUnionTypeVisitor;
    private Logger logger;

    public MethodUnionArgsExpander(Model model, Logger logger) {
        this.model = model;
        this.logger = logger;
        this.removeEnumUnionTypeVisitor = new RemoveEnumUnionTypeVisitor(model, logger);
    }

    public void processModel() {
        logger.info(Logger.LEVEL_INFO, () -> "Expanding methods with union type arguments");
        model.getInterfaceDefinitions().forEach(this::processInterface);
    }

    private void processInterface(InterfaceDefinition definition) {
        expandMethodArguments(definition);
        expandConstructorArguments(definition);
        findUnionReturnTypes(definition);
    }

    private void expandMethodArguments(InterfaceDefinition definition) {
        List<Method> newMethods = processMethods(definition.getMethods());
        definition.getMethods().clear();
        definition.getMethods().addAll(newMethods);
    }

    private void expandConstructorArguments(InterfaceDefinition definition) {
        List<Constructor> newConstructors = processMethods(definition.getConstructors());
        definition.getConstructors().clear();
        definition.getConstructors().addAll(newConstructors);
    }

    private void findUnionReturnTypes(InterfaceDefinition definition) {
        List<UnionType> unionReturnTypes = definition.getMethods().stream()
                .map(method -> getUnionTypesVisitor.accept(method.getReturnType()))
                .flatMap(List::stream)
                .distinct()
                .collect(Collectors.toList());
        removeEnumUnionTypeVisitor.visitUnionTypes(unionReturnTypes)
                .forEach(unionType -> definition.addUnionReturnType(definition, unionType, unionType.getName()));
    }

    private <T extends Method> List<T> processMethods(List<T> methods) {
        List<T> newMethods = new ArrayList<>();
        methods.forEach(method -> processMethod(method, newMethods));
        return newMethods;
    }

    private <T extends Method> void processMethod(T method, List<T> newMethods) {
        UnionTypeReplacementVisitor unionTypeVisitor = new UnionTypeReplacementVisitor();
        for (MethodArgument methodArgument : method.getArguments()) {
            List<Type> suggestedTypes = unionTypeVisitor.accept(methodArgument.getType());
            if (!suggestedTypes.isEmpty()) {
                processArgument(method, methodArgument, suggestedTypes, newMethods);
                return;
            }
        }
        newMethods.add(method);
    }

    private <T extends Method> void processArgument(T method, MethodArgument argument,
                                                    List<Type> suggestedTypes, List<T> newMethods) {
        int argumentIndex = method.getArguments().indexOf(argument);
        for (Type type : suggestedTypes) {
            List<MethodArgument> newArguments = new ArrayList<>(method.getArguments());
            newArguments.set(argumentIndex, argument.newMethodArgumentWithType(type));
            T newMethod = method.newMethodWithArguments(newArguments);
            newMethod.setEnumOverlay(method.getEnumOverlay());
            processMethod(newMethod, newMethods);
        }
    }


}
