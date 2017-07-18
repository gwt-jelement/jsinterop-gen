package com.tenxdev.jsinterop.generator.model;

public class ImplementsDefinition implements Definition {

    private final String name;
    private final String parent;

    public ImplementsDefinition(String name, String parent) {
        this.name = name;
        this.parent = parent;
    }

    @Override
    public String getName() {
        return name;
    }

    public String getParent() {
        return parent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ImplementsDefinition that = (ImplementsDefinition) o;

        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        return parent != null ? parent.equals(that.parent) : that.parent == null;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (parent != null ? parent.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "\nImplementsDefinition{" +
                "name='" + name + '\'' +
                ", parent='" + parent + '\'' +
                '}';
    }
}
