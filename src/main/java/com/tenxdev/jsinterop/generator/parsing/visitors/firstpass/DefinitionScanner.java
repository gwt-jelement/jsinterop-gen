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

import com.tenxdev.jsinterop.generator.parsing.ParsingContext;
import com.tenxdev.jsinterop.generator.parsing.visitors.secondpass.ContextWebIDLBaseVisitor;
import org.antlr4.webidl.WebIDLParser;

class DefinitionScanner extends ContextWebIDLBaseVisitor<Void> {

    DefinitionScanner(ParsingContext context) {
        super(context);
    }

    @Override
    public Void visitDefinition(WebIDLParser.DefinitionContext ctx) {
        if (ctx.callbackOrInterface() != null) {
            if (ctx.callbackOrInterface().interface_() != null) {
                return ctx.callbackOrInterface().interface_()
                        .accept(new InterfaceScanner(parsingContext));
            } else if (ctx.callbackOrInterface().callbackRestOrInterface() != null) {
                return ctx.callbackOrInterface().callbackRestOrInterface()
                        .accept(new CallbackScanner(parsingContext));
            }
        } else if (ctx.dictionary() != null) {
            return ctx.dictionary().accept(new DictionaryScanner(parsingContext));
        } else if (ctx.enum_() != null) {
            return ctx.enum_().accept(new EnumScanner(parsingContext));
        } else if (ctx.typedef() != null) {
            return ctx.typedef().accept(new TypeDefScanner(parsingContext));
        }
        return null;
    }
}
