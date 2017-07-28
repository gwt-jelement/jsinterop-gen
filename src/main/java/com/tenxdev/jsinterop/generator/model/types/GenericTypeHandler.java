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

import java.util.Collections;

public enum GenericTypeHandler {

    INSTANCE;

    public Type getEffectiveType(Type type, String genericSubstitution, String genericParameter) {
        if (genericSubstitution != null) {
            return new GenericType(genericSubstitution);
        }
        if (genericParameter != null && type.getClass() == ObjectType.class) {
            return new ParameterisedType(type, Collections.singletonList(new GenericType(genericParameter)));
        }
        return type;
    }

    public String getTypeSpecifiers(String genericSubstitution) {
        return genericSubstitution == null ? null : genericSubstitution;
    }
}
