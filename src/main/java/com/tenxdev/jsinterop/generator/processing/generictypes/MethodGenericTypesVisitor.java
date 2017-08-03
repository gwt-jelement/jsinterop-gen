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

import com.tenxdev.jsinterop.generator.model.Method;
import com.tenxdev.jsinterop.generator.model.types.*;
import com.tenxdev.jsinterop.generator.processing.visitors.AbstractTypeVisitor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class MethodGenericTypesVisitor extends AbstractTypeVisitor<List<String>> {

    public List<String> accept(Method method){
        ArrayList<String> result=new ArrayList<>();
        result.addAll(accept(method.getReturnType()));
        method.getArguments().forEach(argument->result.addAll(accept(argument.getType())));
        return result.stream().distinct().sorted().collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    protected List<String> visitArrayType(ArrayType type) {
        return accept(type.getType());
    }

    @Override
    protected List<String> visitUnionType(UnionType type) {
        ArrayList<String> result = new ArrayList<String>();
        type.getTypes().forEach(subtype-> result.addAll(accept(subtype)));
        return result.isEmpty()? Collections.emptyList():result;
    }

    @Override
    protected List<String> visitParameterisedType(ParameterisedType type) {
        ArrayList<String> result = new ArrayList<String>();
        result.addAll(accept(type.getBaseType()));
        type.getTypeParameters().forEach(subtype-> result.addAll(accept(subtype)));
        return result.isEmpty()? Collections.emptyList():result;
    }

    @Override
    protected List<String> visitEnumType(EnumType type) {
        return Collections.emptyList();
    }

    @Override
    protected List<String> visitObjectType(ObjectType type) {
        return Collections.emptyList();
    }

    @Override
    protected List<String> visitExtendedObjectType(ExtensionObjectType type) {
        return Collections.emptyList();
    }

    @Override
    protected List<String> visitNativeType(NativeType type) {
        return Collections.emptyList();
    }

    @Override
    protected List<String> visitGenericType(GenericType type) {
        return type.getTypeName().length()==1?
                Collections.singletonList(type.getTypeName()):
                Collections.emptyList();
    }
}
