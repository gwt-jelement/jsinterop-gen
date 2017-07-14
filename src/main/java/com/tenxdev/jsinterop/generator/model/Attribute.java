package com.tenxdev.jsinterop.generator.model;

import java.util.Arrays;

public class Attribute implements InterfaceMember {
    private final boolean static_;
    private String name;
    private String[] types;
    private boolean readOnly;

    public Attribute(String name, String[] types, boolean readOnly, boolean static_) {
        this.name = name;
        this.types = types;
        this.readOnly = readOnly;
        this.static_ = static_;
    }

    public boolean isStatic() {
        return static_;
    }

    public String getName() {
        return name;
    }

    public String[] getTypes() {
        return types;
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
        return types.equals(attribute.types);
    }

    @Override
    public int hashCode() {
        int result = (static_ ? 1 : 0);
        result = 31 * result + name.hashCode();
        result = 31 * result + types.hashCode();
        result = 31 * result + (readOnly ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return "\n\tAttribute{" +
                "static_=" + static_ +
                ", name='" + name + '\'' +
                ", types='" + Arrays.asList(types) + '\'' +
                ", readOnly=" + readOnly +
                '}';
    }
}
