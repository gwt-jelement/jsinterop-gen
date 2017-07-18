package com.tenxdev.jsinterop.generator.model;

import com.tenxdev.jsinterop.generator.model.types.Type;

public class TypeDefinition implements Definition {
    private final String name;
    private final Type type;

    public TypeDefinition(String name, Type type) {
        this.name = name;
        this.type = type;
    }

    public Type getType() {
        return type;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TypeDefinition that = (TypeDefinition) o;

        if (!name.equals(that.name)) return false;
        // Probably incorrect - comparing Object[] arrays with Arrays.equals
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
        return "\nTypeDefinition{" +
                "name='" + name + '\'' +
                ", types=" + type +
                '}';
    }
}
