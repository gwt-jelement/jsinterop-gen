package com.tenxdev.jsinterop.generator.processing;

import com.tenxdev.jsinterop.generator.errors.ErrorReporter;
import com.tenxdev.jsinterop.generator.model.DefinitionInfo;
import com.tenxdev.jsinterop.generator.model.Model;
import com.tenxdev.jsinterop.generator.model.TypeDefinition;
import jdk.nashorn.internal.codegen.TypeMap;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class ImportResolver {
    private final ErrorReporter errorReporter;
    private final Model model;

    public ImportResolver(Model model, ErrorReporter errorReporter) {
        this.model = model;
        this.errorReporter = errorReporter;
    }

    public void processModel(TypeMapper typeMapper) {
        model.getDefinitions().forEach(definitionInfo -> {
            Set<String> types = definitionInfo.getDefinition().getTypeUsage();
            List<String> importtedPackages = types.stream()
                    .map(type -> TypeUtil.INSTANCE.removeArrayIndicator(type))
                    .filter(typeMapper::needsPackageResolution)
                    .map(typeMapper::getPackageSuffix)
                    .filter(packageName -> needsImport(definitionInfo, packageName))
                    .distinct()
                    .sorted()
                    .collect(Collectors.toList());
            definitionInfo.setImportedPackages(importtedPackages);
        });
    }

    private boolean needsImport(DefinitionInfo definitionInfo, String packageName) {
        return packageName != null && !definitionInfo.getPackgeName().equals(packageName);
    }
}
