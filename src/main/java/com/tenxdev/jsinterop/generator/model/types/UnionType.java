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

public class UnionType implements Type {

    private final List<Type> types;
    private String name;

    public UnionType(String name, List<Type> types) {
        this.name = name;
        this.types = types;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Type> getTypes() {
        return types;
    }

    @Override
    public String displayValue() {
        return name != null ? name : types.stream()
                .map(Type::displayValue)
                .collect(Collectors.joining(" or ", "(", ")"));
    }

    @Override
    public String getTypeName() {
        return types.stream()
                .map(Type::getTypeName)
                .collect(Collectors.joining()) + "UnionType";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UnionType unionType = (UnionType) o;

        if (!types.equals(unionType.types)) return false;
        return name != null ? name.equals(unionType.name) : unionType.name == null;
    }

    @Override
    public int hashCode() {
        int result = types.hashCode();
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "UnionType{" +
                "types=" + types +
                ", name='" + name + '\'' +
                '}';
    }
}
