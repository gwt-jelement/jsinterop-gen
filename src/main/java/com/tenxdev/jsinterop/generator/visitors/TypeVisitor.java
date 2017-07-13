package com.tenxdev.jsinterop.generator.visitors;

import org.antlr4.webidl.WebIDLBaseVisitor;
import org.antlr4.webidl.WebIDLParser;

public class TypeVisitor extends WebIDLBaseVisitor<String>{

    @Override
    public String visitType(WebIDLParser.TypeContext ctx) {
        if (ctx.singleType()!=null){
            return ctx.singleType().getText();
        }else if (ctx.arrayType()!=null){
            return ctx.arrayType().getText()+"[]";
        }else if (ctx.unionType()!=null){
            return ctx.unionType().accept(this);
        }else {
            System.err.println("Unexpected tate in visitType");
            return null;
        }
    }

    @Override
    public String visitUnionType(WebIDLParser.UnionTypeContext ctx) {
        String type=ctx.unionMemberType(0).getText()+'|'+ctx.unionMemberType(1).getText();
        if (ctx.unionMemberTypes()!=null){
            type+=ctx.unionMemberTypes().accept(this);
        }
        return type;
    }

    @Override
    public String visitUnionMemberTypes(WebIDLParser.UnionMemberTypesContext ctx) {
        String type="";
        WebIDLParser.UnionMemberTypesContext context = ctx;
        while(context!=null && context.unionMemberType()!=null){
            type+="|"+context.unionMemberType().getText();
            context=context.unionMemberTypes();
        }
        return type;
    }
}
