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

package com.tenxdev.jsinterop.generator.processing;

import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;

public final class ParserUtil {

    private ParserUtil() {
    }

    public static String getText(ParseTree ctx) {
        if (ctx instanceof TerminalNode) {
            return ctx.getText();
        }
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < ctx.getChildCount(); i++) {
            builder.append(getText(ctx.getChild(i))).append(' ');
        }
        return builder.toString();
    }

}
