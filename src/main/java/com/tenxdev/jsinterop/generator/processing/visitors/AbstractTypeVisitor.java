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

package com.tenxdev.jsinterop.generator.processing.visitors;

import com.tenxdev.jsinterop.generator.model.types.*;

public abstract class AbstractTypeVisitor<T> {

    public T accept(Type type) {
        if (type.getClass() == NativeType.class) {
            return visitNativeType((NativeType) type);
        }
        if (type.getClass() == ObjectType.class) {
            return visitObjectType((ObjectType) type);
        }
        if (type.getClass() == EnumType.class) {
            return visitEnumType((EnumType) type);
        }
        if (type.getClass() == ParameterisedType.class) {
            return visitParameterisedType((ParameterisedType) type);
        }
        if (type.getClass() == UnionType.class) {
            return visitUnionType((UnionType) type);
        }
        if (type.getClass() == ArrayType.class) {
            return visitArrayType((ArrayType) type);
        }
        throw new IllegalStateException("Unknown type " + type.getClass().getName());
    }

    protected abstract T visitArrayType(ArrayType type);

    protected abstract T visitUnionType(UnionType type);

    protected abstract T visitParameterisedType(ParameterisedType type);

    protected abstract T visitEnumType(EnumType type);

    protected abstract T visitObjectType(ObjectType type);

    protected abstract T visitNativeType(NativeType type);
}
