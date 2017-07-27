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
import com.tenxdev.jsinterop.generator.model.Feature;
import com.tenxdev.jsinterop.generator.model.InterfaceDefinition;
import com.tenxdev.jsinterop.generator.model.Model;

public class InterfaceFeatureProcessor {

    private Model model;
    private Logger logger;

    public InterfaceFeatureProcessor(Model model, Logger logger) {
        this.model = model;
        this.logger = logger;
    }

    public void process() {
        logger.info(() -> "Processing interface features");
        model.getInterfaceDefinitions().stream()
                .filter(definition -> !definition.getFeatures().isEmpty())
                .forEach(this::processInterface);
    }

    private void processInterface(InterfaceDefinition definition) {
        for (Feature feature : definition.getFeatures()) {
            switch (feature.getFeatureType()) {
                case VALUE_ITERATOR:
                    processValueIterator(definition, feature);
                    break;
                case MAP_ITERATOR:
                    processMapIterator(definition, feature);
                    break;
                case MAP_LIKE:
                    processMapLike(definition, feature);
                    break;
                case SET_LIKE:
                    processSetLike(definition, feature);
                    break;
                case STRINGIFIER:
                    processStringifier(definition, feature);
                    break;
            }
        }
    }

    private void processStringifier(InterfaceDefinition definition, Feature feature) {
    }

    private void processSetLike(InterfaceDefinition definition, Feature feature) {
    }

    private void processMapLike(InterfaceDefinition definition, Feature feature) {
    }

    private void processMapIterator(InterfaceDefinition definition, Feature feature) {
    }

    private void processValueIterator(InterfaceDefinition definition, Feature feature) {
    }
}
