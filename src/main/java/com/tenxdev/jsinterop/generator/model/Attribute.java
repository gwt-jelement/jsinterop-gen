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

import com.tenxdev.jsinterop.generator.model.interfaces.InterfaceMember;
import com.tenxdev.jsinterop.generator.model.types.Type;

public class Attribute implements InterfaceMember {
    private final boolean staticAttribute;
    private final String name;
    private final String jsPropertyName;
    private final boolean deprecated;
    private final Type type;
    private final boolean readOnly;
    private final boolean writeOnly;
    private final Type enumSubstitutionType;

    public Attribute(String name, Type type, boolean readOnly, boolean staticAttribute, ExtendedAttributes extendedAttributes) {
        this(name, type, readOnly, staticAttribute,
                extendedAttributes.extractValue(ExtendedAttributes.JS_PROPERTY_NAME, name),
                extendedAttributes.hasExtendedAttribute(ExtendedAttributes.DEPRECATED),
                false, null);
    }

    protected Attribute(String name, Type type, boolean readOnly, boolean staticAttribute, String jsPropertyName,
                        boolean deprecated, boolean writeOnly, Type enumSubstitutionType) {
        this.name = name;
        this.type = type;
        this.readOnly = readOnly;
        this.staticAttribute = staticAttribute;
        this.jsPropertyName = jsPropertyName;
        this.deprecated = deprecated;
        this.writeOnly = writeOnly;
        this.enumSubstitutionType = enumSubstitutionType;
    }

    protected Attribute(Attribute attribute) {
        this.name = attribute.name;
        this.type = attribute.type;
        this.readOnly = attribute.readOnly;
        this.staticAttribute = attribute.staticAttribute;
        this.jsPropertyName = attribute.jsPropertyName;
        this.deprecated = attribute.deprecated;
        this.writeOnly = attribute.writeOnly;
        this.enumSubstitutionType = attribute.enumSubstitutionType;
    }

    public Attribute newWriteOnlyAttribute() {
        return new Attribute(name, type, readOnly, staticAttribute, jsPropertyName, deprecated,
                true, enumSubstitutionType);
    }

    public Attribute newAttributeWithEnumSubstitutionType(Type enumSubstitutionType) {
        return new Attribute(name, type, readOnly, staticAttribute, jsPropertyName, deprecated,
                writeOnly, enumSubstitutionType);
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

    public boolean isWriteOnly() {
        return writeOnly;
    }

    public Type getEnumSubstitutionType() {
        return enumSubstitutionType;
    }

    public String getJsPropertyName() {
        return jsPropertyName;
    }

    public boolean isDeprecated() {
        return deprecated;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Attribute attribute = (Attribute) o;
        return staticAttribute == attribute.staticAttribute && deprecated == attribute.deprecated
                && readOnly == attribute.readOnly
                && writeOnly == attribute.writeOnly
                && name.equals(attribute.name)
                && (jsPropertyName != null ? jsPropertyName.equals(attribute.jsPropertyName) : attribute.jsPropertyName == null)
                && type.equals(attribute.type)
                && (enumSubstitutionType != null ? enumSubstitutionType.equals(attribute.enumSubstitutionType) : attribute.enumSubstitutionType == null);
    }

    @Override
    public int hashCode() {
        int result = (staticAttribute ? 1 : 0);
        result = 31 * result + name.hashCode();
        result = 31 * result + (jsPropertyName != null ? jsPropertyName.hashCode() : 0);
        result = 31 * result + (deprecated ? 1 : 0);
        result = 31 * result + type.hashCode();
        result = 31 * result + (readOnly ? 1 : 0);
        result = 31 * result + (writeOnly ? 1 : 0);
        result = 31 * result + (enumSubstitutionType != null ? enumSubstitutionType.hashCode() : 0);
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
