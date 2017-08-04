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

package com.tenxdev.jsinterop.generator.parsing;

import com.tenxdev.jsinterop.generator.logging.Logger;
import com.tenxdev.jsinterop.generator.processing.TypeFactory;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.antlr.v4.runtime.tree.Trees;

public class ParsingContext {

    private final TypeFactory typeFactory;
    private final Logger logger;
    private String packageSuffix;
    private Parser parser;

    public ParsingContext(Logger logger) {
        this.logger = logger;
        this.typeFactory = new TypeFactory(logger);
    }

    public TypeFactory getTypeFactory() {
        return typeFactory;
    }

    public Logger getlogger() {
        return logger;
    }

    public String getPackageSuffix() {
        return packageSuffix;
    }

    public void setPackageSuffix(String packageSuffix) {
        this.packageSuffix = packageSuffix;
    }

    public void setParser(Parser parser) {
        this.parser = parser;
    }

    public String getTree(ParseTree ctx) {
        return Trees.toStringTree(ctx, parser);
    }

    public static String getText(ParseTree ctx) {
        if (ctx instanceof TerminalNode) {
            return ctx.getText();
        }
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < ctx.getChildCount(); i++) {
            builder.append(getText(ctx.getChild(i))).append(' ');
        }
        return cleanup(builder.toString());
    }

    private static String cleanup(String value) {
        return value.trim()
                .replaceAll("\\s+\\(","(")
                .replaceAll("\\s+"," ")
                .replaceAll("\\(\\s+","(")
                .replaceAll("\\s+\\)",")")
                .replaceAll("<\\s+","<")
                .replaceAll("\\s+>",">")
                .replaceAll("\\s+=\\s+","=")
                .replaceAll("\\s+,",",");
    }



}
