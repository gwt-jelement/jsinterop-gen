package com.tenxdev.jsinterop.generator.parsing.visitors.secondpass;

import com.tenxdev.jsinterop.generator.model.types.Type;
import com.tenxdev.jsinterop.generator.parsing.ParsingContext;
import org.antlr4.webidl.WebIDLParser;

public class TypeVisitor extends ContextWebIDLBaseVisitor<Type>{

    public TypeVisitor(ParsingContext parsingContext) {
        super(parsingContext);
    }

    @Override
    public Type visitType(WebIDLParser.TypeContext ctx) {
        if (ctx.singleType()!=null){
            return parsingContetxt.getTypeFactory().getType(ctx.singleType().getText());
        }else if (ctx.arrayType()!=null){
            return parsingContetxt.getTypeFactory().getType(ctx.arrayType().getText());
        }else if (ctx.unionType()!=null){
            return ctx.unionType().accept(this);
        }else {
            parsingContetxt.getErrorReporter().reportError("Unexpected state in visitType");
            return null;
        }
    }

    @Override
    public Type visitUnionType(WebIDLParser.UnionTypeContext ctx) {
        String type=ctx.unionMemberType(0).getText()+'|'+ctx.unionMemberType(1).getText();
        if (ctx.unionMemberTypes()!=null){
            type+=getType(ctx.unionMemberTypes());
        }
        return parsingContetxt.getTypeFactory().getUnionType(type.split("\\|"));
    }


    private String getType(WebIDLParser.UnionMemberTypesContext ctx) {
        String type="";
        WebIDLParser.UnionMemberTypesContext context = ctx;
        while(context!=null && context.unionMemberType()!=null){
            type+="|"+context.unionMemberType().getText();
            context=context.unionMemberTypes();
        }
        return type;
    }
}
