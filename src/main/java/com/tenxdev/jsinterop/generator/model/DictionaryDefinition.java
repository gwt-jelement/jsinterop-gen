package com.tenxdev.jsinterop.generator.model;

import com.tenxdev.jsinterop.generator.processing.TypeUtil;

import java.util.*;

public class DictionaryDefinition implements Definition {
    private final String name;
    private final String parent;
    private List<DictionaryMember> members;
    private boolean partial;

    public DictionaryDefinition(String name, String parent, List<DictionaryMember> members) {
        this.name = name;
        this.parent = parent;
        this.members = members;
    }

    public boolean getPartial() {
        return partial;
    }

    @Override
    public boolean isPartial() {
        return partial;
    }

    @Override
    public Set<String> getTypeUsage() {
        Set<String> types=new TreeSet<>();
        if (parent!=null) {
            types.addAll(TypeUtil.INSTANCE.checkParameterizedTypes(parent));
        }
        for (DictionaryMember member: members){
            for (String type: types){
                types.addAll(TypeUtil.INSTANCE.checkParameterizedTypes(type));
            }
        }
        return types;
    }

    public void setPartial(boolean partial) {
        this.partial = partial;
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
            if (isPartial()){
                return Collections.emptyList();
            }
            members=new ArrayList<>();
        }
        return members;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DictionaryDefinition that = (DictionaryDefinition) o;
        if (partial != that.partial) return false;
        if (!name.equals(that.name)) return false;
        if (parent != null ? !parent.equals(that.parent) : that.parent != null) return false;
        return members != null ? members.equals(that.members) : that.members == null;
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + (parent != null ? parent.hashCode() : 0);
        result = 31 * result + (members != null ? members.hashCode() : 0);
        result = 31 * result + (partial ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return "\n" + (partial ? "partial " : "'") +
                "DictionaryDefinition{" +
                "name='" + name + '\'' +
                ", parent='" + parent + '\'' +
                ", members=" + members +
                '}';
    }

    public static boolean is(Definition partialDefinition) {
        return partialDefinition.getClass().equals(DictionaryDefinition.class);
    }
}
