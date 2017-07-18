package com.tenxdev.jsinterop.generator.model;

public class CallbackDefinition implements Definition {
    private String name;
    private Method method;

    public CallbackDefinition(String name, Method method) {
        this.name = name;
        this.method = method;
    }

    public Method getMethod() {
        return method;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CallbackDefinition that = (CallbackDefinition) o;

        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        return method != null ? method.equals(that.method) : that.method == null;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (method != null ? method.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "\nCallbackDefinition{" +
                "name='" + name + '\'' +
                ", method=" + method +
                '}';
    }

}
