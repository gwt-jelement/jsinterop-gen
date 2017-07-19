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
import com.tenxdev.jsinterop.generator.parsing.ParsingContext;
import org.antlr4.webidl.WebIDLParser;

import java.util.List;

class DictionaryVisitor extends ContextWebIDLBaseVisitor<DictionaryDefinition> {

    public DictionaryVisitor(ParsingContext parsingContext) {
        super(parsingContext);
    }

    @Override
    public DictionaryDefinition visitDictionary(WebIDLParser.DictionaryContext ctx) {
        String name = ctx.IDENTIFIER_WEBIDL().getText();
        String parent = ctx.inheritance() == null || ctx.inheritance().IDENTIFIER_WEBIDL() == null ? null :
                ctx.inheritance().IDENTIFIER_WEBIDL().getText();
        List<DictionaryMember> members = ctx.dictionaryMembers().accept(new DictionaryMemberVisitor(parsingContext));
        return new DictionaryDefinition(name, parent, members);
    }
}
