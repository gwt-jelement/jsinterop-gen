package com.tenxdev.jsinterop.generator.visitors;

import com.tenxdev.jsinterop.generator.model.Method;
import com.tenxdev.jsinterop.generator.model.MethodArgument;
import org.antlr4.webidl.WebIDLBaseVisitor;
import org.antlr4.webidl.WebIDLParser;

import java.util.Collections;
import java.util.List;

public class ConstructorVisitor extends WebIDLBaseVisitor<Method> {
    @Override
    public Method visitExtendedAttributeRest(WebIDLParser.ExtendedAttributeRestContext ctx) {
        if (ctx.extendedAttribute() == null) {
            return new Method("", null, Collections.emptyList(), false);
        }
        return ctx.extendedAttribute().accept(this);
    }

    @Override
    public Method visitExtendedAttribute(WebIDLParser.ExtendedAttributeContext ctx) {
        List<MethodArgument> arguments = ctx.extendedAttributeInner() != null ?
                ctx.extendedAttributeInner().accept(new ConstructorArgumentsVisitor()) : Collections.emptyList();
        return new Method("", null, arguments, false);
    }
}
