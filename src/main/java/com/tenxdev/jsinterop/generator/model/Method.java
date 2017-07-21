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
    private final boolean staticMethod;
    private final String name;
    private final List<MethodArgument> arguments;
    private Type returnType;
    private Method methodReferences;
    private boolean privateMethod;
    private Method enumOverlay;
    private boolean enumReturnType;
    private String javaName;

    public Method(String name, Type returnType, List<MethodArgument> arguments, boolean staticMethod,
                  boolean privateMethod, Method enumOverlay, String javaName) {
        this.name = name;
        this.returnType = returnType;
        this.arguments = arguments;
        this.staticMethod = staticMethod;
        this.privateMethod = privateMethod;
        this.enumOverlay = enumOverlay;
        this.javaName = javaName;
    }

    public Method(Method method) {
        this.name = method.name;
        this.returnType = method.returnType;
        this.staticMethod = method.staticMethod;
        this.arguments = method.arguments.stream().map(MethodArgument::new).collect(Collectors.toList());
        this.privateMethod = method.privateMethod;
        this.enumOverlay = method.enumOverlay;
        this.javaName = method.javaName;
    }

    public Method newMethodWithArguments(List<MethodArgument> newArguments) {
        return new Method(name, returnType, newArguments, staticMethod,
                privateMethod, enumOverlay, javaName);
    }

    public boolean isStatic() {
        return staticMethod;
    }

    public String getName() {
        return name;
    }

    public Type getReturnType() {
        return returnType == null ? new NativeType("void") : returnType;
    }

    public void setReturnType(Type returnType) {
        this.returnType = returnType;
    }

    public List<MethodArgument> getArguments() {
        return arguments;
    }

    public void setMethodReferences(Method methodReferences) {
        if (this.methodReferences == null) {
            this.methodReferences = methodReferences;
        }
    }

    @Override
    public int compareTo(@Nonnull Method o) {
        int result = name == null ? -1 : name.compareTo(o.name);
        if (result == 0) {
            result = arguments.size() - o.arguments.size();
        }
        return result;
    }

    public boolean isPrivateMethod() {
        return privateMethod;
    }

    public void setPrivate(boolean privateMethod) {
        this.privateMethod = privateMethod;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Method method = (Method) o;

        if (staticMethod != method.staticMethod) return false;
        if (name != null ? !name.equals(method.name) : method.name != null) return false;
        if (returnType != null ? !returnType.equals(method.returnType) : method.returnType != null) return false;
        if (arguments != null ? !arguments.equals(method.arguments) : method.arguments != null) return false;
        return methodReferences != null ? methodReferences.equals(method.methodReferences) : method.methodReferences == null;
    }

    @Override
    public int hashCode() {
        int result = (staticMethod ? 1 : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (returnType != null ? returnType.hashCode() : 0);
        result = 31 * result + (arguments != null ? arguments.hashCode() : 0);
        result = 31 * result + (methodReferences != null ? methodReferences.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "\n\tMethod{" +
                "staticMethod=" + staticMethod +
                ", name='" + name + '\'' +
                ", returnType='" + returnType + '\'' +
                ", arguments=" + arguments +
                (privateMethod ? ", PRIVATE" : "") +
                (enumOverlay != null ? ("EnumOverlay='" + enumOverlay + "'") : "") +
                '}';
    }
}
