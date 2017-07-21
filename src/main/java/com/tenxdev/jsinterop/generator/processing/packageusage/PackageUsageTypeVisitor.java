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

import com.tenxdev.jsinterop.generator.model.types.*;
import com.tenxdev.jsinterop.generator.processing.visitors.AbstractTypeVisitor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class PackageUsageTypeVisitor extends AbstractTypeVisitor<List<String>> {

    @Override
    public List<String> accept(Type type) {
        if (type == null) {
            return Collections.emptyList();
        }
        return super.accept(type);
    }

    @Override
    protected List<String> visitArrayType(ArrayType type) {
        return accept(type.getType());
    }

    @Override
    protected List<String> visitUnionType(UnionType type) {
        return type.getTypes().stream()
                .map(this::accept)
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }

    @Override
    protected List<String> visitParameterisedType(ParameterisedType type) {
        List<String> result = new ArrayList<>();
        result.addAll(accept(type.getBaseType()));
        for (Type parameterType : type.getTypeParameters()) {
            result.addAll(accept(parameterType));
        }
        return result;
    }

    @Override
    protected List<String> visitEnumType(EnumType type) {
        return Collections.singletonList(type.getPackageName() + "." + type.getTypeName());
    }

    @Override
    protected List<String> visitObjectType(ObjectType type) {
        return Collections.singletonList(type.getPackageName() + "." + type.getTypeName());
    }

    @Override
    protected List<String> visitNativeType(NativeType type) {
        return Collections.emptyList();
    }
}
