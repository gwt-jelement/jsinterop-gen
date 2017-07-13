package com.tenxdev.jsinterop.generator.model;

import java.util.Arrays;

public class MethodArgument {
    private final String defaultValue;
    private final String[] types;
    private final String name;
    private final boolean vararg;
    private final boolean optional;

    public MethodArgument(String name, String type, boolean vararg, boolean optional, String defaultValue) {
        this(name, new String[]{type}, vararg, optional, defaultValue);
    }

    public MethodArgument(String name, String[] types, boolean vararg, boolean optional, String defaultValue) {
        this.types = types;
        this.name = name;
        this.vararg = vararg;
        this.optional = optional;
        this.defaultValue=defaultValue;
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

    public String[] getTypes() {
        return types;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MethodArgument that = (MethodArgument) o;

        if (vararg != that.vararg) return false;
        if (optional != that.optional) return false;
        if (defaultValue != null ? !defaultValue.equals(that.defaultValue) : that.defaultValue != null) return false;
        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        if (!Arrays.equals(types, that.types)) return false;
        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        int result = defaultValue != null ? defaultValue.hashCode() : 0;
        result = 31 * result + Arrays.hashCode(types);
        result = 31 * result + name.hashCode();
        result = 31 * result + (vararg ? 1 : 0);
        result = 31 * result + (optional ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return "\nMethodArgument{" +
                "defaultValue='" + defaultValue + '\'' +
                ", types=" + Arrays.toString(types) +
                ", name='" + name + '\'' +
                ", vararg=" + vararg +
                ", optional=" + optional +
                '}';
    }
}
