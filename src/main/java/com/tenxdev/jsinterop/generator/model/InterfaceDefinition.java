package com.tenxdev.jsinterop.generator.model;

import com.tenxdev.jsinterop.generator.processing.TypeUtil;

import java.util.*;
import java.util.stream.Collectors;

public class InterfaceDefinition implements Definition {
    private final String name;
    private final String parent;
    private List<Constant> constants;
    private List<Feature> features;
    private List<Method> constructors;
    private List<Method> methods;
    private List<Attribute> attributes;
    private boolean partial;
    private List<Method> expandedMethods;
    private List<Method> expandedConstructors;

    public InterfaceDefinition(String name, String parent, List<Method> constructors, List<InterfaceMember> members) {
        this.name = name;
        this.parent = parent;
        this.constructors = constructors;
        this.methods = members.stream().filter(member -> member instanceof Method)
                .map(member -> (Method) member).collect(Collectors.toList());
        this.attributes = members.stream().filter(member -> member instanceof Attribute)
                .map(member -> (Attribute) member).collect(Collectors.toList());
        this.constants = members.stream().filter(member -> member instanceof Constant)
                .map(member -> (Constant) member).collect(Collectors.toList());
        this.features = members.stream().filter(member -> member instanceof Feature)
                .map(member -> (Feature) member).collect(Collectors.toList());
    }

    /**
     * checks for exact match in class, will return false for descendant types
     *
     * @param definition
     * @return
     */
    public static boolean is(Definition definition) {
        return definition.getClass().equals(InterfaceDefinition.class);
    }

    public List<Method> getExpandedMethods() {
        return expandedMethods;
    }

    public void setExpandedMethods(List<Method> expandedMethods) {
        this.expandedMethods = expandedMethods;
    }

    public List<Method> getExpandedConstructors() {
        return expandedConstructors;
    }

    public void setExpandedConstructors(List<Method> expandedConstructors) {
        this.expandedConstructors = expandedConstructors;
    }

    @Override
    public boolean isPartial() {
        return partial;
    }

    public void setPartial(boolean partial) {
        this.partial = partial;
    }

    @Override
    public Set<String> getTypeUsage() {
        Set<String> types = new TreeSet<>();
        if (parent != null) {
            types.addAll(TypeUtil.INSTANCE.checkParameterizedTypes(parent));
        }
        for (Method method : methods) {
            types.addAll(method.getTypeUsage());
        }
        for (Method constructor : constructors) {
            types.addAll(constructor.getTypeUsage());
        }
        for (Constant constant : constants) {
            if (constant.getType() != null && !constant.getType().isEmpty()) {
                types.addAll(TypeUtil.INSTANCE.checkParameterizedTypes(constant.getType()));
            }
        }
        for (Attribute attribute : attributes) {
            for (String type : attribute.getTypes()) {
                types.addAll(TypeUtil.INSTANCE.checkParameterizedTypes(type));
            }
        }
        return types;
    }

    @Override
    public String getName() {
        return name;
    }

    public String getParent() {
        return parent;
    }

    public List<Method> getMethods() {
        if (methods == null) {
            if (isPartial()) {
                return Collections.emptyList();
            }
            methods = new ArrayList<>();
        }
        return methods;
    }

    public List<Attribute> getAttributes() {
        if (attributes == null) {
            if (isPartial()) {
                return Collections.emptyList();
            }
            attributes = new ArrayList<>();
        }
        return attributes;
    }

    public boolean hasStaticMethods() {
        return methods.stream().anyMatch(Method::isStatic);
    }

    public List<Method> getConstructors() {
        if (constructors == null) {
            if (isPartial()) {
                return Collections.emptyList();
            }
            constructors = new ArrayList<>();
        }
        return constructors;
    }

    public List<Constant> getConstants() {
        if (constants == null) {
            if (isPartial()) {
                return Collections.emptyList();
            }
            constants = new ArrayList<>();
        }
        return constants;
    }

    public List<Feature> getFeatures() {
        if (features == null) {
            if (isPartial()) {
                return Collections.emptyList();
            }
            features = new ArrayList<>();
        }

        return features;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        InterfaceDefinition that = (InterfaceDefinition) o;

        if (partial != that.partial) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (parent != null ? !parent.equals(that.parent) : that.parent != null) return false;
        if (constants != null ? !constants.equals(that.constants) : that.constants != null) return false;
        if (features != null ? !features.equals(that.features) : that.features != null) return false;
        if (constructors != null ? !constructors.equals(that.constructors) : that.constructors != null) return false;
        if (methods != null ? !methods.equals(that.methods) : that.methods != null) return false;
        return attributes != null ? attributes.equals(that.attributes) : that.attributes == null;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (parent != null ? parent.hashCode() : 0);
        result = 31 * result + (constants != null ? constants.hashCode() : 0);
        result = 31 * result + (features != null ? features.hashCode() : 0);
        result = 31 * result + (constructors != null ? constructors.hashCode() : 0);
        result = 31 * result + (methods != null ? methods.hashCode() : 0);
        result = 31 * result + (attributes != null ? attributes.hashCode() : 0);
        result = 31 * result + (partial ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return "\n" + (partial ? "partial " : "") +
                "InterfaceDefinition{" +
                "constructors=" + constructors +
                ", name='" + name + '\'' +
                ", parent='" + parent + '\'' +
                ", methods=" + methods +
                ", attributes=" + attributes +
                ", constants=" + constants +
                ", features=" + features +
                '}';
    }
}

