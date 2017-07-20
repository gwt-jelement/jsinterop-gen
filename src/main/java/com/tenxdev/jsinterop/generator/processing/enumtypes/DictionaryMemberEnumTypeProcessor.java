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
import com.tenxdev.jsinterop.generator.model.DictionaryDefinition;
import com.tenxdev.jsinterop.generator.model.DictionaryMember;
import com.tenxdev.jsinterop.generator.model.Model;
import com.tenxdev.jsinterop.generator.model.types.Type;
import com.tenxdev.jsinterop.generator.model.types.UnionType;

public class DictionaryMemberEnumTypeProcessor {

    private Model model;
    private Logger logger;
    private HasEnumTypeVisitor hasEnumTypeVisitor = new HasEnumTypeVisitor();
    private EnumSubstitutionVisitor enumSubstitutionVisitor;

    public DictionaryMemberEnumTypeProcessor(Model model, Logger logger) {
        this.model = model;
        this.logger = logger;
        this.enumSubstitutionVisitor = new EnumSubstitutionVisitor(model, logger);
    }

    public void process() {
        logger.info(Logger.LEVEL_INFO, () -> "Processing enum types in dictionaries");
        model.getDefinitions().stream()
                .filter(definitionInfo -> definitionInfo.getDefinition().getClass() == DictionaryDefinition.class)
                .map(definitionInfo -> (DictionaryDefinition) definitionInfo.getDefinition())
                .forEach(this::processDictionaryDefinition);
    }

    private void processDictionaryDefinition(DictionaryDefinition definition) {
        definition.getMembers().stream()
                .filter(dictionaryMember -> hasEnumTypeVisitor.accept(dictionaryMember.getType()))
                .forEach(this::processDictionaryMember);
    }

    private void processDictionaryMember(DictionaryMember member) {
        Type newType = enumSubstitutionVisitor.accept(member.getType());
        if (!(newType instanceof UnionType)) {
            member.setEnumSubstitutionType(newType);
        }
    }

}
