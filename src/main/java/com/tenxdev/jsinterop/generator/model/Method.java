package com.tenxdev.jsinterop.generator.model;

import com.tenxdev.jsinterop.generator.processing.TypeUtil;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class Method implements InterfaceMember {
    private final boolean static_;
    private final String name;
    private final String[] returnTypes;
    private final List<MethodArgument> arguments;

    public Method(String name, String[] returnTypes, List<MethodArgument> arguments, boolean static_) {
        this.name = name;
        this.returnTypes = returnTypes;
        this.arguments = arguments;
        this.static_ = static_;
    }

    public Method(Method method) {
        this.name = method.name;
        this.returnTypes = Arrays.copyOf(method.returnTypes, method.returnTypes.length);
        this.static_ = method.static_;
        this.arguments = method.arguments.stream().map(MethodArgument::new).collect(Collectors.toList());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Method method = (Method) o;

        if (static_ != method.static_) return false;
        if (name != null ? !name.equals(method.name) : method.name != null) return false;
        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        if (!Arrays.equals(returnTypes, method.returnTypes)) return false;
        return arguments != null ? arguments.equals(method.arguments) : method.arguments == null;
    }

    @Override
    public int hashCode() {
        int result = (static_ ? 1 : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + Arrays.hashCode(returnTypes);
        result = 31 * result + (arguments != null ? arguments.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "\n\tMethod{" +
                "static_=" + static_ +
                ", name='" + name + '\'' +
                ", returnType='" + Arrays.asList(returnTypes) + '\'' +
                ", arguments=" + arguments +
                '}';
    }

    public boolean isStatic() {
        return static_;
    }

    public String getName() {
        return name;
    }

    public String[] getReturnTypes() {
        return returnTypes;
    }

    public List<MethodArgument> getArguments() {
        return arguments;
    }

    public Set<String> getTypeUsage() {
        Set<String> types = new TreeSet<>();
        if (returnTypes != null) {
            for (String returnType : returnTypes) {
                types.addAll(TypeUtil.INSTANCE.checkParameterizedTypes(returnType));
            }
        }
        arguments.forEach(methodArgument -> {
            for (String type : methodArgument.getTypes()) {
                types.addAll(TypeUtil.INSTANCE.checkParameterizedTypes(type));
            }
        });
        return types;
    }
}
