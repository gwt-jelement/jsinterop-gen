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

import com.tenxdev.jsinterop.generator.logging.Logger;
import com.tenxdev.jsinterop.generator.model.AbstractDefinition;
import com.tenxdev.jsinterop.generator.model.EnumDefinition;
import com.tenxdev.jsinterop.generator.model.Model;
import com.tenxdev.jsinterop.generator.model.types.*;
import com.tenxdev.jsinterop.generator.processing.visitors.AbstractTypeVisitor;

import java.util.stream.Collectors;

public class EnumSubstitutionVisitor extends AbstractTypeVisitor<Type> {

    private final Model model;
    private final Logger logger;

    EnumSubstitutionVisitor(Model model, Logger logger) {
        super();
        this.model = model;
        this.logger = logger;
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
        return new ParameterisedType(type.getBaseType(),
                type.getTypeParameters().stream()
                        .map(this::accept)
                        .collect(Collectors.toList()));
    }

    @Override
    protected Type visitEnumType(EnumType type) {
        AbstractDefinition definition = model.getDefinition(type.getTypeName());
        if (definition instanceof EnumDefinition) {
            return ((EnumDefinition) definition).getJavaElementType();
        } else {
            logger.formatError("Unexpected definition for enum type %s", type.getTypeName());
            return type;
        }
    }

    @Override
    protected Type visitObjectType(ObjectType type) {
        return type;
    }

    @Override
    protected Type visitExtendedObjectType(ExtensionObjectType type) {
        return type;
    }

    @Override
    protected Type visitNativeType(NativeType type) {
        return type;
    }

    @Override
    protected Type visitGenericType(GenericType type) {
        return type;
    }
}
