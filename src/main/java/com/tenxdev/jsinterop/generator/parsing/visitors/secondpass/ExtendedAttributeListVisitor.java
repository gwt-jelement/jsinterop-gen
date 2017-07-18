package com.tenxdev.jsinterop.generator.parsing.visitors.secondpass;

import com.tenxdev.jsinterop.generator.model.Method;
import com.tenxdev.jsinterop.generator.parsing.ParsingContext;
import org.antlr4.webidl.WebIDLParser;

import java.util.ArrayList;
import java.util.List;

/**
 * finds a constructor definition in the extended attribute list for a definition
 */
public class ExtendedAttributeListVisitor extends ContextWebIDLBaseVisitor<List<Method>> {

    public ExtendedAttributeListVisitor(ParsingContext parsingContext) {
        super(parsingContext);
    }

    @Override
    public List<Method> visitExtendedAttributeList(WebIDLParser.ExtendedAttributeListContext ctx) {
        List<Method> constructors = new ArrayList<>();
        if (ctx.extendedAttribute() != null) {
            Method constructor = getConstructor(ctx.extendedAttribute());
            if (constructor != null) {
                constructors.add(constructor);
            }
        }
        if (ctx.extendedAttributes() != null) {
            constructors.addAll(ctx.extendedAttributes().accept(this));
        }
        return constructors;
    }

    private Method getConstructor(WebIDLParser.ExtendedAttributeContext ctx) {
        if (ctx.other() != null && "Constructor".equals(ctx.other().getText())) {
            return ctx.extendedAttributeRest().accept(new ConstructorVisitor(parsingContext));
        }
        return null;
    }

    @Override
    public List<Method> visitExtendedAttributes(WebIDLParser.ExtendedAttributesContext ctx) {
        List<Method> constructors = new ArrayList<>();
        if (ctx.extendedAttribute() != null) {
            Method constructor = getConstructor(ctx.extendedAttribute());
            if (constructor != null) {
                constructors.add(constructor);
            }
        }
        if (ctx.extendedAttributes() != null) {
            constructors.addAll(ctx.extendedAttributes().accept(this));
        }
        return constructors;
    }
}
