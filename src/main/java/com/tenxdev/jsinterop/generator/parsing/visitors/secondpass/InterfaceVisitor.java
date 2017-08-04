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

import com.tenxdev.jsinterop.generator.model.Constructor;
import com.tenxdev.jsinterop.generator.model.ExtendedAttributes;
import com.tenxdev.jsinterop.generator.model.InterfaceDefinition;
import com.tenxdev.jsinterop.generator.model.interfaces.InterfaceMember;
import com.tenxdev.jsinterop.generator.model.types.GenericType;
import com.tenxdev.jsinterop.generator.model.types.ParameterisedType;
import com.tenxdev.jsinterop.generator.model.types.Type;
import com.tenxdev.jsinterop.generator.parsing.ParsingContext;
import org.antlr4.webidl.WebIDLParser;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

class InterfaceVisitor extends ContextWebIDLBaseVisitor<InterfaceDefinition> {

    private final List<Constructor> constructors;
    private final List<String> extendedAttributes;

    public InterfaceVisitor(ParsingContext context, List<Constructor> constructors, List<String> extendedAttributes) {
        super(context);
        this.constructors = constructors;
        this.extendedAttributes = extendedAttributes;
    }

    @Override
    public InterfaceDefinition visitInterface_(WebIDLParser.Interface_Context ctx) {
        String name = ctx.name.getText();
        Type parent = ctx.inheritance() == null || ctx.inheritance().IDENTIFIER_WEBIDL() == null
                ? parsingContext.getTypeFactory().getTypeNoParse("IsObject")
                : parsingContext.getTypeFactory().getTypeNoParse(ctx.inheritance().IDENTIFIER_WEBIDL().getText());
        ExtendedAttributes extendedAttributes = new ExtendedAttributes(this.extendedAttributes);
        String[] genericExtendTypeNames = extendedAttributes.extractValues(ExtendedAttributes.GENERIC_EXTEND, null);
        if (genericExtendTypeNames != null) {
            List<Type> genericTypes = Arrays.stream(genericExtendTypeNames)
                    .map(genericTypeName->
                    genericTypeName.length()==1? new GenericType(genericTypeName):
                    parsingContext.getTypeFactory().getTypeNoParse(genericTypeName))
                    .collect(Collectors.toList());
            parent = new ParameterisedType(parent, genericTypes);
        }
        List<InterfaceMember> members = ctx.interfaceMembers().accept(new InterfaceMembersVisitor(parsingContext, name));
        return new InterfaceDefinition(name, parent.getTypeName().equals(name) ? null : parent, constructors,
                members, extendedAttributes);
    }
}
