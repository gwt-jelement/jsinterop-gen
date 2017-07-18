package com.tenxdev.jsinterop.generator.parsing.visitors.secondpass;

import com.tenxdev.jsinterop.generator.parsing.ParsingContext;
import org.antlr4.webidl.WebIDLParser;

public class ExtendedAttributeInnerVisitor extends ContextWebIDLBaseVisitor<String> {

    public ExtendedAttributeInnerVisitor(ParsingContext parsingContext) {
        super(parsingContext);
    }

    @Override
    public String visitOtherOrComma(WebIDLParser.OtherOrCommaContext ctx) {
        return ctx.getText();
    }

    @Override
    public String visitExtendedAttributeInner(WebIDLParser.ExtendedAttributeInnerContext ctx) {
        return ctx.otherOrComma() != null ? ctx.otherOrComma().accept(this) : null;
    }

}
