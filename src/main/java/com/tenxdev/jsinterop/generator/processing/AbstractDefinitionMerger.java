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

import com.tenxdev.jsinterop.generator.logging.Logger;
import com.tenxdev.jsinterop.generator.model.AbstractDefinition;
import com.tenxdev.jsinterop.generator.model.DictionaryDefinition;
import com.tenxdev.jsinterop.generator.model.InterfaceDefinition;

import java.util.ArrayList;
import java.util.List;

abstract class AbstractDefinitionMerger {

    final Logger logger;

    AbstractDefinitionMerger(Logger logger) {
        this.logger = logger;
    }

    void mergeDictionaries(DictionaryDefinition primaryDefinition, DictionaryDefinition partialDefinition) {
        primaryDefinition.getMembers().addAll(partialDefinition.getMembers());
    }

    void mergeInterfaces(InterfaceDefinition primaryDefinition, InterfaceDefinition definition) {
        primaryDefinition.getAttributes().addAll(disjointList(primaryDefinition.getAttributes(), definition.getAttributes()));
        primaryDefinition.getMethods().addAll(disjointList(primaryDefinition.getMethods(), definition.getMethods()));
        primaryDefinition.getConstants().addAll(disjointList(primaryDefinition.getConstants(), definition.getConstants()));
        primaryDefinition.getConstructors().addAll(disjointList(primaryDefinition.getConstructors(), definition.getConstructors()));
        primaryDefinition.getFeatures().addAll(disjointList(primaryDefinition.getFeatures(), definition.getFeatures()));
    }

    void reportTypeMismatch(AbstractDefinition primaryDefinition, AbstractDefinition secondaryDefinition) {
        logger.formatError("Do not know how to merge implements of %s %s and %s %s",
                primaryDefinition.getClass().getSimpleName(), primaryDefinition.getName(),
                secondaryDefinition.getClass().getSimpleName(), secondaryDefinition.getName());
    }

    /**
     * gets all the elements in secondaryList that are not in primaryList
     *
     * @param primaryList   the first list
     * @param secondaryList the second list
     * @param <T>           the type of the lists
     * @return a list with all the elements in secondaryList that are not in primaryList
     */
    private <T> List<T> disjointList(List<T> primaryList, List<T> secondaryList) {
        List<T> result = new ArrayList<>(secondaryList);
        result.removeAll(primaryList);
        return result;
    }
}