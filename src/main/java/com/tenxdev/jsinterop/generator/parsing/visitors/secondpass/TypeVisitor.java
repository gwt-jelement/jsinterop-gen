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

import com.tenxdev.jsinterop.generator.model.types.Type;
import com.tenxdev.jsinterop.generator.parsing.ParsingContext;
import org.antlr4.webidl.WebIDLParser;

class TypeVisitor extends ContextWebIDLBaseVisitor<Type> {

    public TypeVisitor(ParsingContext parsingContext) {
        super(parsingContext);
    }

    @Override
    public Type visitType(WebIDLParser.TypeContext ctx) {
        if (ctx.singleType() != null) {
            return parsingContext.getTypeFactory().getType(ctx.singleType().getText());
        } else if (ctx.arrayType() != null) {
            return parsingContext.getTypeFactory().getType(ctx.arrayType().getText());
        } else if (ctx.unionType() != null) {
            return ctx.unionType().accept(this);
        } else {
            parsingContext.getlogger().reportError("Unexpected state in visitType");
            return null;
        }
    }

    @Override
    public Type visitUnionType(WebIDLParser.UnionTypeContext ctx) {
        String type = ctx.unionMemberType(0).getText() + '|' + ctx.unionMemberType(1).getText();
        if (ctx.unionMemberTypes() != null) {
            type += getType(ctx.unionMemberTypes());
        }
        return parsingContext.getTypeFactory().getUnionType(type.split("\\|"));
    }


    private String getType(WebIDLParser.UnionMemberTypesContext ctx) {
        StringBuilder type = new StringBuilder();
        WebIDLParser.UnionMemberTypesContext context = ctx;
        while (context != null && context.unionMemberType() != null) {
            type.append('|').append(context.unionMemberType().getText());
            context = context.unionMemberTypes();
        }
        return type.toString();
    }
}
