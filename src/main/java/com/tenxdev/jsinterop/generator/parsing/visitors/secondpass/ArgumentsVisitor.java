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

import com.tenxdev.jsinterop.generator.model.MethodArgument;
import com.tenxdev.jsinterop.generator.parsing.ParsingContext;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr4.webidl.WebIDLParser;

import java.util.ArrayList;
import java.util.List;

class ArgumentsVisitor extends ContextWebIDLBaseVisitor<List<MethodArgument>> {

    public ArgumentsVisitor(ParsingContext parsingContext) {
        super(parsingContext);
    }

    @Override
    public List<MethodArgument> visitArguments(WebIDLParser.ArgumentsContext ctx) {
        List<MethodArgument> argumentList = new ArrayList<>();
        for (WebIDLParser.ArgumentsContext arguments = ctx; arguments != null && arguments.argument() != null; arguments = arguments.arguments()) {
            List<String> extendedAttributes = arguments.argument().extendedAttributeList() != null ?
                    arguments.argument().extendedAttributeList().accept(new GenericExtendedAttributeListVisitor()) :
                    null;
            boolean added = visitIfNotNull(arguments.argument().optionalOrRequiredArgument().optionalArgument(), argumentList, extendedAttributes) ||
                    visitIfNotNull(arguments.argument().optionalOrRequiredArgument().requiredArgument(), argumentList, extendedAttributes) ||
                    visitIfNotNull(arguments.argument().optionalOrRequiredArgument().requiredVarArgArgument(), argumentList, extendedAttributes);
            if (!added) {
                parsingContext.getLogger().reportError("Invalid state in Arguments visitor");
            }
        }
        return argumentList;
    }

    private boolean visitIfNotNull(ParserRuleContext context, List<MethodArgument> argumentList, List<String> extendedAttributes) {
        if (context != null) {
            argumentList.add(context.accept(new ArgumentVisitor(parsingContext, extendedAttributes)));
            return true;
        }
        return false;
    }

    @Override
    public List<MethodArgument> visitArgumentList(WebIDLParser.ArgumentListContext ctx) {
        List<MethodArgument> argumentList = new ArrayList<>();
        List<String> extendedAttributes = ctx.argument().extendedAttributeList() != null ?
                ctx.argument().extendedAttributeList().accept(new GenericExtendedAttributeListVisitor()) :
                null;
        boolean added = visitIfNotNull(ctx.argument().optionalOrRequiredArgument().optionalArgument(), argumentList, extendedAttributes) ||
                visitIfNotNull(ctx.argument().optionalOrRequiredArgument().requiredArgument(), argumentList, extendedAttributes) ||
                visitIfNotNull(ctx.argument().optionalOrRequiredArgument().requiredVarArgArgument(), argumentList, extendedAttributes);
        if (!added) {
            parsingContext.getLogger().reportError("Invalid state in Arguments visitor");
        }
        if (ctx.arguments() != null && ctx.arguments().argument() != null) {
            argumentList.addAll(ctx.arguments().accept(this));
        }
        return argumentList;
    }
}
