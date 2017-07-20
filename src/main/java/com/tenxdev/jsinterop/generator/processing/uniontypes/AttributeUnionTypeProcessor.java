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
import com.tenxdev.jsinterop.generator.model.*;
import com.tenxdev.jsinterop.generator.model.types.NativeType;
import com.tenxdev.jsinterop.generator.model.types.UnionType;

import java.util.ArrayList;
import java.util.List;

import static org.eclipse.xtext.xbase.lib.StringExtensions.toFirstUpper;

public class AttributeUnionTypeProcessor {

    private final Model model;
    private Logger logger;
    private GetUnionTypesVisitor getUnionTypesVisitor = new GetUnionTypesVisitor();
    private HasUnionTypeVisitor hasUnionTypeVisitor = new HasUnionTypeVisitor();

    public AttributeUnionTypeProcessor(Model model, Logger logger) {
        this.model = model;
        this.logger = logger;
    }

    public void process() {
        logger.info(Logger.LEVEL_INFO, () -> "Processing union type attributes");
        model.getDefinitions().stream()
                .filter(definitionInfo -> definitionInfo.getDefinition().getClass() == InterfaceDefinition.class)
                .map(definitionInfo -> (InterfaceDefinition) definitionInfo.getDefinition())
                .forEach(this::processInterfaceDefinition);
        model.getDefinitions().stream()
                .filter(definitionInfo -> definitionInfo.getDefinition().getClass() == DictionaryDefinition.class)
                .map(definitionInfo -> (DictionaryDefinition) definitionInfo.getDefinition())
                .forEach(this::processDictionaryDefinition);

    }

    private void processDictionaryDefinition(DictionaryDefinition definition) {
        definition.getMembers().stream()
                .filter(dictionaryMember -> hasUnionTypeVisitor.accept(dictionaryMember.getType()))
                .forEach(dictionaryMember -> processDictionaryMember(dictionaryMember, definition));
    }

    private void processInterfaceDefinition(InterfaceDefinition definition) {
        definition.getAttributes().stream()
                .filter(attribute -> hasUnionTypeVisitor.accept(attribute.getType()))
                .forEach(attribute -> processAttribute(attribute, definition));
    }

    private void processAttribute(Attribute attribute, InterfaceDefinition definition) {
        List<UnionType> unionTypes = getUnionTypesVisitor.accept(attribute.getType());
        if (unionTypes.size() == 1) {
            UnionType unionType = unionTypes.get(0);
            if (unionType.getName() == null) {
                unionType.setName(toFirstUpper(attribute.getName()) + "UnionType");

            }
            Attribute newAttribute = new Attribute(attribute.getName(), new NativeType(unionType.getName()),
                    attribute.isReadOnly(), attribute.isStatic());
            int index = definition.getAttributes().indexOf(attribute);
            definition.getAttributes().set(index, newAttribute);
            if (definition.getUnionReturnTypes() == null) {
                definition.setUnionReturnTypes(new ArrayList<>());
            }
            definition.getUnionReturnTypes().add(unionType);
        } else {
            logger.formatError("Unexpected number of union types (%d) for attribute %s in %s%n",
                    unionTypes.size(), attribute.getName(), definition.getName());
        }
    }

    private void processDictionaryMember(DictionaryMember dictionaryMember, DictionaryDefinition definition) {
        List<UnionType> unionTypes = getUnionTypesVisitor.accept(dictionaryMember.getType());
        if (unionTypes.size() == 1) {
            UnionType unionType = unionTypes.get(0);
            if (unionType.getName() == null) {
                unionType.setName(toFirstUpper(dictionaryMember.getName()) + "UnionType");

            }
            DictionaryMember newMember = new DictionaryMember(dictionaryMember.getName(),
                    new NativeType(unionType.getName()),
                    dictionaryMember.isRequired(), dictionaryMember.getDefaultValue());
            int index = definition.getMembers().indexOf(dictionaryMember);
            definition.getMembers().set(index, newMember);
            if (definition.getUnionReturnTypes() == null) {
                definition.setUnionReturnTypes(new ArrayList<>());
            }
            definition.getUnionReturnTypes().add(unionType);
        } else {
            logger.formatError("Unexpected number of union types (%d) for attribute %s in %s%n",
                    unionTypes.size(), dictionaryMember.getName(), definition.getName());
        }
    }


}
