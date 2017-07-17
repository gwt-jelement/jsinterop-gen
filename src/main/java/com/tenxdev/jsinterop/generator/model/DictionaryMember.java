package com.tenxdev.jsinterop.generator.model;

import com.tenxdev.jsinterop.generator.model.types.Type;

import java.util.Arrays;

public class DictionaryMember {

    private final String name;
    private final boolean required;
    private final String defaultValue;
    private final Type type;

    public DictionaryMember(String name, Type type, boolean required, String defaultValue) {
        this.name = name;
        this.type = type;
        this.required = required;
        this.defaultValue = defaultValue;
    }

    public boolean isRequired() {
        return required;
    }

    public Type getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DictionaryMember that = (DictionaryMember) o;

        if (required != that.required) return false;
        if (!name.equals(that.name)) return false;
        if (defaultValue != null ? !defaultValue.equals(that.defaultValue) : that.defaultValue != null) return false;
        return type.equals(that.type);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + (required ? 1 : 0);
        result = 31 * result + (defaultValue != null ? defaultValue.hashCode() : 0);
        result = 31 * result + type.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "\n\tDictionaryMember{" +
                "name='" + name + '\'' +
                ", required=" + required +
                ", defaultValue='" + defaultValue + '\'' +
                ", type=" + type.toString() +
                '}';
    }
}
