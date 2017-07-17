package com.tenxdev.jsinterop.generator.model.types;

import java.util.List;
import java.util.stream.Collectors;

public class UnionType implements Type {

    private final List<Type> types;
    private String name;

    public UnionType(String name, List<Type> types) {
        this.name = name;
        this.types = types;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Type> getTypes() {
        return types;
    }

    @Override
    public String displayValue() {
        return name != null ? name : types.stream()
                .map(Type::displayValue)
                .collect(Collectors.joining(" or ", "(", ")"));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        UnionType unionType = (UnionType) o;

        return types.equals(unionType.types);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + types.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "UnionType{" +
                "types=" + types +
                ", name='" + name + '\'' +
                '}';
    }
}
