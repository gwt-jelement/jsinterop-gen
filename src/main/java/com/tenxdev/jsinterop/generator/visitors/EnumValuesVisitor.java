package com.tenxdev.jsinterop.generator.visitors;

import org.antlr4.webidl.WebIDLBaseVisitor;
import org.antlr4.webidl.WebIDLParser;

import java.util.ArrayList;
import java.util.List;

public class EnumValuesVisitor extends WebIDLBaseVisitor<List<String>> {

    @Override
    public List<String> visitEnumValueList(WebIDLParser.EnumValueListContext ctx) {
        List<String> values = new ArrayList<>();
        values.add(ctx.STRING_WEBIDL().getText());
        if (ctx.enumValueListComma() != null) {
            values.addAll(ctx.enumValueListComma().accept(this));
        }
        return values;
    }

    @Override
    public List<String> visitEnumValueListComma(WebIDLParser.EnumValueListCommaContext ctx) {
        List<String> values = new ArrayList<>();
        WebIDLParser.EnumValueListCommaContext context = ctx;
        while (context!=null && context.enumValueListString() != null && context.enumValueListString().STRING_WEBIDL()!=null) {
            values.add(context.enumValueListString().STRING_WEBIDL().getText());
            context = context.enumValueListString().enumValueListComma();
        }
        return values;
    }
}

