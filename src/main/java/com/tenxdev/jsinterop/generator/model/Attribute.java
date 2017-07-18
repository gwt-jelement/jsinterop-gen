package com.tenxdev.jsinterop.generator.model;

import com.tenxdev.jsinterop.generator.model.types.Type;

public class Attribute implements InterfaceMember {
    private final boolean staticAttribute;
    private final String name;
    private final Type type;
    private final boolean readOnly;

    public Attribute(String name, Type type, boolean readOnly, boolean staticAttribute) {
        this.name = name;
        this.type = type;
        this.readOnly = readOnly;
        this.staticAttribute = staticAttribute;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Attribute attribute = (Attribute) o;

        if (staticAttribute != attribute.staticAttribute) return false;
        if (readOnly != attribute.readOnly) return false;
        if (!name.equals(attribute.name)) return false;
        return type.equals(attribute.type);
    }

    @Override
    public int hashCode() {
        int result = (staticAttribute ? 1 : 0);
        result = 31 * result + name.hashCode();
        result = 31 * result + type.hashCode();
        result = 31 * result + (readOnly ? 1 : 0);
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
