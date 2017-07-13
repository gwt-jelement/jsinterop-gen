package com.tenxdev.jsinterop.generator.visitors;

import com.tenxdev.jsinterop.generator.model.Method;
import org.antlr4.webidl.WebIDLBaseVisitor;
import org.antlr4.webidl.WebIDLParser;

import java.util.ArrayList;
import java.util.List;

/**
 * finds a constructor definition in the extended attrubute list for a definition
 */
public class ExtendedAttributeListVisitor extends WebIDLBaseVisitor<List<Method>>{

    @Override
    public List<Method> visitExtendedAttributeList(WebIDLParser.ExtendedAttributeListContext ctx) {
        List<Method> constructors=new ArrayList<>();
        if (ctx.extendedAttribute()!=null){
            Method constructor = getConstructor(ctx.extendedAttribute());
            if (constructor!=null){
                constructors.add(constructor);
            }
        }
        if (ctx.extendedAttributes()!=null){
            constructors.addAll(ctx.extendedAttributes().accept(this));
        }
        return constructors;
    }

    private Method getConstructor(WebIDLParser.ExtendedAttributeContext ctx) {
        if (ctx.other()!=null && "Constructor".equals(ctx.other().getText())){
            return ctx.extendedAttributeRest().accept(new ConstructorVisitor());
        }
        return null;
    }

    @Override
    public List<Method> visitExtendedAttributes(WebIDLParser.ExtendedAttributesContext ctx) {
        List<Method> constructors=new ArrayList<>();
        if (ctx.extendedAttribute()!=null){
            Method constructor = getConstructor(ctx.extendedAttribute());
            if (constructor!=null){
                constructors.add(constructor);
            }
        }
        if (ctx.extendedAttributes()!=null){
            constructors.addAll(ctx.extendedAttributes().accept(this));
        }
        return constructors;
    }
}
