package com.tenxdev.jsinterop.generator.processing;

import com.google.common.collect.ImmutableMap;
import com.tenxdev.jsinterop.generator.errors.ErrorReporter;
import com.tenxdev.jsinterop.generator.model.types.NativeType;
import com.tenxdev.jsinterop.generator.model.types.ObjectType;
import com.tenxdev.jsinterop.generator.model.types.Type;
import com.tenxdev.jsinterop.generator.model.types.UnionType;
import com.tenxdev.jsinterop.generator.parsing.visitors.types.TypeParser;
import com.tenxdev.jsinterop.generator.processing.DeferredTypeAdjuster;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class TypeFactory {

    private static ImmutableMap<String, Type> BOXED_TYPES = ImmutableMap.<String, Type>builder()
            .put("void", new NativeType("Void"))
            .put("int", new NativeType("Integer"))
            .put("long", new NativeType("Long"))
            .put("float", new NativeType("Float"))
            .put("double", new NativeType("Double"))
            .put("byte", new NativeType("Byte"))
            .put("boolean", new NativeType("Boolean"))
            .put("char", new NativeType("Character"))
            .build();
    private final ErrorReporter errorReporter;
    private final TypeParser typeParser;
    private Map<String, Type> typeMap = new HashMap<>();
    private Map<String, Type> deferredTypeDefs = new HashMap<>();

    public TypeFactory(ErrorReporter errorReporter) {
        this.errorReporter = errorReporter;
        this.typeParser = new TypeParser(this, errorReporter);

        typeMap.put("bool", new NativeType("boolean"));
        typeMap.put("boolean", new NativeType("boolean"));
        typeMap.put("int", new NativeType("int"));
        typeMap.put("byte", new NativeType("byte"));
        typeMap.put("octet", new NativeType("byte"));
        typeMap.put("any", new ObjectType("Any", "jsinterop.base"));
        typeMap.put("SerializedScriptValue", new NativeType("Object"));
        typeMap.put("object", new NativeType("Object"));
        typeMap.put("void", new NativeType("void"));
        typeMap.put("unrestricteddouble", new NativeType("double"));
        typeMap.put("double", new NativeType("double"));
        typeMap.put("unrestrictedfloat", new NativeType("float"));
        typeMap.put("float", new NativeType("float"));
        typeMap.put("unsignedlong", new NativeType("long"));
        typeMap.put("unsignedlonglong", new NativeType("long"));
        typeMap.put("EnforceRangeunsignedlong", new NativeType("long"));
        typeMap.put("long", new NativeType("long"));
        typeMap.put("longlong", new NativeType("long"));
        typeMap.put("unsignedshort", new NativeType("short"));
        typeMap.put("short", new NativeType("short"));
        typeMap.put("DOMString", new NativeType("String"));
        typeMap.put("USVString", new NativeType("String"));
        typeMap.put("ByteString", new NativeType("String"));
        typeMap.put("Date", new ObjectType("Date", "java.util"));
        typeMap.put("Function", new ObjectType("Function", "elemental2.core"));
        typeMap.put("Promise", new ObjectType("Promise", "elemental2.promise"));
        typeMap.put("Dictionary", new NativeType("Object"));
        typeMap.put("record", new NativeType("Object"));
    }


    public Type getTypeNoParse(String typeName) {
        if (typeName.endsWith("?")) {
            typeName = typeName.substring(0, typeName.length() - 1);
        }
        Type type = typeMap.get(typeName);
        if (type != null) {
            return type;
        }
        errorReporter.reportError("Assumed type of " + typeName);
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
        return typeParser.parseType(typeName);
    }


    public Type boxType(Type type) {
        if (type instanceof NativeType) {
            Type boxedType = BOXED_TYPES.get(((NativeType) type).getTypeName());
            return boxedType != null ? boxedType : type;
        }
        //TODO may neet to box other types
        return type;
    }

    public Type getUnionType(String[] typeNames) {
        return new UnionType(null, Arrays.stream(typeNames)
                .map(this::getType)
                .collect(Collectors.toList()));
    }

    public void registerType(String name, Type type) {
        typeMap.put(name, type);
    }

    public void registerTypeDef(String name, Type type) {
        deferredTypeDefs.put(name, type);
    }

    void registerTypeDefs() {
        DeferredTypeAdjuster adjuster = new DeferredTypeAdjuster(this);
        deferredTypeDefs.forEach((name, type) -> {
            Type adjustedType = adjuster.accept(type);
            if (adjustedType instanceof UnionType){
                ((UnionType)adjustedType).setName(name);
            }
            registerType(name, adjustedType);
        });
    }
}
