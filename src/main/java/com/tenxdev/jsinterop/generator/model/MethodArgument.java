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

public class MethodArgument {
    private final String defaultValue;
    private final String name;
    private final boolean vararg;
    private final boolean optional;
    private final Type type;
    private boolean enumSubstitution;

    public MethodArgument(String name, Type type, boolean vararg, boolean optional, String defaultValue,
                          ExtendedAttributes extendedAttributes) {
        this(name, type, vararg, optional, defaultValue);
    }

    private MethodArgument(String name, Type type, boolean vararg, boolean optional, String defaultValue) {
        this.type = type;
        this.name = name;
        this.vararg = vararg;
        this.optional = optional;
        this.defaultValue = defaultValue;
    }

    MethodArgument(MethodArgument methodArgument) {
        this.type = methodArgument.type;
        this.name = methodArgument.name;
        this.vararg = methodArgument.vararg;
        this.optional = methodArgument.optional;
        this.defaultValue = methodArgument.defaultValue;
    }

    public MethodArgument newMethodArgumentWithType(Type type) {
        return new MethodArgument(name, type, vararg, optional, defaultValue);
    }

    public MethodArgument newRequiredArgument() {
        return new MethodArgument(name, type, vararg, false, defaultValue);
    }

    @SuppressWarnings("unused")
    public String getDefaultValue() {
        return defaultValue;
    }

    public boolean isVararg() {
        return vararg;
    }

    public String getName() {
        return name;
    }

    public boolean isOptional() {
        return optional;
    }

    public Type getType() {
        return type;
    }

    public boolean isEnumSubstitution() {
        return enumSubstitution;
    }

    public void setEnumSubstitution(boolean enumSubstitution) {
        this.enumSubstitution = enumSubstitution;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MethodArgument that = (MethodArgument) o;

        if (vararg != that.vararg) return false;
        if (optional != that.optional) return false;
        if (defaultValue != null ? !defaultValue.equals(that.defaultValue) : that.defaultValue != null) return false;
        if (type != null ? !type.equals(that.type) : that.type != null) return false;
        return name != null ? name.equals(that.name) : that.name == null;
    }

    @Override
    public int hashCode() {
        int result = defaultValue != null ? defaultValue.hashCode() : 0;
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (vararg ? 1 : 0);
        result = 31 * result + (optional ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return "\nMethodArgument{" +
                "defaultValue='" + defaultValue + '\'' +
                ", type=" + type +
                ", name='" + name + '\'' +
                ", vararg=" + vararg +
                ", optional=" + optional +
                '}';
    }
}
