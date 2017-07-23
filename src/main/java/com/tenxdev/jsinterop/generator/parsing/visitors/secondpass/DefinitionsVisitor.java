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

package com.tenxdev.jsinterop.generator.parsing.visitors.secondpass;

import com.tenxdev.jsinterop.generator.model.AbstractDefinition;
import com.tenxdev.jsinterop.generator.model.Constructor;
import com.tenxdev.jsinterop.generator.parsing.ParsingContext;
import com.tenxdev.jsinterop.generator.processing.ParserUtil;
import org.antlr4.webidl.WebIDLParser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DefinitionsVisitor extends ContextWebIDLBaseVisitor<List<AbstractDefinition>> {

    public DefinitionsVisitor(ParsingContext parsingContext) {
        super(parsingContext);
    }

    @Override
    public List<AbstractDefinition> visitDefinitions(WebIDLParser.DefinitionsContext ctx) {
        List<AbstractDefinition> definitionList = new ArrayList<>();

        WebIDLParser.DefinitionsContext definitions = ctx;
        while (definitions != null && definitions.definition() != null) {
            List<Constructor> constructors = definitions.extendedAttributeList() != null ?
                    ctx.extendedAttributeList().accept(new ExtendedAttributeListVisitor(parsingContext))
                    : Collections.emptyList();
            AbstractDefinition definition = definitions.definition().accept(new DefinitionVisitor(parsingContext, constructors));
            if (definition != null) {
                definitionList.add(definition);
            } else {
                parsingContext.getlogger().reportError("Unexpected missed definition: " + ParserUtil.getText(ctx));
            }
            definitions = definitions.definitions();
        }
        return definitionList;
    }
}
