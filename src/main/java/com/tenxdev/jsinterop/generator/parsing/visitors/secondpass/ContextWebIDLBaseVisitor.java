package com.tenxdev.jsinterop.generator.parsing.visitors.secondpass;

import com.tenxdev.jsinterop.generator.parsing.ParsingContext;
import org.antlr4.webidl.WebIDLBaseVisitor;

public class ContextWebIDLBaseVisitor<T> extends WebIDLBaseVisitor<T> {

    protected final ParsingContext parsingContext;

    protected ContextWebIDLBaseVisitor(ParsingContext parsingContext) {
        this.parsingContext = parsingContext;
    }
}
