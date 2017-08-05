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

public class NativeType implements Type {

    public static final NativeType CHAR = new NativeType("char");
    public static final NativeType BYTE = new NativeType("byte");
    public static final NativeType SHORT = new NativeType("short");
    public static final NativeType INT = new NativeType("int");
    public static final NativeType LONG = new NativeType("long");
    public static final NativeType FLOAT = new NativeType("float");
    public static final NativeType DOUBLE = new NativeType("double");
    public static final NativeType BOOLEAN = new NativeType("boolean");

    public static final NativeType[] NATIVE_TYPES=new NativeType[]{
            CHAR, BYTE, SHORT, INT, LONG, FLOAT, DOUBLE, BOOLEAN
    };

    private final String typeName;

    public NativeType(String typeName) {
        this.typeName = typeName;
    }

    @Override
    public String getTypeName() {
        return typeName;
    }

    @Override
    public String displayValue() {
        return typeName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NativeType that = (NativeType) o;

        return typeName != null ? typeName.equals(that.typeName) : that.typeName == null;
    }

    @Override
    public int hashCode() {
        return typeName != null ? typeName.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "NativeType{" +
                "typeName='" + typeName + '\'' +
                '}';
    }
}
