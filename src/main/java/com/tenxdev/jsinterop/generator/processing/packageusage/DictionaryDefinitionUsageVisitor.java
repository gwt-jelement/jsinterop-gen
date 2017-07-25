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

import com.tenxdev.jsinterop.generator.model.DictionaryDefinition;
import com.tenxdev.jsinterop.generator.model.DictionaryMember;
import com.tenxdev.jsinterop.generator.model.types.ArrayType;
import com.tenxdev.jsinterop.generator.model.types.Type;
import com.tenxdev.jsinterop.generator.model.types.UnionType;
import com.tenxdev.jsinterop.generator.processing.visitors.AbstractDictionaryDefinitionVisitor;

import java.util.List;
import java.util.stream.Collectors;

public class DictionaryDefinitionUsageVisitor extends AbstractDictionaryDefinitionVisitor<List<String>> {
    private final PackageUsageTypeVisitor typeVisitor = new PackageUsageTypeVisitor();

    @Override
    public List<String> accept(DictionaryDefinition definition) {
        List<String> result = super.accept(definition);
        result.add("jsinterop.annotations.JsPackage");
        result.add("jsinterop.annotations.JsProperty");
        result.add("jsinterop.annotations.JsType");
        result.add("jsinterop.annotations.JsOverlay");
        if (!definition.getUnionReturnTypes().isEmpty()) {
            result.add("jsinterop.base.Js");
            result.addAll(definition.getUnionReturnTypes().stream()
                    .map(UnionType::getTypes)
                    .flatMap(List::stream)
                    .map(typeVisitor::accept)
                    .flatMap(List::stream)
                    .collect(Collectors.toList()));
        }
        if (definition.getMembers().stream()
                .anyMatch(member -> member.getEnumSubstitutionType() instanceof ArrayType)) {
            result.add("java.util.Arrays");
        }
        result.addAll(definition.getUnionReturnTypes().stream()
                .filter(unionType -> definition != unionType.getOwner())
                .map(unionType -> unionType.getOwner().getPackageName() + "." + unionType.getOwner().getName())
                .collect(Collectors.toList()));
        return result;
    }

    @Override
    protected List<String> coallesce(List<List<String>> result) {
        return result.stream().flatMap(List::stream).collect(Collectors.toList());
    }

    @Override
    protected List<String> visitMembers(List<DictionaryMember> members) {
        return members.stream()
                .map(member -> typeVisitor.accept(member.getType()))
                .flatMap(List::stream)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }

    @Override
    protected List<String> visitParent(Type parent) {
        return typeVisitor.accept(parent);
    }
}
