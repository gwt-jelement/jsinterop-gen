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

import com.tenxdev.jsinterop.generator.model.ExtendedAttributes;
import com.tenxdev.jsinterop.generator.model.MethodArgument;
import com.tenxdev.jsinterop.generator.model.types.GenericType;
import com.tenxdev.jsinterop.generator.model.types.ParameterisedType;
import com.tenxdev.jsinterop.generator.model.types.Type;
import com.tenxdev.jsinterop.generator.parsing.ParsingContext;
import org.antlr4.webidl.WebIDLParser;

import java.util.Collections;
import java.util.List;

class ArgumentVisitor extends ContextWebIDLBaseVisitor<MethodArgument> {

    private final List<String> extendedAttributes;

    ArgumentVisitor(ParsingContext parsingContext, List<String> extendedAttributes) {
        super(parsingContext);
        this.extendedAttributes = extendedAttributes;
    }

    @Override
    public MethodArgument visitOptionalArgument(WebIDLParser.OptionalArgumentContext ctx) {
        String name = ctx.argumentName().getText();
        Type type = adjustType(ctx.type().accept(new TypeVisitor(parsingContext)));

        String defaultValue = ctx.default_() == null || ctx.default_().defaultValue() == null ? null :
                ctx.default_().defaultValue().getText();
        return new MethodArgument(name, type, false, true, defaultValue,
                new ExtendedAttributes(extendedAttributes));
    }

    @Override
    public MethodArgument visitRequiredArgument(WebIDLParser.RequiredArgumentContext ctx) {
        String name = ctx.argumentName().getText();
        Type type = adjustType(ctx.type().accept(new TypeVisitor(parsingContext)));
        return new MethodArgument(name, type, false, false, null,
                new ExtendedAttributes(extendedAttributes));
    }

    @Override
    public MethodArgument visitRequiredVarArgArgument(WebIDLParser.RequiredVarArgArgumentContext ctx) {
        String name = ctx.argumentName().getText();
        Type type = adjustType(ctx.type().accept(new TypeVisitor(parsingContext)));
        return new MethodArgument(name, type, true, false, null,
                new ExtendedAttributes(extendedAttributes));
    }

    private Type adjustType(Type type){
        ExtendedAttributes extendedAttributes=new ExtendedAttributes(this.extendedAttributes);
        String genericParameter = extendedAttributes.extractValue(ExtendedAttributes.GENERIC_PARAMETER, null);
        if (genericParameter!=null){
            return new ParameterisedType(type,
                    Collections.singletonList(parsingContext.getTypeFactory().getTypeNoParse(genericParameter)));
        }
        String genericSubstitution = extendedAttributes.extractValue(ExtendedAttributes.GENERIC_SUB, null);
        if (genericSubstitution!=null) {
            return new GenericType(genericSubstitution);
        }
        return type;
    }

}
