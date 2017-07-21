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

import javax.annotation.Generated;
import java.util.List;
import java.util.stream.Collectors;

import static org.eclipse.xtext.xbase.lib.StringExtensions.toFirstUpper;

public class UnionType implements Type {

    private List<Type> types;
    private String name;
    private boolean shared;
    private String packageName;
    private String owner;

    public UnionType(String name, List<Type> types) {
        this.name = name;
        this.types = types;
        checkName();
    }

    private void checkName() {
        if (name == null && types != null) {
            this.name = types.stream()
                    .map(type -> toFirstUpper(type.displayValue()).replace("[]", "Array"))
                    .collect(Collectors.joining("Or")) + "UnionType";
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        checkName();
    }

    public List<Type> getTypes() {
        return types;
    }

    public void setTypes(List<Type> types) {
        this.types = types;
    }

    @Override
    public String displayValue() {
        return owner + "." + name;
    }

    @Override
    public String getTypeName() {
        return types.stream()
                .map(Type::getTypeName)
                .collect(Collectors.joining()) + "UnionType";
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    @Override
    @Generated("IntelliJ")
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
