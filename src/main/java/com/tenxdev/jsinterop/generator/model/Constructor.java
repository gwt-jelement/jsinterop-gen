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

import com.tenxdev.jsinterop.generator.model.types.Type;

import java.util.ArrayList;
import java.util.List;

public class Constructor extends Method {

    private final List<MethodArgument> superArguments;

    public Constructor(String name, Type returnType, List<MethodArgument> arguments) {
        super(name, returnType, arguments, false, null, new ExtendedAttributes(null), null);
        this.superArguments=new ArrayList<>();
    }

    protected Constructor(String name, Type returnType, List<MethodArgument> arguments, boolean staticMethod,
                          String genericTypeSpecifiers, boolean deprecated, boolean enumReturnType,
                          Method enumOverlay,
                          String javaName, List<MethodArgument> superArguments){
        super(name, returnType, arguments, false, null, deprecated, false, null, null, null);
        this.superArguments = superArguments;
    }

    protected Constructor(Method method) {
        super(method);
        this.superArguments = new ArrayList<>();
    }

    public Constructor constructorWithSuperArguments(List<MethodArgument> superArguments) {
        return new Constructor(name, returnType, arguments, false, null,
                deprecated, false, null, null, superArguments);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends Method> T newMethodWithArguments(List<MethodArgument> newArguments) {
        return (T) new Constructor(name, returnType, newArguments, false, null,
                deprecated, false, null, null, superArguments);
    }

    public List<MethodArgument> getSuperArguments() {
        return superArguments;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Constructor that = (Constructor) o;

        return superArguments != null ? superArguments.equals(that.superArguments) : that.superArguments == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (superArguments != null ? superArguments.hashCode() : 0);
        return result;
    }
}
