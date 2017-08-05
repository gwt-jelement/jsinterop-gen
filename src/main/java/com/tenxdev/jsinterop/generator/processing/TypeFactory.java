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

package com.tenxdev.jsinterop.generator.processing;

import com.google.common.collect.ImmutableMap;
import com.tenxdev.jsinterop.generator.logging.Logger;
import com.tenxdev.jsinterop.generator.model.types.NativeType;
import com.tenxdev.jsinterop.generator.model.types.ObjectType;
import com.tenxdev.jsinterop.generator.model.types.Type;
import com.tenxdev.jsinterop.generator.model.types.UnionType;
import com.tenxdev.jsinterop.generator.parsing.visitors.types.TypeParser;

import java.lang.annotation.Native;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class TypeFactory {

    public static final ImmutableMap<String, Type> BOXED_TYPES = ImmutableMap.<String, Type>builder()
            .put("void", new NativeType("Void"))
            .put("int", new NativeType("Integer"))
            .put("long", new NativeType("Long"))
            .put("float", new NativeType("Float"))
            .put("double", new NativeType("Double"))
            .put("byte", new NativeType("Byte"))
            .put("boolean", new NativeType("Boolean"))
            .put("char", new NativeType("Character"))
            .build();
    private final TypeParser typeParser;
    private final Map<String, Type> typeMap = new HashMap<>();
    private final Map<String, Type> deferredTypeDefinitions = new HashMap<>();
    private final Map<String, UnionType> anonymousUnionTypes = new HashMap<>();

    public TypeFactory(Logger logger) {
        this.typeParser = new TypeParser(this, logger);

        typeMap.put("bool", NativeType.BOOLEAN);
        typeMap.put("boolean", NativeType.BOOLEAN);
        typeMap.put("int", NativeType.INT);
        typeMap.put("byte", NativeType.BYTE);
        typeMap.put("octet", NativeType.BYTE);
        typeMap.put("unsignedshort", NativeType.SHORT);
        typeMap.put("short", NativeType.SHORT);
        typeMap.put("double", NativeType.DOUBLE);
        typeMap.put("unrestricteddouble", NativeType.DOUBLE);
        typeMap.put("unrestrictedfloat", NativeType.DOUBLE);
        typeMap.put("float", NativeType.DOUBLE);
        typeMap.put("unsignedlong", NativeType.DOUBLE);
        typeMap.put("unsignedlonglong", NativeType.DOUBLE);
        typeMap.put("EnforceRangeunsignedlong", NativeType.DOUBLE);
        typeMap.put("long", NativeType.DOUBLE);
        typeMap.put("longlong", NativeType.DOUBLE);

        typeMap.put("Byte", new NativeType("Byte"));
        typeMap.put("Integer", new NativeType("Integer"));
        typeMap.put("Short", new NativeType("Short"));
        typeMap.put("Double", new NativeType("Double"));
        typeMap.put("Float", new NativeType("Double"));
        typeMap.put("Long", new NativeType("Double"));

        typeMap.put("object", new NativeType("Object"));
        typeMap.put("void", new NativeType("void"));

        typeMap.put("USVString", new NativeType("String"));
        typeMap.put("ByteString", new NativeType("String"));
        typeMap.put("DOMString", new NativeType("String"));

        typeMap.put("any", new NativeType("Object"));

        typeMap.put("Dictionary", new ObjectType("JsObject",".core"));
        typeMap.put("record", new ObjectType("JsObject",".core"));
        typeMap.put("SerializedScriptValue", new ObjectType("JsObject",".core"));
    }

    public Type getTypeNoParse(String typeName) {
        if (typeName.endsWith("?")) {
            typeName = typeName.substring(0, typeName.length() - 1);
        }
        if (typeName.endsWith("Constructor")) {
            typeName = typeName.substring(0, typeName.length() - "Constructor".length());
        }
        Type type = typeMap.get(typeName);
        if (type != null) {
            return type;
        }
        return new NativeType(typeName);
    }

    public Type getType(String typeName) {
        if (typeName.endsWith("?")) {
            typeName = typeName.substring(0, typeName.length() - 1);
        }
        Type type = typeMap.get(typeName);
        if (type != null) {
            return type;
        }
        return typeParser.parseType(typeName.replace("?", ""));
    }

    public Type boxType(Type type) {
        if (type instanceof NativeType) {
            Type boxedType = BOXED_TYPES.get(type.getTypeName());
            return boxedType != null ? boxedType : type;
        }
        //TODO may need to box other types
        return type;
    }

    public Type getUnionType(String[] typeNames) {
        String key = String.join(",", typeNames);
        UnionType unionType = anonymousUnionTypes.computeIfAbsent(key,
                k -> new UnionType(null, Arrays.stream(typeNames)
                        .map(this::getType)
                        .collect(Collectors.toList())));
        return unionType;
    }

    public void registerType(String name, Type type) {
        typeMap.put(name, type);
    }

    public void registerTypeDef(String name, Type type) {
        deferredTypeDefinitions.put(name, type);
    }

    void fixUpDeferredTypeDefinitions() {
        DeferredTypeAdjuster adjuster = new DeferredTypeAdjuster(this);
        deferredTypeDefinitions.forEach((name, type) -> {
            Type adjustedType = adjuster.accept(type);
            if (adjustedType instanceof UnionType) {
                ((UnionType) adjustedType).setName(name);
            }
            registerType(name, adjustedType);
        });
    }

    public boolean hasType(String name) {
        return typeMap.containsKey(name);
    }
}
