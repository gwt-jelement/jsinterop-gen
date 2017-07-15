package com.tenxdev.jsinterop.generator.generator;

import com.google.common.collect.ImmutableMap;
import com.tenxdev.jsinterop.generator.errors.ErrorReporter;
import com.tenxdev.jsinterop.generator.model.DefinitionInfo;
import com.tenxdev.jsinterop.generator.model.Model;

import java.util.Arrays;

public class TypeMapper {

    private static ImmutableMap<String, String> PACKAGE_MAPPINGS = ImmutableMap.<String, String>builder()
            .put("Date", "java.util.Date")
            .put("Function", ".base")
            .put("Promise", ".base")
            .put("Dictionary", ".base")
            .build();

    private static ImmutableMap<String, String> TYPE_MAPPINGS = ImmutableMap.<String, String>builder()
            .put("bool", "boolean")
            .put("boolean", "boolean")
            .put("int", "int")
            .put("byte", "byte")
            .put("octet", "byte")
            .put("any", "Object")
            .put("SerializedScriptValue", "Object")
            .put("object", "Object")
            .put("void", "void")
            .put("unrestricteddouble", "double")
            .put("double", "double")
            .put("unrestrictedfloat", "float")
            .put("float", "float")
            .put("unisgnedlong", "long")
            .put("unisgnedlonglong", "long")
            .put("EnforceRangeunsignedlong", "long")
            .put("long", "long")
            .put("longlong", "long")
            .put("unisgnedshort", "short")
            .put("short", "short")
            .put("DOMString", "String")
            .put("USVString", "String")
            .put("ByteString", "String")
            .put("DOMTimeStamp", "long")
            .put("DOMHighResTimeStamp", "double")
            .put("Date", "Date")
            .put("Function", "Function")
            .put("Promise", "Promise")
            .put("Dictionary", "object")
            .put("record", "object")
            .build();

    private final Model model;
    private final ErrorReporter errorReporter;

    public TypeMapper(Model model, ErrorReporter errorReporter){
        this.model=model;
        this.errorReporter=errorReporter;
    }

    public String mapType(String idlType) {
        if (isParameterizedType(idlType)){
            return mapParameterizedType(idlType);
        }
        String nativeType = TYPE_MAPPINGS.get(idlType);
        if (nativeType!=null){
            return nativeType;
        }
        DefinitionInfo modelType = model.getDefinitionInfo(idlType);
        if (modelType!=null){
            return idlType;
        }
        errorReporter.formatError("Type mapper: unknown type %s%n", idlType);
        return "Object";
    }

    private String mapParameterizedType(String idlType) {
        String baseType=extractBaseType(idlType);
        String[] parameters=extractParameters(idlType);
        switch (baseType){
            case "sequence":
                return mapParameterizedSequence(idlType, baseType, parameters);
            case "Promise":
                return mapParameterizedSingle(idlType, "Promise", parameters);
            case "record":
            case "Dictionary":
                return mapParameterizedDouble(idlType, "Dictionary", parameters);
            default:
                errorReporter.formatError("Type mapper: Unknown parameterized type %s%n",idlType);
                return "Object";
        }
    }

    private String mapParameterizedSingle(String idlType, String baseType, String[] parameters) {
        if (parameters.length!=1){
            errorReporter.formatError("Type mapper: Unexpected number of parameters (%s) in %s%n",
                    parameters.length, idlType);
            return "Object";
        }
        return String.format("%s<%s>", baseType, parameters[0]);
    }
    private String mapParameterizedDouble(String idlType, String baseType, String[] parameters) {
        if (parameters.length!=2){
            errorReporter.formatError("Type mapper: Unexpected number of parameters (%s) in %s%n",
                    parameters.length, idlType);
            return "Object";
        }
        return String.format("%s<%s, %s>", baseType, parameters[0], parameters[1]);
    }

    private String mapParameterizedSequence(String idlType, String baseType, String[] parameters) {
        if (parameters.length!=1){
            errorReporter.formatError("Type mapper: Unexpected number of parameters (%s) in %s%n",
                    parameters.length, idlType);
            return "Object";
        }
        return parameters[0]+"[]";
    }

    private String[] extractParameters(String idlType) {
        int index=idlType.indexOf("<");
        if (index!=-1){
            return Arrays.stream(idlType.substring(index+1, idlType.length()-1).split(","))
                    .map(String::trim)
                    .map(this::mapType)
                    .toArray(String[]::new);
        }
        return new String[0];
    }

    private String extractBaseType(String idlType) {
        int index=idlType.indexOf("<");
        return index==-1?idlType:idlType.substring(0,index);
    }

    private boolean isParameterizedType(String type){
        return type.contains("<") && type.endsWith(">");
    }

}
