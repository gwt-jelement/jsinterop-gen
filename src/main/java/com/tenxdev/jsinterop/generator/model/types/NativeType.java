package com.tenxdev.jsinterop.generator.model.types;

public class NativeType implements Type {

    private final String typeName;

    public NativeType(String typeName) {
        this.typeName = typeName;
    }

    public String getTypeName() {
        return typeName;
    }

    @Override
    public String displayValue() {
        return typeName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NativeType that = (NativeType) o;

        return typeName != null ? typeName.equals(that.typeName) : that.typeName == null;
    }

    @Override
    public int hashCode() {
        return typeName != null ? typeName.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "NativeType{" +
                "typeName='" + typeName + '\'' +
                '}';
    }
}
