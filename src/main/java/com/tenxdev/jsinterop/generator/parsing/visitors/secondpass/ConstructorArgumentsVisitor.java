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
import com.tenxdev.jsinterop.generator.parsing.FileAwareANTLRErrorListener;
import com.tenxdev.jsinterop.generator.parsing.ParsingContext;
import com.tenxdev.jsinterop.generator.parsing.visitors.firstpass.DefinitionsScanner;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr4.webidl.WebIDLLexer;
import org.antlr4.webidl.WebIDLParser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class ConstructorArgumentsVisitor extends ContextWebIDLBaseVisitor<List<MethodArgument>> {

    public ConstructorArgumentsVisitor(ParsingContext parsingContext) {
        super(parsingContext);
    }

    @Override
    public List<MethodArgument> visitConstructorArgs(WebIDLParser.ConstructorArgsContext ctx) {
        return ctx.argumentList().accept(new ArgumentsVisitor(parsingContext));
    }

    @Override
    public List<MethodArgument> visitExtendedAttributeInner(WebIDLParser.ExtendedAttributeInnerContext ctx) {
        String argumentText=ParsingContext.getText(ctx).trim();
        if (!argumentText.isEmpty()) {
            WebIDLLexer lexer = new WebIDLLexer(new ANTLRInputStream(argumentText));
            CommonTokenStream tokenStream = new CommonTokenStream(lexer);
            WebIDLParser parser = new WebIDLParser(tokenStream);
            parser.removeErrorListeners();
            parser.addErrorListener(parsingContext.getParser().getErrorListeners().get(0));
            WebIDLParser.ConstructorArgsContext constructorArgs = parser.constructorArgs();
            List<MethodArgument> argumentList = constructorArgs.accept(this);
            return argumentList;
        }
        return Collections.emptyList();
    }
}

