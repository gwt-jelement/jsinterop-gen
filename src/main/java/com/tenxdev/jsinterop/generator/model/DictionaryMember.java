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

import com.tenxdev.jsinterop.generator.model.interfaces.HasExtendedAttributes;
import com.tenxdev.jsinterop.generator.model.types.Type;

import java.util.List;

public class DictionaryMember implements HasExtendedAttributes {

    private final String name;
    private final boolean required;
    private final String defaultValue;
    private final Type type;
    private List<String> extendedAttributes;
    private Type enumSubstitutionType;

    public DictionaryMember(String name, Type type, boolean required, String defaultValue,
                            List<String> extendedAttributes) {
        this.name = name;
        this.type = type;
        this.required = required;
        this.defaultValue = defaultValue;
        this.extendedAttributes = extendedAttributes;
    }

    public boolean isRequired() {
        return required;
    }

    public Type getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public Type getEnumSubstitutionType() {
        return enumSubstitutionType;
    }

    public void setEnumSubstitutionType(Type enumSubstitutionType) {
        this.enumSubstitutionType = enumSubstitutionType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DictionaryMember that = (DictionaryMember) o;

        if (!name.equals(that.name)) return false;
        return type.equals(that.type);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + type.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "\n\tDictionaryMember{" +
                "name='" + name + '\'' +
                ", required=" + required +
                ", defaultValue='" + defaultValue + '\'' +
                ", type=" + type +
                ", extendedAttributes=" + extendedAttributes +
                ", enumSubstitutionType=" + enumSubstitutionType +
                '}';
    }

    @Override
    public boolean hasExtendedAttribute(String name) {
        return extendedAttributes != null && extendedAttributes.contains(name);
    }
}
