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

import com.tenxdev.jsinterop.generator.model.*;
import com.tenxdev.jsinterop.generator.model.types.Type;
import com.tenxdev.jsinterop.generator.parsing.ParsingContext;
import com.tenxdev.jsinterop.generator.processing.ParserUtil;
import org.antlr4.webidl.WebIDLParser;

import java.util.Collections;
import java.util.List;

class CallbackVisitor extends ContextWebIDLBaseVisitor<AbstractDefinition> {

    private final List<Constructor> constructors;
    private final List<String> extendedAttributes;

    CallbackVisitor(ParsingContext context, List<Constructor> constructors, List<String> extendedAttributes) {
        super(context);
        this.constructors = constructors;
        this.extendedAttributes = extendedAttributes;
    }

    @Override
    public AbstractDefinition visitCallbackRestOrInterface(WebIDLParser.CallbackRestOrInterfaceContext ctx) {
        if (ctx.interface_() != null) {
            InterfaceDefinition interfaceDefinition = ctx.interface_().accept(new InterfaceVisitor(parsingContext, constructors, extendedAttributes));
            return new CallbackDefinition(interfaceDefinition.getName(),
                    interfaceDefinition.getMethods().get(0), new ExtendedAttributes(extendedAttributes));
        } else if (ctx.callbackRest() != null) {
            return ctx.callbackRest().accept(this);
        } else {
            parsingContext.getlogger().reportError("unexpected state in CallbackRestOrInterface visitor");
            parsingContext.getlogger().reportError("Content: " + ParserUtil.getText(ctx));
            return null;
        }
    }

    @Override
    public CallbackDefinition visitCallbackRest(WebIDLParser.CallbackRestContext ctx) {
        String name = ctx.IDENTIFIER_WEBIDL().getText();
        Type returnType = ctx.returnType().accept(new TypeVisitor(parsingContext));
        List<MethodArgument> arguments = ctx.argumentList() != null && ctx.argumentList().arguments() != null ?
                ctx.argumentList().accept(new ArgumentsVisitor(parsingContext)) : Collections.emptyList();
        Method method = new Method(null, returnType, arguments, false, null,
                new ExtendedAttributes(null));
        return new CallbackDefinition(name, method, new ExtendedAttributes(extendedAttributes));
    }
}
