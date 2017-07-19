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

/**
 * provides replacement types for an type containing union types
 */
public class UnionTypeReplacementVisitor extends AbstractTypeVisitor<List<Type>> {

    @Override
    public List<Type> accept(Type type) {
        return type == null ? Collections.emptyList() : super.accept(type);
    }

    @Override
    protected List<Type> visitArrayType(ArrayType type) {
        List<Type> unionTypes = accept(type.getType());
        return unionTypes.stream()
                .map(ArrayType::new)
                .collect(Collectors.toList());
    }

    @Override
    protected List<Type> visitUnionType(UnionType type) {
        return type.getTypes();
    }

    @Override
    protected List<Type> visitParameterisedType(ParameterisedType type) {
        List<Type> types = accept(type.getBaseType());
        if (!types.isEmpty()) {
            return types.stream()
                    .map(unionType -> new ParameterisedType(unionType, type.getTypeParameters()))
                    .collect(Collectors.toList());
        }
        for (Type subType : type.getTypeParameters()) {
            List<Type> unionTypes = accept(subType);
            if (!unionTypes.isEmpty()) {
                List<Type> result = new ArrayList<>();
                for (Type unionType : unionTypes) {
                    ArrayList<Type> newSubTypes = new ArrayList<>(type.getTypeParameters());
                    int index = newSubTypes.indexOf(subType);
                    newSubTypes.set(index, unionType);
                    result.add(new ParameterisedType(type.getBaseType(), newSubTypes));
                }
                return result;
            }
        }
        return Collections.emptyList();
    }

    @Override
    protected List<Type> visitEnumType(EnumType type) {
        return Collections.emptyList();
    }

    @Override
    protected List<Type> visitObjectType(ObjectType type) {
        return Collections.emptyList();
    }

    @Override
    protected List<Type> visitNativeType(NativeType type) {
        return Collections.emptyList();
    }
}
