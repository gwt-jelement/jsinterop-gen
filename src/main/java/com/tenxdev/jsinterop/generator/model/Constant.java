package com.tenxdev.jsinterop.generator.model;

public class Constant implements InterfaceMember {

    private String name;
    private String type;
    private String value;

    public Constant(String name, String type, String value) {
        this.name = name;
        this.type = type;
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Constant constant = (Constant) o;

        if (!name.equals(constant.name)) return false;
        if (!type.equals(constant.type)) return false;
        return value.equals(constant.value);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + type.hashCode();
        result = 31 * result + value.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "\n\tConstant{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", value='" + value + '\'' +
                '}';
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getValue() {
        return value;
    }
}
