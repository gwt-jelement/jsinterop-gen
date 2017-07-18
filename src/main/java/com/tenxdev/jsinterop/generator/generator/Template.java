package com.tenxdev.jsinterop.generator.generator;

import com.tenxdev.jsinterop.generator.model.Method;

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

    protected String enumValueToJavaName(String value) {
        String result = value;
        if (value != null && value.startsWith("\"") && value.endsWith("\"")) {
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
