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

import com.google.common.collect.ImmutableMap;

public interface Type {

    ImmutableMap<String, Type> BOXED_TYPES = ImmutableMap.<String, Type>builder()
            .put("void", new NativeType("Void"))
            .put("short", new NativeType("Double"))
            .put("int", new NativeType("Double"))
            .put("long", new NativeType("Double"))
            .put("float", new NativeType("Double"))
            .put("double", new NativeType("Double"))
            .put("byte", new NativeType("Double"))
            .put("boolean", new NativeType("Boolean"))
            .put("char", new NativeType("String"))
            .build();

    String displayValue();

    String getTypeName();

    default boolean isGwtPrimitiveType() {
        return this instanceof NativeType &&
                ("byte".equals(this.getTypeName())
                        || "char".equals(this.getTypeName())
                        || "short".equals(this.getTypeName())
                        || "int".equals(this.getTypeName())
                        || "float".equals(this.getTypeName())
                        || "double".equals(this.getTypeName())
                        || "boolean".equals(this.getTypeName()));

    }

    default boolean isLongPrimitiveType() {
        return this instanceof NativeType && "long".equals(this.getTypeName());
    }

    default boolean isNativeType() {
        return isGwtPrimitiveType() || isNativeType();
    }

    default Type box() {
        if (this instanceof NativeType) {
            Type boxedType = BOXED_TYPES.get(getTypeName());
            return boxedType != null ? boxedType : this;
        }
        return this;
    }


}
