package com.tenxdev.jsinterop.generator.model;

import com.tenxdev.jsinterop.generator.model.types.Type;

import java.util.Arrays;

public class MethodArgument {
    private final String defaultValue;
    private final Type type;
    private final String name;
    private final boolean vararg;
    private final boolean optional;

    public MethodArgument(String name, Type type, boolean vararg, boolean optional, String defaultValue) {
        this.type = type;
        this.name = name;
        this.vararg = vararg;
        this.optional = optional;
        this.defaultValue = defaultValue;
    }

    public MethodArgument(MethodArgument methodArgument) {
        this.defaultValue = methodArgument.defaultValue;
        this.type = methodArgument.type;
        this.name = methodArgument.name;
        this.vararg = methodArgument.vararg;
        this.optional = methodArgument.optional;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public boolean isVararg() {
        return vararg;
    }

    public String getName() {
        return name;
    }

    public boolean isOptional() {
        return optional;
    }

    public Type getType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MethodArgument that = (MethodArgument) o;

        if (vararg != that.vararg) return false;
        if (optional != that.optional) return false;
        if (defaultValue != null ? !defaultValue.equals(that.defaultValue) : that.defaultValue != null) return false;
        if (type != null ? !type.equals(that.type) : that.type != null) return false;
        return name != null ? name.equals(that.name) : that.name == null;
    }

    @Override
    public int hashCode() {
        int result = defaultValue != null ? defaultValue.hashCode() : 0;
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (vararg ? 1 : 0);
        result = 31 * result + (optional ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return "\nMethodArgument{" +
                "defaultValue='" + defaultValue + '\'' +
                ", type=" + type +
                ", name='" + name + '\'' +
                ", vararg=" + vararg +
                ", optional=" + optional +
                '}';
    }
}
