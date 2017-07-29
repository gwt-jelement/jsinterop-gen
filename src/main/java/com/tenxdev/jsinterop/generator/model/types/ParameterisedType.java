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

package com.tenxdev.jsinterop.generator.model.types;

import java.util.List;
import java.util.stream.Collectors;

public class ParameterisedType implements Type {

    private final List<Type> typeParameters;
    private final Type baseType;

    public ParameterisedType(Type baseType, List<Type> typeParameters) {
        this.baseType = baseType;
        this.typeParameters = typeParameters;
    }

    public List<Type> getTypeParameters() {
        return typeParameters;
    }

    public Type getBaseType() {
        return baseType;
    }

    @Override
    public String displayValue() {
        if ("JsObject".equals(baseType.displayValue()) && typeParameters.size() == 2) {
            return "JsObject<" + typeParameters.get(1).displayValue() + ">";
        }
        return baseType.displayValue() + (typeParameters.isEmpty() ? "" : (
                "<" + typeParameters.stream()
                        .map(Type::displayValue)
                        .map(s -> s.isEmpty()?"?":s)
                        .collect(Collectors.joining(", "))
                        + ">"));
    }

    @Override
    public String getTypeName() {
        String result = baseType.getTypeName();
        if (!typeParameters.isEmpty()) {
            result += "Of" + typeParameters.get(0).getTypeName();
        }
        if (typeParameters.size() > 1) {
            result += "And" + typeParameters.get(1).getTypeName();
        }
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ParameterisedType that = (ParameterisedType) o;

        if (!typeParameters.equals(that.typeParameters)) return false;
        return baseType.equals(that.baseType);
    }

    @Override
    public int hashCode() {
        int result = typeParameters.hashCode();
        result = 31 * result + baseType.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "ParameterisedType{" +
                "typeParameters=" + typeParameters +
                ", baseType=" + baseType +
                '}';
    }
}
