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
package com.tenxdev.jsinterop.generator.generator;

import com.tenxdev.jsinterop.generator.model.Method;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

abstract class Template {

    private static final Set<String> JAVA_RESERVED_KEYWORDS = new TreeSet<>(
            Arrays.asList("abstract", "continue", "for", "new", "switch",
                    "assert", "default", "goto", "package", "synchronized",
                    "boolean	", "do	", "if	", "private	", "this",
                    "break	", "double	", "implements	", "protected	", "throw",
                    "byte	", "else	", "import	", "public	", "throws",
                    "case	", "enum	", "instanceof	", "return	", "transient",
                    "catch	", "extends	", "int	", "short	", "try",
                    "char	", "final	", "interface	", "static	", "void",
                    "class	", "finally	", "long	", "strictfp", "volatile",
                    "const	", "float	", "native	", "super	", "while", "_"));

    protected String enumValueToJavaName(@Nonnull String value) {
        String result = value;
        if (value.startsWith("\"") && value.endsWith("\"")) {
            result = value.substring(1, value.length() - 1);
        }
        if (result.isEmpty()) {
            result = "NONE";
        }
        if (!Character.isAlphabetic(result.charAt(0))) {
            result = "_" + result;
        }
        return result.replace('-', '_')
                .replace('/', '_')
                .replace('+', '_');
    }

    protected String getCallbackMethodName(Method method) {
        return method.getName() == null || method.getName().isEmpty() ? "callback" : method.getName();
    }

    protected String adjustJavaName(String name) {
        return JAVA_RESERVED_KEYWORDS.contains(name) ? name + "_" : name;
    }
}
