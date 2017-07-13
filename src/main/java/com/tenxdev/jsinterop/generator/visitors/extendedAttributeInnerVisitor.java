package com.tenxdev.jsinterop.generator.visitors;

import org.antlr4.webidl.WebIDLBaseVisitor;
import org.antlr4.webidl.WebIDLParser;

public class extendedAttributeInnerVisitor extends WebIDLBaseVisitor<String>{

    @Override
    public String visitOtherOrComma(WebIDLParser.OtherOrCommaContext ctx) {
        return ctx.getText();
    }

    @Override
    public String visitExtendedAttributeInner(WebIDLParser.ExtendedAttributeInnerContext ctx) {
        return ctx. otherOrComma()!=null?ctx.otherOrComma().accept(this):null;
    }

}
