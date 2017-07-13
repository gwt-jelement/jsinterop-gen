package com.tenxdev.jsinterop.generator.model;

import java.util.List;

public class Method implements InterfaceMember {
    private final boolean static_;
    private String name;
    private String returnType;
    private List<MethodArgument> arguments;

    public Method(String name, String returnType, List<MethodArgument> arguments, boolean static_) {
        this.name = name;
        this.returnType = returnType;
        this.arguments = arguments;
        this.static_ = static_;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Method method = (Method) o;

        if (static_ != method.static_) return false;
        if (name != null ? !name.equals(method.name) : method.name != null) return false;
        if (returnType != null ? !returnType.equals(method.returnType) : method.returnType != null) return false;
        return arguments != null ? arguments.equals(method.arguments) : method.arguments == null;
    }

    @Override
    public int hashCode() {
        int result = (static_ ? 1 : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (returnType != null ? returnType.hashCode() : 0);
        result = 31 * result + (arguments != null ? arguments.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "\n\tMethod{" +
                "static_=" + static_ +
                ", name='" + name + '\'' +
                ", returnType='" + returnType + '\'' +
                ", arguments=" + arguments +
                '}';
    }

    public boolean isStatic() {
        return static_;
    }

    public String getName() {
        return name;
    }

    public String getReturnType() {
        return returnType;
    }

    public List<MethodArgument> getArguments() {
        return arguments;
    }
}
