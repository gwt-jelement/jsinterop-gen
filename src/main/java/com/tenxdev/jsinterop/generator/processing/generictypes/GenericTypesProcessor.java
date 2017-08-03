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

package com.tenxdev.jsinterop.generator.processing.generictypes;

import com.tenxdev.jsinterop.generator.logging.Logger;
import com.tenxdev.jsinterop.generator.model.InterfaceDefinition;
import com.tenxdev.jsinterop.generator.model.Method;
import com.tenxdev.jsinterop.generator.model.Model;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class GenericTypesProcessor {

    private final MethodGenericTypesVisitor methodVisitor;
    private Model model;
    private Logger logger;

    public GenericTypesProcessor(Model model, Logger logger) {
        this.model = model;
        this.logger = logger;
        this.methodVisitor = new MethodGenericTypesVisitor();
    }

    public void process() {
        logger.info(() -> "Processing generic types");
        model.getInterfaceDefinitions().forEach(this::processInterface);
    }

    private void processInterface(InterfaceDefinition definition) {
        List<String> definitionGenericTypes = definition.getGenericParameters() == null ?
                Collections.emptyList() :
                Arrays.asList(definition.getGenericParameters());
        definition.getMethods().forEach(method -> processMethod(method, definitionGenericTypes));
    }

    private void processMethod(Method method, List<String> definitionGenericTypes) {
        List<String> methodGenericTypes = methodVisitor.accept(method);
        if (!method.isStatic()) {
            definitionGenericTypes.forEach(defitionGenericType -> methodGenericTypes.remove(defitionGenericType));
        }
        method.setGenericTypeSpecifiers(methodGenericTypes.isEmpty() ? null :
                methodGenericTypes.stream().collect(Collectors.joining(",")));
    }
}