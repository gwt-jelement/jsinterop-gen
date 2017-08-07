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

public class CallbackDefinition extends AbstractDefinition {
    private final Method method;
    private final String[] genericParameters;

    public CallbackDefinition(String name, Method method, ExtendedAttributes extendedAttributes) {
        super(name);
        this.method = method;
        this.genericParameters = extendedAttributes.extractValues(ExtendedAttributes.GENERIC_PARAMETER, null);
    }

    public Method getMethod() {
        return method;
    }

    public String[] getGenericParameters() {
        return genericParameters;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CallbackDefinition that = (CallbackDefinition) o;
        return (getName() != null ? getName().equals(that.getName()) : that.getName() == null)
                && (method != null ? method.equals(that.method) : that.method == null);
    }

    @Override
    public int hashCode() {
        int result = getName() != null ? getName().hashCode() : 0;
        result = 31 * result + (method != null ? method.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "\nCallbackDefinition{" +
                "name='" + getName() + '\'' +
                ", method=" + method +
                '}';
    }

}
