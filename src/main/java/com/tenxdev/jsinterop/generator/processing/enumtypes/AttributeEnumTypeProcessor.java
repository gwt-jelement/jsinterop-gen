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
import com.tenxdev.jsinterop.generator.model.Attribute;
import com.tenxdev.jsinterop.generator.model.InterfaceDefinition;
import com.tenxdev.jsinterop.generator.model.Model;
import com.tenxdev.jsinterop.generator.model.types.Type;
import com.tenxdev.jsinterop.generator.model.types.UnionType;

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
        logger.info(() -> "Processing enum type attributes");
        model.getInterfaceDefinitions().forEach(this::processInterfaceDefinition);
    }

    private void processInterfaceDefinition(InterfaceDefinition definition) {
        definition.getAttributes().stream()
                .filter(attribute -> hasEnumTypeVisitor.accept(attribute.getType()))
                .forEach(this::processAttribute);
    }

    private void processAttribute(Attribute attribute) {
        Type newType = enumSubstitutionVisitor.accept(attribute.getType());
        if (!(newType instanceof UnionType)) {
            attribute.setEnumSubstitutionType(newType);
        }
    }
}
