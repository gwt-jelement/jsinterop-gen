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
import org.antlr4.webidl.WebIDLBaseVisitor;
import org.antlr4.webidl.WebIDLParser;

import java.util.ArrayList;
import java.util.List;

class GenericExtendedAttribeListVisitor extends WebIDLBaseVisitor<List<String>> {

    @Override
    public List<String> visitExtendedAttributeList(WebIDLParser.ExtendedAttributeListContext ctx) {
        List<String> extendedAttributes = new ArrayList<>();
        if (ctx.extendedAttribute() != null) {
            extendedAttributes.add(cleanup(ParsingContext.getText(ctx.extendedAttribute())));
        }
        processRest(ctx.extendedAttributes(), extendedAttributes);
        return extendedAttributes.isEmpty() ? null : extendedAttributes;
    }

    private void processRest(WebIDLParser.ExtendedAttributesContext extendedAttributesContext,
                             List<String> extendedAttributes) {
        WebIDLParser.ExtendedAttributesContext ctx = extendedAttributesContext;
        while (ctx != null) {
            if (ctx.extendedAttribute() != null) {
                extendedAttributes.add(cleanup(ParsingContext.getText(ctx.extendedAttribute())));
            }
            ctx = ctx.extendedAttributes();
        }
    }

    private String cleanup(String value) {
        return value.trim()
                .replaceAll("\\s+\\(","(")
                .replaceAll("\\s+"," ")
                .replaceAll("\\(\\s+","(")
                .replaceAll("\\s+\\)",")")
                .replaceAll("\\s+=\\s+","=")
                .replaceAll("\\s+,",",");
    }
}
