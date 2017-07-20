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

package com.tenxdev.jsinterop.generator.processing.uniontypes;

import com.tenxdev.jsinterop.generator.logging.Logger;
import com.tenxdev.jsinterop.generator.model.DefinitionInfo;
import com.tenxdev.jsinterop.generator.model.DictionaryDefinition;
import com.tenxdev.jsinterop.generator.model.DictionaryMember;
import com.tenxdev.jsinterop.generator.model.Model;
import com.tenxdev.jsinterop.generator.model.types.UnionType;

import java.util.ArrayList;
import java.util.List;

import static org.eclipse.xtext.xbase.lib.StringExtensions.toFirstUpper;

public class DictionaryMemberUnionTypeProcessor {

    private final RemoveEnumUnionTypeVisitor removeEnumUnionTypeVisitor;
    private HasUnionTypeVisitor hasUnionTypeVisitor = new HasUnionTypeVisitor();
    private GetUnionTypesVisitor getUnionTypesVisitor = new GetUnionTypesVisitor();
    private Model model;
    private Logger logger;

    public DictionaryMemberUnionTypeProcessor(Model model, Logger logger) {
        this.model = model;
        this.logger = logger;
        this.removeEnumUnionTypeVisitor = new RemoveEnumUnionTypeVisitor(model, logger);
    }

    public void process() {
        logger.info(Logger.LEVEL_INFO, () -> "Processing union types in dictionaries");
        model.getDefinitions().stream()
                .map(DefinitionInfo::getDefinition)
                .filter(definition -> definition instanceof DictionaryDefinition)
                .map(definition -> (DictionaryDefinition) definition)
                .forEach(this::processDictionary);
    }

    private void processDictionary(DictionaryDefinition definition) {
        definition.getMembers().stream()
                .filter(member -> hasUnionTypeVisitor.accept(member.getType()))
                .forEach(member -> processMember(member, definition));
    }

    private void processMember(DictionaryMember member, DictionaryDefinition definition) {
        List<UnionType> unionTypes = getUnionTypesVisitor.accept(member.getType());
        if (unionTypes.size() == 1) {
            UnionType unionType = unionTypes.get(0);
            if (unionType.getName() == null) {
                unionType.setName(toFirstUpper(member.getName()) + "UnionType");
            }
            if (definition.getUnionReturnTypes() == null) {
                definition.setUnionReturnTypes(new ArrayList<>());
            }

            UnionType newUnionType = removeEnumUnionTypeVisitor.visitUnionType(unionType);
            if (!definition.getUnionReturnTypes().contains(newUnionType)) {
                definition.getUnionReturnTypes().add(newUnionType);
            }
        } else {
            logger.formatError("Unexpected number of union types (%d) for attribute %s in %s%n",
                    unionTypes.size(), member.getName(), definition.getName());
        }
    }
}
