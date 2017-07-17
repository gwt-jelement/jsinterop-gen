package com.tenxdev.jsinterop.generator.model.types;

import java.util.List;
import java.util.stream.Collectors;

public class UnionType implements Type{

    private final List<Type> types;

    public UnionType(List<Type> types) {
        this.types = types;
    }

    public List<Type> getTypes() {
        return types;
    }

    @Override
    public String displayValue() {
        return types.stream()
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
                '}';
    }
}
