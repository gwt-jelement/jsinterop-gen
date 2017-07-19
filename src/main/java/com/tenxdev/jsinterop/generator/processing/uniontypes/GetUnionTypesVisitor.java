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

import com.tenxdev.jsinterop.generator.model.types.*;
import com.tenxdev.jsinterop.generator.processing.visitors.AbstractTypeVisitor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class GetUnionTypesVisitor extends AbstractTypeVisitor<List<UnionType>> {
    @Override
    protected List<UnionType> visitArrayType(ArrayType type) {
        return accept(type.getType());
    }

    @Override
    protected List<UnionType> visitUnionType(UnionType type) {
        List<UnionType> result = new ArrayList<>();
        result.add(type);
        result.addAll(type.getTypes().stream()
                .map(this::accept)
                .flatMap(List::stream)
                .collect(Collectors.toList()));
        return result;
    }

    @Override
    protected List<UnionType> visitParameterisedType(ParameterisedType type) {
        List<UnionType> result = new ArrayList<>();
        result.addAll(accept(type.getBaseType()));
        result.addAll(type.getTypeParameters().stream()
                .map(this::accept)
                .flatMap(List::stream)
                .collect(Collectors.toList()));
        return result;
    }

    @Override
    protected List<UnionType> visitEnumType(EnumType type) {
        return Collections.emptyList();
    }

    @Override
    protected List<UnionType> visitObjectType(ObjectType type) {
        return Collections.emptyList();
    }

    @Override
    protected List<UnionType> visitNativeType(NativeType type) {
        return Collections.emptyList();
    }
}
