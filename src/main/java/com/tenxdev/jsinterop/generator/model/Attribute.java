package com.tenxdev.jsinterop.generator.model;

import com.tenxdev.jsinterop.generator.model.types.Type;

public class Attribute implements InterfaceMember {
    private final boolean static_;
    private String name;
    private Type type;
    private boolean readOnly;

    public Attribute(String name, Type type, boolean readOnly, boolean static_) {
        this.name = name;
        this.type = type;
        this.readOnly = readOnly;
        this.static_ = static_;
    }

    public boolean isStatic() {
        return static_;
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

        if (static_ != attribute.static_) return false;
        if (readOnly != attribute.readOnly) return false;
        if (!name.equals(attribute.name)) return false;
        return type.equals(attribute.type);
    }

    @Override
    public int hashCode() {
        int result = (static_ ? 1 : 0);
        result = 31 * result + name.hashCode();
        result = 31 * result + type.hashCode();
        result = 31 * result + (readOnly ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return "\n\tAttribute{" +
                "static_=" + static_ +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", readOnly=" + readOnly +
                '}';
    }
}
