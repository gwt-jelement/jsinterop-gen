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

import com.tenxdev.jsinterop.generator.model.Method;
import com.tenxdev.jsinterop.generator.model.MethodArgument;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

class MethodVisitor {

    private final PackageUsageTypeVisitor typeVisitor = new PackageUsageTypeVisitor();

    public List<String> accept(Method method) {
        ArrayList<String> packages = method.getArguments().stream()
                .map(this::visitMethodArgument)
                .flatMap(List::stream)
                .distinct()
                .collect(Collectors.toCollection(ArrayList::new));
        packages.addAll(typeVisitor.accept(method.getReturnType()));
        return packages.stream()
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }

    private List<String> visitMethodArgument(MethodArgument methodArgument) {
        return typeVisitor.accept(methodArgument.getType());
    }

}
