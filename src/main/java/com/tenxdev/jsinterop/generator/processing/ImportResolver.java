package com.tenxdev.jsinterop.generator.processing;

import com.tenxdev.jsinterop.generator.logging.Logger;
import com.tenxdev.jsinterop.generator.model.DefinitionInfo;
import com.tenxdev.jsinterop.generator.model.Model;
import com.tenxdev.jsinterop.generator.processing.packageusage.PackageUsageModelVisitor;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ImportResolver {

    public void processModel(Model model, Logger logger) {
        logger.info(Logger.LEVEL_INFO, () -> "Resolving Java package imports");
        Map<DefinitionInfo, List<String>> packagesMap = new PackageUsageModelVisitor().accept(model);
        packagesMap.entrySet().forEach(entry -> processPackagesForDefinition(entry.getKey(), entry.getValue()));
    }

    private void processPackagesForDefinition(DefinitionInfo definitionInfo, List<String> packages) {
        definitionInfo.setImportedPackages(packages.stream()
                .filter(packageName -> needsImport(definitionInfo, packageName))
                .collect(Collectors.toList()));
    }

    private boolean needsImport(DefinitionInfo definitionInfo, String packageName) {
        return packageName != null && !definitionInfo.getPackageName().equals(packageName);
    }
}
