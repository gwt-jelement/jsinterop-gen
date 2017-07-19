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

import com.tenxdev.jsinterop.generator.model.Method;
import com.tenxdev.jsinterop.generator.parsing.ParsingContext;
import org.antlr4.webidl.WebIDLParser;

import java.util.ArrayList;
import java.util.List;

/**
 * finds a constructor definition in the extended attribute list for a definition
 */
class ExtendedAttributeListVisitor extends ContextWebIDLBaseVisitor<List<Method>> {

    public ExtendedAttributeListVisitor(ParsingContext parsingContext) {
        super(parsingContext);
    }

    @Override
    public List<Method> visitExtendedAttributeList(WebIDLParser.ExtendedAttributeListContext ctx) {
        List<Method> constructors = new ArrayList<>();
        if (ctx.extendedAttribute() != null) {
            Method constructor = getConstructor(ctx.extendedAttribute());
            if (constructor != null) {
                constructors.add(constructor);
            }
        }
        if (ctx.extendedAttributes() != null) {
            constructors.addAll(ctx.extendedAttributes().accept(this));
        }
        return constructors;
    }

    private Method getConstructor(WebIDLParser.ExtendedAttributeContext ctx) {
        if (ctx.other() != null && "Constructor".equals(ctx.other().getText())) {
            return ctx.extendedAttributeRest().accept(new ConstructorVisitor(parsingContext));
        }
        return null;
    }

    @Override
    public List<Method> visitExtendedAttributes(WebIDLParser.ExtendedAttributesContext ctx) {
        List<Method> constructors = new ArrayList<>();
        if (ctx.extendedAttribute() != null) {
            Method constructor = getConstructor(ctx.extendedAttribute());
            if (constructor != null) {
                constructors.add(constructor);
            }
        }
        if (ctx.extendedAttributes() != null) {
            constructors.addAll(ctx.extendedAttributes().accept(this));
        }
        return constructors;
    }
}
