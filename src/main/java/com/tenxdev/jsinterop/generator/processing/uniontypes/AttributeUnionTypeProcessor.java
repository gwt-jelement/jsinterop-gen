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
import com.tenxdev.jsinterop.generator.model.Attribute;
import com.tenxdev.jsinterop.generator.model.DefinitionInfo;
import com.tenxdev.jsinterop.generator.model.InterfaceDefinition;
import com.tenxdev.jsinterop.generator.model.Model;
import com.tenxdev.jsinterop.generator.model.types.UnionType;

import java.util.ArrayList;
import java.util.List;

public class AttributeUnionTypeProcessor {

    private final Model model;
    private final RemoveEnumUnionTypeVisitor removeEnumUnionTypeVisitor;
    private Logger logger;
    private GetUnionTypesVisitor getUnionTypesVisitor = new GetUnionTypesVisitor();
    private HasUnionTypeVisitor hasUnionTypeVisitor = new HasUnionTypeVisitor();

    public AttributeUnionTypeProcessor(Model model, Logger logger) {
        this.model = model;
        this.logger = logger;
        this.removeEnumUnionTypeVisitor = new RemoveEnumUnionTypeVisitor(model, logger);

    }

    public void process() {
        logger.info(Logger.LEVEL_INFO, () -> "Processing union type attributes");
        model.getDefinitions().stream()
                .filter(definitionInfo -> definitionInfo.getDefinition().getClass() == InterfaceDefinition.class)
                .forEach(definitionInfo ->
                        processInterfaceDefinition(definitionInfo, (InterfaceDefinition) definitionInfo.getDefinition()));
    }

    private void processInterfaceDefinition(DefinitionInfo definitionInfo, InterfaceDefinition definition) {
        List<Attribute> newAttributes = new ArrayList<>();
        definition.getAttributes().stream()
                .filter(attribute -> hasUnionTypeVisitor.accept(attribute.getType()))
                .forEach(attribute -> newAttributes.addAll(processAttribute(attribute, definitionInfo, definition)));
        definition.getAttributes().addAll(newAttributes);
    }

    private List<Attribute> processAttribute(Attribute attribute, DefinitionInfo definitionInfo,
                                             InterfaceDefinition definition) {
        List<Attribute> newAttributes = new ArrayList<>();
        List<UnionType> unionTypes = getUnionTypesVisitor.accept(attribute.getType());
        if (unionTypes.size() == 1) {
            UnionType unionType = unionTypes.get(0);
            UnionType newUnionType = removeEnumUnionTypeVisitor.visitUnionType(unionType);
            definition.addUnionReturnType(definitionInfo, newUnionType);
            //writeable attribute
//            if (!attribute.isReadOnly()) {
//                for (Type type : unionType.getTypes()) {
//                    Attribute newAttribute = new Attribute(attribute.getName(), type,
//                            false, attribute.isStatic());
//                    newAttribute.setWriteOnly(true);
//                    newAttribute.setReference(attribute);
//                    newAttributes.add(newAttribute);
//                }
//            }
        } else {
            logger.formatError("Unexpected number of union types (%d) for attribute %s in %s%n",
                    unionTypes.size(), attribute.getName(), definition.getName());
        }
        return newAttributes;
    }

}
