package com.tenxdev.jsinterop.generator.parsing.visitors.secondpass;

import com.tenxdev.jsinterop.generator.model.Method;
import com.tenxdev.jsinterop.generator.model.MethodArgument;
import com.tenxdev.jsinterop.generator.parsing.ParsingContext;
import org.antlr4.webidl.WebIDLParser;

import java.util.Collections;
import java.util.List;

class ConstructorVisitor extends ContextWebIDLBaseVisitor<Method> {

    public ConstructorVisitor(ParsingContext parsingContext) {
        super(parsingContext);
    }

    @Override
    public Method visitExtendedAttributeRest(WebIDLParser.ExtendedAttributeRestContext ctx) {
        if (ctx.extendedAttribute() == null) {
            return new Method("", null, Collections.emptyList(), false, false, null);
        }
        return ctx.extendedAttribute().accept(this);
    }

    @Override
    public Method visitExtendedAttribute(WebIDLParser.ExtendedAttributeContext ctx) {
        List<MethodArgument> arguments = ctx.extendedAttributeInner() != null ?
                ctx.extendedAttributeInner().accept(new ConstructorArgumentsVisitor(parsingContext)) :
                Collections.emptyList();
        return new Method("", null, arguments, false, false, null);
    }
}
