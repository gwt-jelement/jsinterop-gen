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

import com.tenxdev.jsinterop.generator.model.types.*;
import com.tenxdev.jsinterop.generator.processing.visitors.AbstractTypeVisitor;

import java.util.stream.Collectors;

public class DeferredTypeAdjuster extends AbstractTypeVisitor<Type> {

    private final TypeFactory typeFactory;

    DeferredTypeAdjuster(TypeFactory typeFactory) {
        this.typeFactory = typeFactory;
    }

    @Override
    public Type accept(Type type) {
        return type == null ? new NativeType("Object") : super.accept(type);
    }

    @Override
    protected Type visitArrayType(ArrayType type) {
        return new ArrayType(accept(type.getType()));
    }

    @Override
    protected Type visitUnionType(UnionType type) {
        type.setTypes(type.getTypes().stream()
                .map(this::accept)
                .collect(Collectors.toList()));
        return type;
    }

    @Override
    protected Type visitParameterisedType(ParameterisedType type) {
        return new ParameterisedType(accept(type.getBaseType()),
                type.getTypeParameters().stream()
                        .map(this::accept)
                        .collect(Collectors.toList()));
    }

    @Override
    protected Type visitEnumType(EnumType type) {
        return type;
    }

    @Override
    protected Type visitObjectType(ObjectType type) {
        return type;
    }

    @Override
    protected Type visitNativeType(NativeType type) {
        return typeFactory.getTypeNoParse(type.getTypeName());
    }
}
