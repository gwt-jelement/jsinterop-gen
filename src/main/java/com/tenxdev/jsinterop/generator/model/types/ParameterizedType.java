package com.tenxdev.jsinterop.generator.model.types;

import java.util.List;
import java.util.stream.Collectors;

public class ParameterizedType implements Type {

    private final List<Type> typeParameters;
    private final Type baseType;

    public ParameterizedType(Type baseType, List<Type> typeParameters) {
        this.baseType = baseType;
        this.typeParameters = typeParameters;
    }

    public List<Type> getTypeParameters() {
        return typeParameters;
    }

    @Override
    public String displayValue() {
        return baseType + "<" + typeParameters.stream()
                .map(Type::displayValue)
                .collect(Collectors.joining(", "))
                + ">";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ParameterizedType that = (ParameterizedType) o;

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
        return "ParameterizedType{" +
                "typeParameters=" + typeParameters +
                ", baseType=" + baseType +
                '}';
    }
}
