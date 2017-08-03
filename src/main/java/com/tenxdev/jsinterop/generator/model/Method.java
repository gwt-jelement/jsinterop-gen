/*
 * Copyright 2017 Abed Tony BenBrahim <tony.benrahim@10xdev.com>
 *     and Gwt-JElement project contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.tenxdev.jsinterop.generator.model;

import com.tenxdev.jsinterop.generator.model.interfaces.InterfaceMember;
import com.tenxdev.jsinterop.generator.model.types.NativeType;
import com.tenxdev.jsinterop.generator.model.types.Type;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.stream.Collectors;

public class Method implements InterfaceMember, Comparable<Method> {
    final String name;
    final List<MethodArgument> arguments;
    private final boolean staticMethod;

    private String genericTypeSpecifiers;

    final boolean deprecated;
    private final Type replacedReturnType;
    private Method enumOverlay;
    private String javaName;
    Type returnType;
    private String body;
    private List<Type> extraTypes;
    private boolean enumReturnType;
    public Method(String name, Type returnType, List<MethodArgument> arguments, boolean staticMethod,
                  String genericTypeSpecifiers, ExtendedAttributes extendedAttributes, Type replacedReturnType) {
        this.name = name;
        this.returnType = returnType;
        this.genericTypeSpecifiers = genericTypeSpecifiers;
        this.arguments = arguments;
        this.staticMethod = staticMethod;
        this.enumOverlay = null;
        this.javaName = extendedAttributes.extractValue(ExtendedAttributes.JAVA_NAME, null);
        this.deprecated = extendedAttributes.hasExtendedAttribute(ExtendedAttributes.DEPRECATED);
        this.replacedReturnType=replacedReturnType;
    }

    Method(String name, Type returnType, List<MethodArgument> arguments, boolean staticMethod,
           String genericTypeSpecifiers, boolean deprecated, boolean enumReturnType, Method enumOverlay,
           String javaName, Type replacedReturnType) {
        this.name = name;
        this.returnType = returnType;
        this.arguments = arguments;
        this.staticMethod = staticMethod;
        this.genericTypeSpecifiers = genericTypeSpecifiers;
        this.deprecated = deprecated;
        this.enumReturnType = enumReturnType;
        this.enumOverlay = enumOverlay;
        this.javaName = javaName;
        this.replacedReturnType=replacedReturnType;
    }

    Method(Method method) {
        this.name = method.name;
        this.returnType = method.returnType;
        this.arguments = method.arguments.stream().map(MethodArgument::new).collect(Collectors.toList());
        this.staticMethod = method.staticMethod;
        this.genericTypeSpecifiers = method.genericTypeSpecifiers;
        this.deprecated = method.deprecated;
        this.enumReturnType = method.enumReturnType;
        this.enumOverlay = method.enumOverlay;
        this.javaName = method.javaName;
        this.replacedReturnType=method.replacedReturnType;
    }

    @SuppressWarnings("unchecked")
    public <T extends Method> T newMethodWithArguments(List<MethodArgument> newArguments) {
        return (T) new Method(name, returnType, newArguments, staticMethod, genericTypeSpecifiers,
                deprecated, enumReturnType, enumOverlay, javaName, replacedReturnType);
    }

    public boolean isStatic() {
        return staticMethod;
    }

    public String getName() {
        return name;
    }

    public Type getReturnType() {
        return returnType == null ?
                new NativeType("void") : returnType;
    }

    public Type getReplacedReturnType() {
        return replacedReturnType;
    }

    public void setReturnType(Type returnType) {
        this.returnType = returnType;
    }

    public List<MethodArgument> getArguments() {
        return arguments;
    }

    public String getGenericTypeSpecifiers() {
        return genericTypeSpecifiers;
    }

    public void setGenericTypeSpecifiers(String genericTypeSpecifiers) {
        this.genericTypeSpecifiers = genericTypeSpecifiers;
    }

    public boolean isDeprecated() {
        return deprecated;
    }

    @Override
    public int compareTo(@Nonnull Method o) {
        int result = name == null ? -1 : name.compareTo(o.name);
        if (result == 0) {
            result = arguments.size() - o.arguments.size();
        }
        return result;
    }

    public Method getEnumOverlay() {
        return enumOverlay;
    }

    public void setEnumOverlay(Method enumOverlay) {
        this.enumOverlay = enumOverlay;
    }

    public boolean isEnumReturnType() {
        return enumReturnType;
    }

    public void setEnumReturnType(boolean enumReturnType) {
        this.enumReturnType = enumReturnType;
    }

    public String getJavaName() {
        return javaName == null ? name : javaName;
    }

    public void setJavaName(String javaName) {
        this.javaName = javaName;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public List<Type> getExtraTypes() {
        return extraTypes;
    }

    public void setExtraTypes(List<Type> extraTypes) {
        this.extraTypes = extraTypes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Method method = (Method) o;

        if (staticMethod != method.staticMethod) return false;
        if (name != null ? !name.equals(method.name) : method.name != null) return false;
        if (arguments != null ? !arguments.equals(method.arguments) : method.arguments != null) return false;
        if (genericTypeSpecifiers != null ? !genericTypeSpecifiers.equals(method.genericTypeSpecifiers) : method.genericTypeSpecifiers != null)
            return false;
        return returnType != null ? returnType.equals(method.returnType) : method.returnType == null;
    }

    @Override
    public int hashCode() {
        int result = (staticMethod ? 1 : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (arguments != null ? arguments.hashCode() : 0);
        result = 31 * result + (genericTypeSpecifiers != null ? genericTypeSpecifiers.hashCode() : 0);
        result = 31 * result + (returnType != null ? returnType.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "\n\tMethod{" +
                "staticMethod=" + staticMethod +
                ", name='" + name + '\'' +
                ", returnType='" + returnType + '\'' +
                ", arguments=" + arguments +
                (enumOverlay != null ? ("EnumOverlay='" + enumOverlay + "'") : "") +
                '}';
    }
}
