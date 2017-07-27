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
import com.tenxdev.jsinterop.generator.model.Model;

/**
 * if A extends B, and both A and B implement C, remove C from A
 */
public class RedundantImplementsRemoval extends AbstractParentModelProcessor {

    public void process(Model model, Logger logger) {
        logger.info(() -> "Removing redundant implements definitions");
        for (AbstractDefinition definition : model.getDefinitions()) {
            AbstractDefinition parentAbstractDefinition = getParentDefinition(model, definition);
            while (parentAbstractDefinition != null) {
                definition.getImplementsDefinitions().removeAll(parentAbstractDefinition.getImplementsDefinitions());
                parentAbstractDefinition = getParentDefinition(model, parentAbstractDefinition);
            }
        }
    }

}
