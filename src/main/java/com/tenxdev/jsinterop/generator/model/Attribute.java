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

package com.tenxdev.jsinterop.generator.model;

import com.tenxdev.jsinterop.generator.model.types.Type;

public class Attribute implements InterfaceMember {
    private final boolean staticAttribute;
    private final String name;
    private final Type type;
    private final boolean readOnly;

    public Attribute(String name, Type type, boolean readOnly, boolean staticAttribute) {
        this.name = name;
        this.type = type;
        this.readOnly = readOnly;
        this.staticAttribute = staticAttribute;
    }

    public boolean isStatic() {
        return staticAttribute;
    }

    public String getName() {
        return name;
    }

    public Type getType() {
        return type;
    }

    public boolean isReadOnly() {
        return readOnly;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Attribute attribute = (Attribute) o;

        if (staticAttribute != attribute.staticAttribute) return false;
        if (readOnly != attribute.readOnly) return false;
        if (!name.equals(attribute.name)) return false;
        return type.equals(attribute.type);
    }

    @Override
    public int hashCode() {
        int result = (staticAttribute ? 1 : 0);
        result = 31 * result + name.hashCode();
        result = 31 * result + type.hashCode();
        result = 31 * result + (readOnly ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return "\n\tAttribute{" +
                "staticAttribute=" + staticAttribute +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", readOnly=" + readOnly +
                '}';
    }
}
