package com.tenxdev.jsinterop.generator.processing;

import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

public enum TypeUtil {

    INSTANCE;

    public Set<String> checkParameterizedTypes(String type) {
        Set<String> types = new TreeSet<>();
        int start = type.indexOf("<");
        if (start != -1 && type.endsWith(">")) {
            types.add(type.substring(0, start));
            String[] subTypes = TypeUtil.INSTANCE.removeOptionalIndicator(type.substring(start + 1, type.length() - 1).split(","));
            for (String subType : subTypes) {
                types.addAll(checkParameterizedTypes(subType.trim()));
            }
        } else {
            types.add(type);
        }
        return types;
    }

    public String[] removeOptionalIndicator(String[] types) {
        return types == null ? null : Arrays.stream(types).map(this::removeOptionalIndicator).toArray(String[]::new);
    }

    public String removeOptionalIndicator(String type) {
        if (type.endsWith("?")) {
            return type.substring(0, type.length() - 1);
        }
        return type;
    }

    public String removeArrayIndicator(String type) {
        if (type.endsWith("[]")){
            return type.substring(0, type.length()-2);
        }
        return type;
    }
}
