package com.tenxdev.jsinterop.generator.processing;

import com.tenxdev.jsinterop.generator.model.DefinitionInfo;
import com.tenxdev.jsinterop.generator.errors.ErrorReporter;
import com.tenxdev.jsinterop.generator.model.Model;

import java.util.*;
import java.util.stream.Collectors;

public class ImportResolver {

    private static final Set<String> BUILTIN_TYPES =
            new TreeSet<>(Arrays.asList(
                    "void", "DOMString", "USVString", "ByteString", "sequence", "any",
                    "boolean", "byte", "int", "long", "unsignedlong", "unsignedlonglong",
                    "unsignedshort", "double", "unrestricteddouble", "Promise",
                    "bool", "float", "Dictionary", "longlong", "octet", "short", "FrozenArray",
                    "Function", "unrestrictedfloat","Date", "object", "SerializedScriptValue" /*=any*/,
                    "EnforceRangeunsignedlong","record"
            ));

    private final ErrorReporter errorReporter;
    private final Model model;

    public ImportResolver(Model model, ErrorReporter errorReporter) {
        this.model = model;
        this.errorReporter = errorReporter;
    }

    public void processModel() {
        model.getDefinitions().forEach(definitionInfo -> {
            Set<String> types = definitionInfo.getDefinition().getTypeUsage();
            List<String> importtedPackages = types.stream()
                    .map(type->TypeUtil.INSTANCE.removeArrayIndicator(type))
                    .filter(this::isExternalType)
                    .map(this::getPackageSuffix)
                    .filter(packageName -> needsImport(definitionInfo, packageName))
                    .distinct()
                    .sorted()
                    .collect(Collectors.toList());
            definitionInfo.setImportedPackages(importtedPackages);
        });
    }

    private boolean isExternalType(String type) {
        return !BUILTIN_TYPES.contains(type);
    }

    private String getPackageSuffix(String typeName) {
        DefinitionInfo definitionInfo = model.getDefinitionInfo(typeName);
        if (definitionInfo == null && !typeName.isEmpty()) {
            if (typeName.endsWith("Constructor")){
                return getPackageSuffix(typeName.replace("Constructor",""));
            }
            errorReporter.formatError("Unknown type -%s-%n", typeName);
        }
        return definitionInfo == null ? null : definitionInfo.getPackgeName();
    }

    private boolean needsImport(DefinitionInfo definitionInfo, String packageName) {
        return packageName != null && !definitionInfo.getPackgeName().equals(packageName);
    }
}
