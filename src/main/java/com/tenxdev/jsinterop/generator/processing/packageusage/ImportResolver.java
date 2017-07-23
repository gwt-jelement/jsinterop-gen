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

package com.tenxdev.jsinterop.generator.processing.packageusage;

import com.tenxdev.jsinterop.generator.logging.Logger;
import com.tenxdev.jsinterop.generator.model.AbstractDefinition;
import com.tenxdev.jsinterop.generator.model.Model;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ImportResolver {

    public void processModel(Model model, Logger logger) {
        logger.info(Logger.LEVEL_INFO, () -> "Resolving Java package imports");
        Map<AbstractDefinition, List<String>> packagesMap = new PackageUsageModelVisitor().accept(model);
        packagesMap.entrySet().forEach(entry -> processPackagesForDefinition(entry.getKey(), entry.getValue()));
    }

    private void processPackagesForDefinition(AbstractDefinition definition, List<String> packages) {
        definition.setImportedPackages(packages.stream()
                .filter(packageName -> needsImport(definition, packageName))
                .distinct()
                .sorted()
                .collect(Collectors.toList()));
    }

    private boolean needsImport(AbstractDefinition definition, String packageName) {
        return packageName != null && !definition.getPackageName().equals(packageName);
    }
}
