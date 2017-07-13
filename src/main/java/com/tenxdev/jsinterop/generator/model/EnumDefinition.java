package com.tenxdev.jsinterop.generator.model;

import java.util.List;

public class EnumDefinition implements Definition {

    private String name;
    private List<String> values;

    public EnumDefinition(String name, List<String> values) {
        this.name = name;
        this.values = values;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean isPartial() {
        return false;
    }

    public List<String> getValues() {
        return values;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EnumDefinition that = (EnumDefinition) o;

        if (!name.equals(that.name)) return false;
        return values.equals(that.values);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + values.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "\nEnumDefinition{" +
                "name='" + name + '\'' +
                ", values=" + values +
                '}';
    }
}
