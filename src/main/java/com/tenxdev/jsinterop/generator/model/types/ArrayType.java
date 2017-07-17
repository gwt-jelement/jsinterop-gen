package com.tenxdev.jsinterop.generator.model.types;

public class ArrayType implements Type {

    private final Type type;

    public ArrayType(Type type) {
        this.type=type;
    }

    public Type getType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ArrayType arrayType = (ArrayType) o;

        return type.equals(arrayType.type);
    }

    @Override
    public int hashCode() {
        return type.hashCode();
    }

    @Override
    public String toString() {
        return "ArrayType{" +
                "type=" + type +
                '}';
    }

    @Override
    public String displayValue() {
        return type.displayValue()+"[]";
    }
}
