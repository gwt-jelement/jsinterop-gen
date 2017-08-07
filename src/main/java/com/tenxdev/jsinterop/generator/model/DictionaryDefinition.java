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
import com.tenxdev.jsinterop.generator.model.types.Type;
import com.tenxdev.jsinterop.generator.model.types.UnionType;

import java.util.ArrayList;
import java.util.List;

public class DictionaryDefinition extends AbstractDefinition implements HasUnionReturnTypes {
    private final Type parent;
    private final String[] genericParameters;
    private List<DictionaryMember> members;
    private final List<UnionType> unionReturnTypes = new ArrayList<>();

    public DictionaryDefinition(String name, Type parent, List<DictionaryMember> members, ExtendedAttributes extendedAttributes) {
        super(name);
        this.parent = parent;
        this.members = members;
        this.genericParameters = extendedAttributes.extractValues(ExtendedAttributes.GENERIC_PARAMETER, null);
    }

    protected DictionaryDefinition(DictionaryDefinition dictionaryDefinition) {
        super(dictionaryDefinition.getName());
        this.parent = dictionaryDefinition.getParent();
        this.members = dictionaryDefinition.getMembers();
        this.genericParameters = dictionaryDefinition.genericParameters;
    }

    public Type getParent() {
        return parent;
    }

    public List<DictionaryMember> getMembers() {
        if (members == null) {
            members = new ArrayList<>();
        }
        return members;
    }

    public String[] getGenericParameters() {
        return genericParameters;
    }

    @Override
    public List<UnionType> getUnionReturnTypes() {
        return unionReturnTypes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DictionaryDefinition that = (DictionaryDefinition) o;
        return getName().equals(that.getName())
                && (parent != null ? parent.equals(that.parent) : that.parent == null)
                && (members != null ? members.equals(that.members) : that.members == null);
    }

    @Override
    public int hashCode() {
        int result = getName().hashCode();
        result = 31 * result + (parent != null ? parent.hashCode() : 0);
        result = 31 * result + (members != null ? members.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "\n" + getClass().getSimpleName() +
                "{" +
                "name='" + getName() + '\'' +
                ", parent='" + parent + '\'' +
                ", members=" + members +
                '}';
    }
}
