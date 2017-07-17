package com.tenxdev.jsinterop.generator.processing;

import com.google.common.collect.ImmutableMap;
import com.tenxdev.jsinterop.generator.errors.ErrorReporter;
import com.tenxdev.jsinterop.generator.model.DefinitionInfo;
import com.tenxdev.jsinterop.generator.model.Model;
import com.tenxdev.jsinterop.generator.model.TypeDefinition;
import com.tenxdev.jsinterop.generator.model.types.Type;

import java.util.*;

public class TypeMapper {

    private static final Set<String> TYPES_THAT_DO_NOT_NEED_PACKAGE_RESOLTION =
            new TreeSet<>(Arrays.asList(
                    "void", "DOMString", "USVString", "ByteString", "sequence", "any",
                    "boolean", "byte", "int", "long", "unsignedlong", "unsignedlonglong",
                    "unsignedshort", "double", "unrestricteddouble",
                    "bool", "float", "Dictionary", "longlong", "octet", "short", "FrozenArray",
                    "unrestrictedfloat", "Date", "object", "SerializedScriptValue" /*=any*/,
                    "EnforceRangeunsignedlong", "record"
            ));

    private static ImmutableMap<String, String> PACKAGE_MAPPINGS = ImmutableMap.<String, String>builder()
            .put("Date", "java.util")
            .put("Function", ".ecmascript")
            .put("Promise", ".ecmascript")
            .build();

    private static Map<String, String> TYPE_MAPPINGS = new HashMap<>(ImmutableMap.<String, String>builder()
            .build());

    private static ImmutableMap<String, String> BOXED_TYPES = ImmutableMap.<String, String>builder()
            .put("void", "Void")
            .put("int", "Integer")
            .put("long", "Long")
            .put("float", "Float")
            .put("double", "Double")
            .put("byte", "Byte")
            .put("boolean", "Boolean")
            .put("char", "Character")
            .build();


    private final Model model;
    private final ErrorReporter errorReporter;
    private Map<String, String[]> typeDefinitions = new HashMap<>();

    public TypeMapper(Model model, ErrorReporter errorReporter) {
        this.model = model;
        this.errorReporter = errorReporter;
    }

    public String mapType(String idlType) {
        if (isParameterizedType(idlType)) {
            return mapParameterizedType(idlType);
        }
        String nativeType = TYPE_MAPPINGS.get(idlType);
        if (nativeType != null) {
            return nativeType;
        }
        DefinitionInfo modelType = model.getDefinitionInfo(idlType);
        if (modelType != null) {
            return idlType;
        }
        errorReporter.formatError("Type mapper: unknown type %s%n", idlType);
        return "Object";
    }

    private String mapParameterizedType(String idlType) {
        String baseType = extractBaseType(idlType);
        String[] parameters = extractParameters(idlType);
        switch (baseType) {
            case "sequence":
            case "FrozenArray":
                return mapParameterizedSequence(idlType, baseType, parameters);
            case "Promise":
                return mapParameterizedSingle(idlType, "Promise", parameters);
            case "record":
            case "Dictionary":
                return mapParameterizedDouble(idlType, "Dictionary", parameters);
            default:
                errorReporter.formatError("Type mapper: Unknown parameterized type %s%n", idlType);
                return "Object";
        }
    }

    private String mapParameterizedSingle(String idlType, String baseType, String[] parameters) {
        if (parameters.length != 1) {
            errorReporter.formatError("Type mapper: Unexpected number of parameters (%s) in %s%n",
                    parameters.length, idlType);
            return "Object";
        }
        return String.format("%s<%s>", baseType, parameters[0]);
    }

    private String mapParameterizedDouble(String idlType, String baseType, String[] parameters) {
        if (parameters.length != 2) {
            errorReporter.formatError("Type mapper: Unexpected number of parameters (%s) in %s%n",
                    parameters.length, idlType);
            return "Object";
        }
        return String.format("%s<%s, %s>", baseType, parameters[0], parameters[1]);
    }

    private String mapParameterizedSequence(String idlType, String baseType, String[] parameters) {
        if (parameters.length != 1) {
            errorReporter.formatError("Type mapper: Unexpected number of parameters (%s) in %s%n",
                    parameters.length, idlType);
            return "Object";
        }
        return parameters[0] + "[]";
    }

    private String[] extractParameters(String idlType) {
        int index = idlType.indexOf("<");
        if (index != -1) {
            return Arrays.stream(idlType.substring(index + 1, idlType.length() - 1).split(","))
                    .map(String::trim)
                    .map(type -> TypeUtil.INSTANCE.removeOptionalIndicator(type))
                    .map(this::mapType)
                    .map(this::boxAsNeeded)
                    .toArray(String[]::new);
        }
        return new String[0];
    }

    private String boxAsNeeded(String javaType) {
        String boxedType = BOXED_TYPES.get(javaType);
        return boxedType == null ? javaType : boxedType;
    }

    private String extractBaseType(String idlType) {
        int index = idlType.indexOf("<");
        return index == -1 ? idlType : idlType.substring(0, index);
    }

    private boolean isParameterizedType(String type) {
        return type.contains("<") && type.endsWith(">");
    }

    public String getPackageSuffix(String typeName) {
        String packageSuffix = PACKAGE_MAPPINGS.get(typeName);
        if (packageSuffix != null) {
            return packageSuffix + "." + typeName;
        }
        DefinitionInfo definitionInfo = model.getDefinitionInfo(typeName);
        if (definitionInfo == null && !typeName.isEmpty()) {
            if (typeName.endsWith("Constructor")) {
                return getPackageSuffix(typeName.replace("Constructor", ""));
            }
            errorReporter.formatError("Unknown type -%s-%n", typeName);
        }
        return definitionInfo == null || definitionInfo.getDefinition() instanceof TypeDefinition ?
                null : definitionInfo.getPackgeName() + "." + typeName;

    }

    public boolean needsPackageResolution(String type) {
        return !TYPES_THAT_DO_NOT_NEED_PACKAGE_RESOLTION.contains(type);
    }

    public void addTypeDefinitions(Map<String, Type> typesMap) {
    }

    /**
     * check if the given type is overriden by a typedef
     *
     * @param type the type to check
     * @return the efftive types, which may be the typedef'ed type(s) if any
     */
    public List<String> getEffectiveTypes(String type) {
        if (isParameterizedType(type)){
            String baseType=extractBaseType(type);
            String[] parameters = extractParameters(type);
            List<String> result=new ArrayList<>();
            getEffectiveTypes(new ArrayList<>(Arrays.asList(parameters)), baseType+"<", result);
            return result;
        }
        String[] effectiveTypes = typeDefinitions.get(type);
        return effectiveTypes == null ? Arrays.asList(type) : Arrays.asList(effectiveTypes);
    }

    private void getEffectiveTypes(List<String> parameterTypes, String prefix, List<String> result){
        if (parameterTypes.isEmpty()) {
            result.add(prefix.substring(0, prefix.length() - 1) + ">");
        }else {
            List<String> effectiveTypes = getEffectiveTypes(parameterTypes.remove(0));
            for (String effectiveType : effectiveTypes) {
                getEffectiveTypes(parameterTypes, prefix + effectiveType + ",", result);
            }
        }
    }



}
