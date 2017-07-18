package com.tenxdev.jsinterop.generator.model.types;

import java.util.List;
import java.util.stream.Collectors;

public class ParameterisedType implements Type {

    private final List<Type> typeParameters;
    private final Type baseType;

    public ParameterisedType(Type baseType, List<Type> typeParameters) {
        this.baseType = baseType;
        this.typeParameters = typeParameters;
    }

    public List<Type> getTypeParameters() {
        return typeParameters;
    }

    public Type getBaseType() {
        return baseType;
    }

    @Override
    public String displayValue() {
        return baseType.displayValue() + (typeParameters.isEmpty() ? "" : (
                "<" + typeParameters.stream()
                        .map(Type::displayValue)
                        .collect(Collectors.joining(", "))
                        + ">"));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ParameterisedType that = (ParameterisedType) o;

        if (!typeParameters.equals(that.typeParameters)) return false;
        return baseType.equals(that.baseType);
    }

    @Override
    public int hashCode() {
        int result = typeParameters.hashCode();
        result = 31 * result + baseType.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "ParameterisedType{" +
                "typeParameters=" + typeParameters +
                ", baseType=" + baseType +
                '}';
    }
}
