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

package com.tenxdev.jsinterop.generator.parsing.visitors.firstpass;

import com.tenxdev.jsinterop.generator.model.types.ObjectType;
import com.tenxdev.jsinterop.generator.parsing.ParsingContext;
import com.tenxdev.jsinterop.generator.parsing.visitors.secondpass.ContextWebIDLBaseVisitor;
import com.tenxdev.jsinterop.generator.processing.ParserUtil;
import org.antlr4.webidl.WebIDLParser;

class CallbackScanner extends ContextWebIDLBaseVisitor<Void> {

    public CallbackScanner(ParsingContext context) {
        super(context);
    }

    @Override
    public Void visitCallbackRestOrInterface(WebIDLParser.CallbackRestOrInterfaceContext ctx) {
        if (ctx.interface_() != null) {
            return ctx.interface_().accept(new InterfaceScanner(parsingContext));
        } else if (ctx.callbackRest() != null) {
            return ctx.callbackRest().accept(this);
        } else {
            parsingContext.getlogger().reportError("unexpected state in CallbackRestOrInterface visitor");
            parsingContext.getlogger().reportError("Content: " + ParserUtil.getText(ctx));
            return null;
        }
    }

    @Override
    public Void visitCallbackRest(WebIDLParser.CallbackRestContext ctx) {
        String name = ctx.IDENTIFIER_WEBIDL().getText();
        parsingContext.getTypeFactory().registerType(name,
                new ObjectType(name, parsingContext.getPackageSuffix()));
        return null;
    }
}
