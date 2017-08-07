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
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class GenericTypesProcessor {

    private final MethodGenericTypesVisitor methodVisitor;
    private final Model model;
    private final Logger logger;
    private final Pattern TYPE_PATTERN = Pattern.compile("([A-Z]) extends .*");

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
            methodGenericTypes.removeAll(definitionGenericTypes);
        }
        if (method.getGenericTypeSpecifiers() != null) {
            methodGenericTypes.removeAll(extractExistingGenericTypes(method.getGenericTypeSpecifiers()));
            methodGenericTypes.add(0, method.getGenericTypeSpecifiers());
        }
        if (!methodGenericTypes.isEmpty()) {
            method.setGenericTypeSpecifiers(methodGenericTypes.stream().collect(Collectors.joining(", ")));
        }
    }

    private List<String> extractExistingGenericTypes(String genericTypeSpecifiers) {
        return Arrays.stream(genericTypeSpecifiers.split(","))
                .map(String::trim)
                .map(this::extractGenericType)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private String extractGenericType(String type) {
        if (type.length() == 1) {
            return type;
        }
        Matcher matcher = TYPE_PATTERN.matcher(type);
        if (matcher.find() && matcher.groupCount() == 1) {
            return matcher.group(1);
        }
        return null;
    }
}