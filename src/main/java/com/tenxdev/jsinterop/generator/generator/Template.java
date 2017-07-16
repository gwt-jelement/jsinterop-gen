package com.tenxdev.jsinterop.generator.generator;

import com.tenxdev.jsinterop.generator.model.Method;

public abstract class Template {

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

    protected String getFirstReturnType(Method method) {
        if (method.getReturnTypes() == null || method.getReturnTypes().length == 0) {
            return "void";
        } else {
            return method.getReturnTypes()[0];
        }
    }

    protected String getCallbackMethodName(Method method) {
        return method.getName() == null || method.getName().isEmpty() ? "callback" : method.getName();
    }
}
