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

import com.tenxdev.jsinterop.generator.model.interfaces.HasUnionReturnTypes;
import com.tenxdev.jsinterop.generator.model.interfaces.InterfaceMember;
import com.tenxdev.jsinterop.generator.model.types.Type;
import com.tenxdev.jsinterop.generator.model.types.UnionType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class InterfaceDefinition extends AbstractDefinition implements HasUnionReturnTypes {
    private final String jsTypeName;
    private final List<UnionType> unionReturnTypes = new ArrayList<>();
    private final String[] genericParameters;
    private final Type parent;
    private List<Constant> constants = new ArrayList<>();
    private List<Feature> features = new ArrayList<>();
    private List<Constructor> constructors = new ArrayList<>();
    private List<Method> methods = new ArrayList<>();
    private List<Attribute> attributes = new ArrayList<>();
    private List<Type> implementedInterfaces = new ArrayList<>();

    public InterfaceDefinition(String name, Type parent, List<Constructor> constructors,
                               List<InterfaceMember> members, ExtendedAttributes extendedAttributes) {
        super(name);
        this.parent = parent;
        this.constructors = constructors == null ? Collections.emptyList() : constructors;
        this.methods = members.stream()
                .filter(member -> member instanceof Method)
                .map(member -> (Method) member)
                .collect(Collectors.toCollection(ArrayList::new));
        this.attributes = members.stream()
                .filter(member -> member instanceof Attribute)
                .map(member -> (Attribute) member)
                .collect(Collectors.toList());
        this.constants = members.stream()
                .filter(member -> member instanceof Constant)
                .map(member -> (Constant) member)
                .collect(Collectors.toList());
        this.features = members.stream()
                .filter(member -> member instanceof Feature)
                .map(member -> (Feature) member)
                .collect(Collectors.toList());
        members.stream()
                .filter(member -> member instanceof SpecialOperationMembers)
                .map(member -> (SpecialOperationMembers) member)
                .forEach(member -> this.methods.addAll(member.getMethods()));
        this.jsTypeName = extendedAttributes.extractValue(ExtendedAttributes.JS_TYPE_NAME,
                extendedAttributes.hasExtendedAttribute(ExtendedAttributes.NO_INTERFACE_OBJECT) ? "Object" : name);
        this.genericParameters = extendedAttributes.extractValues(ExtendedAttributes.GENERIC_PARAMETER, null);
    }

    protected InterfaceDefinition(InterfaceDefinition interfaceDefinition) {
        super(interfaceDefinition.getName());
        this.parent = interfaceDefinition.parent;
        this.constructors = interfaceDefinition.constructors;
        this.methods = interfaceDefinition.methods;
        this.attributes = interfaceDefinition.attributes;
        this.constants = interfaceDefinition.constants;
        this.features = interfaceDefinition.features;
        this.jsTypeName = interfaceDefinition.jsTypeName;
        this.genericParameters = interfaceDefinition.genericParameters;
        this.implementedInterfaces = interfaceDefinition.implementedInterfaces;
    }

    public List<Type> getImplementedInterfaces() {
        return implementedInterfaces;
    }

    public String[] getGenericParameters() {
        return genericParameters;
    }

    public Type getParent() {
        return parent;
    }

    public List<Method> getMethods() {
        return methods;
    }

    public List<Attribute> getAttributes() {
        return attributes;
    }

    public List<Constructor> getConstructors() {
        return constructors;
    }

    public List<Constant> getConstants() {
        return constants;
    }

    public List<Feature> getFeatures() {
        return features;
    }

    @Override
    public List<UnionType> getUnionReturnTypes() {
        return unionReturnTypes;
    }

    public String getJsTypeName() {
        return jsTypeName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        InterfaceDefinition that = (InterfaceDefinition) o;

        if (getName() != null ? !getName().equals(that.getName()) : that.getName() != null) return false;
        if (parent != null ? !parent.equals(that.parent) : that.parent != null) return false;
        if (constants != null ? constants.equals(that.constants) : that.constants == null)
            if (features != null ? features.equals(that.features) : that.features == null)
                if (constructors != null ? constructors.equals(that.constructors) : that.constructors == null)
                    if (methods != null ? methods.equals(that.methods) : that.methods == null)
                        if (attributes != null ? attributes.equals(that.attributes) : that.attributes == null)
                            return true;
        return false;
    }

    @Override
    public int hashCode() {
        int result = getName() != null ? getName().hashCode() : 0;
        result = 31 * result + (parent != null ? parent.hashCode() : 0);
        result = 31 * result + (constants != null ? constants.hashCode() : 0);
        result = 31 * result + (features != null ? features.hashCode() : 0);
        result = 31 * result + (constructors != null ? constructors.hashCode() : 0);
        result = 31 * result + (methods != null ? methods.hashCode() : 0);
        result = 31 * result + (attributes != null ? attributes.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "\n" + getClass().getSimpleName() +
                "(constructors=" + constructors +
                ", name='" + getName() + '\'' +
                ", parent='" + parent + '\'' +
                ", methods=" + methods +
                ", attributes=" + attributes +
                ", constants=" + constants +
                ", features=" + features +
                '}';
    }
}

