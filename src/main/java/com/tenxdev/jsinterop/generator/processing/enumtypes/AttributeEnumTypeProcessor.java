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

package com.tenxdev.jsinterop.generator.processing.enumtypes;

import com.tenxdev.jsinterop.generator.logging.Logger;
import com.tenxdev.jsinterop.generator.model.*;
import com.tenxdev.jsinterop.generator.model.types.Type;

public class AttributeEnumTypeProcessor {

    private Model model;
    private Logger logger;
    private HasEnumTypeVisitor hasEnumTypeVisitor = new HasEnumTypeVisitor();
    private EnumSubstitutionVisitor enumSubstitutionVisitor;

    public AttributeEnumTypeProcessor(Model model, Logger logger) {
        this.model = model;
        this.logger = logger;
        this.enumSubstitutionVisitor = new EnumSubstitutionVisitor(model, logger);
    }

    public void process() {
        logger.info(Logger.LEVEL_INFO, () -> "Processing enum type attributes");
        model.getDefinitions().stream()
                .filter(definitionInfo -> definitionInfo.getDefinition().getClass() == InterfaceDefinition.class)
                .map(definitionInfo -> (InterfaceDefinition) definitionInfo.getDefinition())
                .forEach(this::processInterfaceDefinition);
        model.getDefinitions().stream()
                .filter(definitionInfo -> definitionInfo.getDefinition().getClass() == DictionaryDefinition.class)
                .map(definitionInfo -> (DictionaryDefinition) definitionInfo.getDefinition())
                .forEach(this::processDictionaryDefinition);
    }

    private void processInterfaceDefinition(InterfaceDefinition definition) {
        definition.getAttributes().stream()
                .filter(attribute -> hasEnumTypeVisitor.accept(attribute.getType()))
                .forEach(attribute -> processAttribute(attribute, definition));
    }

    private void processDictionaryDefinition(DictionaryDefinition definition) {
        definition.getMembers().stream()
                .filter(dictionaryMember -> hasEnumTypeVisitor.accept(dictionaryMember.getType()))
                .forEach(dictionaryMember -> processDictionaryMember(dictionaryMember, definition));
    }

    private void processAttribute(Attribute attribute, InterfaceDefinition definition) {
        Type newType = enumSubstitutionVisitor.accept(attribute.getType());
        attribute.setEnumSubstitutionType(newType);
    }

    private void processDictionaryMember(DictionaryMember member, DictionaryDefinition definition) {
        Type newType = enumSubstitutionVisitor.accept(member.getType());
        member.setEnumSubstitutionType(newType);
    }
}
