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
import com.tenxdev.jsinterop.generator.logging.VelocityLogger;
import com.tenxdev.jsinterop.generator.model.AbstractDefinition;
import com.tenxdev.jsinterop.generator.model.Model;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.tools.generic.DisplayTool;

import java.io.StringWriter;
import java.util.Arrays;
import java.util.List;

public class TemplateFiller {

    private static final List<String> GWT_PRIMITIVE_TYPES = Arrays.asList(
            "boolean", "char", "byte", "short", "int", "float", "double");
    private final VelocityEngine velocityEngine;
    private final Model model;
    private final Logger logger;

    public TemplateFiller(Model model, Logger logger) {
        this.model = model;
        this.logger = logger;
        velocityEngine = new VelocityEngine();
        velocityEngine.setProperty("runtime.log.logsystem", new VelocityLogger(logger));
        velocityEngine.init();
    }

    public String fill(AbstractDefinition definition, String basePackage) {
        if (definition.getExtension() != null) {
            VelocityContext context = new VelocityContext();
            context.put("GWT_PRIMITIVE_TYPES", GWT_PRIMITIVE_TYPES);
            context.put("model", model);
            context.put("definition", definition);
            context.put("basePackage", basePackage);
            context.put("display", new DisplayTool());
            StringWriter writer = new StringWriter();
            return velocityEngine.evaluate(context, writer, definition.getName(),
                    definition.getExtension().getTemplate()) ?
                    writer.toString() : "";
        }
        return "";
    }
}
