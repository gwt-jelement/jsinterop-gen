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

import com.tenxdev.jsinterop.generator.model.DictionaryDefinition;
import com.tenxdev.jsinterop.generator.model.DictionaryMember;
import com.tenxdev.jsinterop.generator.model.ExtendedAttributes;
import com.tenxdev.jsinterop.generator.model.types.GenericType;
import com.tenxdev.jsinterop.generator.model.types.ParameterisedType;
import com.tenxdev.jsinterop.generator.model.types.Type;
import com.tenxdev.jsinterop.generator.parsing.ParsingContext;
import org.antlr4.webidl.WebIDLParser;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

class DictionaryVisitor extends ContextWebIDLBaseVisitor<DictionaryDefinition> {

    private final ExtendedAttributes extendedAttributes;

    public DictionaryVisitor(ParsingContext parsingContext, List<String> extendedAttributes) {
        super(parsingContext);
        this.extendedAttributes = new ExtendedAttributes(extendedAttributes);
    }

    @Override
    public DictionaryDefinition visitDictionary(WebIDLParser.DictionaryContext ctx) {
        String name = ctx.IDENTIFIER_WEBIDL().getText();
        String parent = ctx.inheritance() == null || ctx.inheritance().IDENTIFIER_WEBIDL() == null ? null :
                ctx.inheritance().IDENTIFIER_WEBIDL().getText();
        List<DictionaryMember> members = ctx.dictionaryMembers().accept(new DictionaryMemberVisitor(parsingContext));
        Type parentType = parent == null
                ? parsingContext.getTypeFactory().getTypeNoParse("JsObject")
                : parsingContext.getTypeFactory().getTypeNoParse(parent);
        String[] genericExtendTypeNames = extendedAttributes.extractValues(ExtendedAttributes.GENERIC_EXTEND, null);
        if (genericExtendTypeNames != null) {
            List<Type> genericTypes = Arrays.stream(genericExtendTypeNames)
                    .map(genericTypeName->
                            genericTypeName.length()==1? new GenericType(genericTypeName):
                                    parsingContext.getTypeFactory().getTypeNoParse(genericTypeName))
                    .collect(Collectors.toList());
            parentType = new ParameterisedType(parentType, genericTypes);
        }
        return new DictionaryDefinition(name, parentType, members, extendedAttributes);
    }
}
