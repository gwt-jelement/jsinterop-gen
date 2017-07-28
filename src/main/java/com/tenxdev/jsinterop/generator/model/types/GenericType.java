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

public class GenericType implements Type {

    private final String typeName;

    public GenericType(String typeName) {
        super();
        this.typeName = typeName;
    }

    @Override
    public String displayValue() {
        return typeName;
    }

    @Override
    public String getTypeName() {
        return typeName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GenericType that = (GenericType) o;

        return typeName != null ? typeName.equals(that.typeName) : that.typeName == null;
    }

    @Override
    public int hashCode() {
        return typeName != null ? typeName.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "GenericType{" +
                "typeName='" + typeName + '\'' +
                '}';
    }
}
