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

import com.tenxdev.jsinterop.generator.model.interfaces.Definition;
import com.tenxdev.jsinterop.generator.model.interfaces.HasUnionReturnTypes;
import com.tenxdev.jsinterop.generator.model.interfaces.InterfaceMember;
import com.tenxdev.jsinterop.generator.model.types.Type;
import com.tenxdev.jsinterop.generator.model.types.UnionType;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class InterfaceDefinition implements Definition, HasUnionReturnTypes {
    private final String name;
    private final Type parent;
    private List<Constant> constants;
    private List<Feature> features;
    private List<Method> constructors;
    private List<Method> methods;
    private List<Attribute> attributes;
    private List<UnionType> unionReturnTypes = new ArrayList<>();

    public InterfaceDefinition(String name, Type parent, List<Method> constructors, List<InterfaceMember> members) {
        this.name = name;
        this.parent = parent;
        this.constructors = constructors;
        this.methods = members.stream().filter(member -> member instanceof Method)
                .map(member -> (Method) member).collect(Collectors.toList());
        this.attributes = members.stream().filter(member -> member instanceof Attribute)
                .map(member -> (Attribute) member).collect(Collectors.toList());
        this.constants = members.stream().filter(member -> member instanceof Constant)
                .map(member -> (Constant) member).collect(Collectors.toList());
        this.features = members.stream().filter(member -> member instanceof Feature)
                .map(member -> (Feature) member).collect(Collectors.toList());
    }

    InterfaceDefinition(InterfaceDefinition interfaceDefinition) {
        this.name = interfaceDefinition.name;
        this.parent = interfaceDefinition.parent;
        this.constructors = interfaceDefinition.constructors;
        this.methods = interfaceDefinition.methods;
        this.attributes = interfaceDefinition.attributes;
        this.constants = interfaceDefinition.constants;
        this.features = interfaceDefinition.features;
    }

    @Override
    public String getName() {
        return name;
    }

    public Type getParent() {
        return parent;
    }

    public List<Method> getMethods() {
        if (methods == null) {
            methods = new ArrayList<>();
        }
        return methods;
    }

    public List<Attribute> getAttributes() {
        if (attributes == null) {
            attributes = new ArrayList<>();
        }
        return attributes;
    }

    public List<Method> getConstructors() {
        if (constructors == null) {
            constructors = new ArrayList<>();
        }
        return constructors;
    }

    public List<Constant> getConstants() {
        if (constants == null) {
            constants = new ArrayList<>();
        }
        return constants;
    }

    public List<Feature> getFeatures() {
        if (features == null) {
            features = new ArrayList<>();
        }

        return features;
    }

    @Override
    public List<UnionType> getUnionReturnTypes() {
        return unionReturnTypes;
    }

    @Override
    public void setUnionReturnTypes(List<UnionType> unionReturnTypes) {
        this.unionReturnTypes = unionReturnTypes;
    }

    public List<Attribute> getReadableAttributes() {
        return attributes.stream()
                .filter(attribute -> !attribute.isWriteOnly())
                .collect(Collectors.toList());
    }

    public List<Attribute> getWriteableAttributes() {
        return attributes.stream()
                .filter(attribute -> !attribute.isReadOnly())
                .collect(Collectors.toList());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        InterfaceDefinition that = (InterfaceDefinition) o;

        if (name != null ? !name.equals(that.name) : that.name != null) return false;
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
        int result = name != null ? name.hashCode() : 0;
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
                ", name='" + name + '\'' +
                ", parent='" + parent + '\'' +
                ", methods=" + methods +
                ", attributes=" + attributes +
                ", constants=" + constants +
                ", features=" + features +
                '}';
    }

}

