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

import java.util.Arrays;
import java.util.List;

public class ExtendedAttributes {

    public static final String GENERIC_EXTEND = "GenericExtend";
    public static final String GENERIC_RETURN = "GenericReturn";
    public static final String GENERIC_SUB = "GenericSub";
    public static final String GENERIC_PARAMETER = "GenericParameter";
    static final String NO_INTERFACE_OBJECT = "NoInterfaceObject";
    static final String JAVA_NAME = "JavaName";
    static final String JS_TYPE_NAME = "JsTypeName";
    static final String JS_PROPERTY_NAME = "JsPropertyName";
    static final String DEPRECATED = "Deprecated";
    private final List<String> attributes;

    public ExtendedAttributes(List<String> attributes) {
        this.attributes = attributes;
    }

    public boolean hasExtendedAttribute(String name) {
        return attributes != null && (attributes.contains(name)
                || attributes.stream().anyMatch(s -> s.startsWith(name + "(")));
    }

    public String extractValue(String attributeName, String defaultValue) {
        return attributes == null ? defaultValue : attributes.stream()
                .map(String::trim)
                .filter(s -> s.startsWith(attributeName + "("))
                .findFirst()
                .map(s -> s.replace(attributeName, "").replace("(", "")
                        .replace(")", "").trim())
                .orElse(defaultValue);
    }

    public String[] extractValues(String attributeName, String[] defaultValue) {
        String values = extractValue(attributeName, null);
        return values == null ?
                defaultValue :
                Arrays.stream(values.split(",")).map(String::trim).toArray(String[]::new);
    }
}
