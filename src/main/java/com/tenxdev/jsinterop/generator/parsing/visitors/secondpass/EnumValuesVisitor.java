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

import com.tenxdev.jsinterop.generator.parsing.ParsingContext;
import org.antlr4.webidl.WebIDLParser;

import java.util.ArrayList;
import java.util.List;

class EnumValuesVisitor extends ContextWebIDLBaseVisitor<List<String>> {

    public EnumValuesVisitor(ParsingContext parsingContext) {
        super(parsingContext);
    }

    @Override
    public List<String> visitEnumValueList(WebIDLParser.EnumValueListContext ctx) {
        List<String> values = new ArrayList<>();
        values.add(ctx.STRING_WEBIDL().getText());
        if (ctx.enumValueListComma() != null) {
            values.addAll(ctx.enumValueListComma().accept(this));
        }
        return values;
    }

    @Override
    public List<String> visitEnumValueListComma(WebIDLParser.EnumValueListCommaContext ctx) {
        List<String> values = new ArrayList<>();
        WebIDLParser.EnumValueListCommaContext context = ctx;
        while (context != null && context.enumValueListString() != null && context.enumValueListString().STRING_WEBIDL() != null) {
            values.add(context.enumValueListString().STRING_WEBIDL().getText());
            context = context.enumValueListString().enumValueListComma();
        }
        return values;
    }
}

