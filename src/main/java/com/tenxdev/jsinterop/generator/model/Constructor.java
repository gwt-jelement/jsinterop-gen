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

    List<MethodArgument> superArguments = new ArrayList<>();

    public Constructor(String name, Type returnType, List<MethodArgument> arguments, boolean staticMethod, Method enumOverlay, String javaName) {
        super(name, returnType, arguments, staticMethod, enumOverlay, javaName, null);
    }

    public Constructor(Method method) {
        super(method);
    }

    @Override
    public <T extends Method> T newMethodWithArguments(List<MethodArgument> newArguments) {
        return (T) new Constructor(getName(), getReturnType(), newArguments, isStatic(),
                getEnumOverlay(), getJavaName());
    }

    public List<MethodArgument> getSuperArguments() {
        return superArguments;
    }

    public void setSuperArguments(List<MethodArgument> superArguments) {
        this.superArguments = superArguments;
    }
}
