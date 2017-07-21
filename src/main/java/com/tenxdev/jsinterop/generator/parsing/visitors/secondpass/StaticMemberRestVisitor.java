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

import com.tenxdev.jsinterop.generator.model.interfaces.InterfaceMember;
import com.tenxdev.jsinterop.generator.model.types.Type;
import com.tenxdev.jsinterop.generator.parsing.ParsingContext;
import org.antlr4.webidl.WebIDLParser;

class StaticMemberRestVisitor extends ContextWebIDLBaseVisitor<InterfaceMember> {

    StaticMemberRestVisitor(ParsingContext parsingContext) {
        super(parsingContext);
    }

    @Override
    public InterfaceMember visitStaticMemberRest(WebIDLParser.StaticMemberRestContext ctx) {
        if (ctx.attributeRest() != null) {
            boolean readOnly = ctx.readOnly() != null && "readonly".equals(ctx.readOnly().getText());
            return ctx.attributeRest().accept(new AttributeRestVisitor(parsingContext, readOnly, true));

        }
        if (ctx.operationRest() != null) {
            Type returnType = ctx.returnType().accept(new TypeVisitor(parsingContext));
            return ctx.operationRest().accept(new OperationRestVisitor(parsingContext, returnType, true));
        }
        parsingContext.getlogger().reportError("Unexpected state in StaticMemberRest");
        return null;
    }
}
