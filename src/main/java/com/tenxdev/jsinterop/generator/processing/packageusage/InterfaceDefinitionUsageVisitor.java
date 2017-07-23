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

package com.tenxdev.jsinterop.generator.processing.packageusage;

import com.tenxdev.jsinterop.generator.model.*;
import com.tenxdev.jsinterop.generator.model.types.PackageType;
import com.tenxdev.jsinterop.generator.model.types.UnionType;
import com.tenxdev.jsinterop.generator.processing.visitors.AbstractInterfaceDefinitionVisitor;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class InterfaceDefinitionUsageVisitor extends AbstractInterfaceDefinitionVisitor<List<String>> {

    private final PackageUsageTypeVisitor typeVisitor = new PackageUsageTypeVisitor();
    private MethodVisitor methodVisitor = new MethodVisitor();

    @Override
    public List<String> accept(InterfaceDefinition interfaceDefinition) {
        List<String> result = super.accept(interfaceDefinition);
        result.add("jsinterop.annotations.JsPackage");
        result.add("jsinterop.annotations.JsType");
        if (interfaceDefinition.getParent() instanceof PackageType) {
            PackageType packageType = (PackageType) interfaceDefinition.getParent();
            result.add(packageType.getPackageName() + "." + packageType.getTypeName());
        }
        if (!interfaceDefinition.getConstructors().isEmpty()) {
            result.add("jsinterop.annotations.JsConstructor");
        }
        if (!interfaceDefinition.getUnionReturnTypes().isEmpty()) {
            result.add("jsinterop.annotations.JsOverlay");
            result.add("jsinterop.annotations.JsType");
            result.add("jsinterop.base.Js");
            result.addAll(interfaceDefinition.getUnionReturnTypes().stream()
                    .map(UnionType::getTypes)
                    .flatMap(List::stream)
                    .map(typeVisitor::accept)
                    .flatMap(List::stream)
                    .collect(Collectors.toList()));
        }
        if (!interfaceDefinition.getMethods().isEmpty()) {
            result.add("jsinterop.annotations.JsMethod");
        }
        if (!interfaceDefinition.getAttributes().isEmpty()) {
            result.add("jsinterop.annotations.JsProperty");
        }
        if (interfaceDefinition.getMethods().stream()
                .anyMatch(method -> method.getEnumOverlay() != null)) {
            result.add("jsinterop.annotations.JsOverlay");
            result.add("jsinterop.base.Any");
        }
        if (interfaceDefinition.getAttributes().stream()
                .anyMatch(attribute -> attribute.getEnumSubstitutionType() != null)) {
            result.add("jsinterop.annotations.JsOverlay");
        }
        result.addAll(interfaceDefinition.getUnionReturnTypes().stream()
                .filter(unionType -> interfaceDefinition != unionType.getOwner())
                .map(unionType -> unionType.getOwner().getPackageName() + "." + unionType.getOwner().getName())
                .collect(Collectors.toList()));
        return result;
    }

    @Override
    protected List<String> visitConstructors(List<Constructor> constructors) {
        return constructors.stream()
                .map(methodVisitor::accept)
                .flatMap(List::stream)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }

    @Override
    protected List<String> visitFeatures(List<Feature> features) {
        //TODO revisit
        return Collections.emptyList();
    }

    @Override
    protected List<String> coallesce(List<List<String>> result) {
        return result.stream().flatMap(List::stream).collect(Collectors.toList());
    }

    @Override
    protected List<String> visitConstants(List<Constant> constants) {
        return constants.stream()
                .map(this::visitConstant)
                .flatMap(List::stream)
                .distinct()
                .sorted()
                .collect(Collectors.toList());

    }

    private List<String> visitConstant(Constant constant) {
        return typeVisitor.accept(constant.getType());
    }

    @Override
    protected List<String> visitAttributes(List<Attribute> attributes) {
        return new AttributesVisitor().accept(attributes);
    }

    @Override
    protected List<String> visitMethods(List<Method> methods) {
        return methods.stream()
                .map(methodVisitor::accept)
                .flatMap(List::stream)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }

}
