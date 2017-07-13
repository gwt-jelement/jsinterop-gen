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
