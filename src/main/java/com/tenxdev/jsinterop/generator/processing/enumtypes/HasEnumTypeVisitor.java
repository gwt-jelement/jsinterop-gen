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

package com.tenxdev.jsinterop.generator.processing.enumtypes;

import com.tenxdev.jsinterop.generator.model.types.*;
import com.tenxdev.jsinterop.generator.processing.visitors.AbstractTypeVisitor;

public class HasEnumTypeVisitor extends AbstractTypeVisitor<Boolean> {
    @Override
    protected Boolean visitArrayType(ArrayType type) {
        return accept(type.getType());
    }

    @Override
    protected Boolean visitUnionType(UnionType type) {
        return type.getTypes().stream()
                .anyMatch(this::accept);
    }

    @Override
    protected Boolean visitParameterisedType(ParameterisedType type) {
        return type.getTypeParameters().stream()
                .anyMatch(this::accept);
    }

    @Override
    protected Boolean visitEnumType(EnumType type) {
        return true;
    }

    @Override
    protected Boolean visitObjectType(ObjectType type) {
        return false;
    }

    @Override
    protected Boolean visitNativeType(NativeType type) {
        return false;
    }

    @Override
    protected Boolean visitGenericType(GenericType type) {
        return false;
    }
}
