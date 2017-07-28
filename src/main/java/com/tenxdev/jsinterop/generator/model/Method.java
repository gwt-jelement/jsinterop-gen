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
import com.tenxdev.jsinterop.generator.model.types.GenericTypeHandler;
import com.tenxdev.jsinterop.generator.model.types.NativeType;
import com.tenxdev.jsinterop.generator.model.types.Type;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.stream.Collectors;

public class Method implements InterfaceMember, Comparable<Method> {
    private final boolean staticMethod;
    private final String name;
    private final List<MethodArgument> arguments;
    private final boolean deprecated;
    private final boolean genericReturn;
    private final String genericTypeSpecifiers;
    private Type returnType;
    private Method methodReferences;
    private Method enumOverlay;
    private boolean enumReturnType;
    private String javaName;

    public Method(String name, Type returnType, List<MethodArgument> arguments, boolean staticMethod,
                  Method enumOverlay, String javaName, ExtendedAttributes extendedAttributes) {
        this(name, returnType, arguments, staticMethod, enumOverlay, javaName,
                extendedAttributes.hasExtendedAttribute(ExtendedAttributes.DEPRECATED),
                extendedAttributes.hasExtendedAttribute(ExtendedAttributes.GENERIC_RETURN),
                extendedAttributes.extractValue(ExtendedAttributes.GENERIC_SUB, null),
                extendedAttributes.extractValue(ExtendedAttributes.GENERIC_PARAMETER, null));
    }

    protected Method(String name, Type returnType, List<MethodArgument> arguments, boolean staticMethod,
                     Method enumOverlay, String javaName, boolean deprecated, boolean genericReturn,
                     String genericSubstitution, String genericParameter) {
        this.name = name;
        this.returnType = GenericTypeHandler.INSTANCE.getEffectiveType(returnType, genericSubstitution, genericParameter);
        this.genericTypeSpecifiers = staticMethod ?
                GenericTypeHandler.INSTANCE.getTypeSpecifiers(genericSubstitution) :
                null;
        this.arguments = arguments;
        this.staticMethod = staticMethod;
        this.enumOverlay = enumOverlay;
        this.javaName = javaName;
        this.deprecated = deprecated;
        this.genericReturn = genericReturn;
    }

    protected Method(Method method) {
        this.name = method.name;
        this.returnType = method.returnType;
        this.staticMethod = method.staticMethod;
        this.arguments = method.arguments.stream().map(MethodArgument::new).collect(Collectors.toList());
        this.enumOverlay = method.enumOverlay;
        this.javaName = method.javaName;
        this.deprecated = method.deprecated;
        this.genericReturn = method.genericReturn;
        this.genericTypeSpecifiers = method.genericTypeSpecifiers;
    }

    @SuppressWarnings("unchecked")
    public <T extends Method> T newMethodWithArguments(List<MethodArgument> newArguments) {
        return (T) new Method(name, returnType, newArguments, staticMethod, enumOverlay,
                javaName, deprecated, genericReturn, null, null);
    }

    public boolean isGenericReturn() {
        return genericReturn;
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

    public String getGenericTypeSpecifiers() {
        return genericTypeSpecifiers;
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

    public boolean hasReturnType() {
        if (returnType instanceof NativeType) {
            String returnTypeName = ((NativeType) returnType).getTypeName();
            return !"void".equals(returnTypeName);
        }
        return returnType != null;
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
                (enumOverlay != null ? ("EnumOverlay='" + enumOverlay + "'") : "") +
                '}';
    }
}
