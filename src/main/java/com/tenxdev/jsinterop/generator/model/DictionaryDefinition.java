package com.tenxdev.jsinterop.generator.model;

import com.tenxdev.jsinterop.generator.processing.TypeUtil;

import java.util.*;

public class DictionaryDefinition implements Definition {
    private final String name;
    private final String parent;
    private List<DictionaryMember> members;

    public DictionaryDefinition(String name, String parent, List<DictionaryMember> members) {
        this.name = name;
        this.parent = parent;
        this.members = members;
    }

    public String getParent() {
        return parent;
    }

    @Override
    public String getName() {
        return name;
    }

    public List<DictionaryMember> getMembers() {
        if (members==null){
            members=new ArrayList<>();
        }
        return members;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DictionaryDefinition that = (DictionaryDefinition) o;
        if (!name.equals(that.name)) return false;
        if (parent != null ? !parent.equals(that.parent) : that.parent != null) return false;
        return members != null ? members.equals(that.members) : that.members == null;
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + (parent != null ? parent.hashCode() : 0);
        result = 31 * result + (members != null ? members.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "\n" +getClass().getSimpleName()+
                "{" +
                "name='" + name + '\'' +
                ", parent='" + parent + '\'' +
                ", members=" + members +
                '}';
    }

}
