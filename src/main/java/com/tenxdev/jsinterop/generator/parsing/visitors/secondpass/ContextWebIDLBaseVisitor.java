package com.tenxdev.jsinterop.generator.parsing.visitors.secondpass;

import com.tenxdev.jsinterop.generator.parsing.ParsingContext;
import org.antlr4.webidl.WebIDLBaseVisitor;

public class ContextWebIDLBaseVisitor<T> extends WebIDLBaseVisitor<T> {

    protected final ParsingContext parsingContetxt;

    public ContextWebIDLBaseVisitor(ParsingContext parsingContext){
        this.parsingContetxt=parsingContext;
    }
}
