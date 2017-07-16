package com.tenxdev.jsinterop.generator.visitors;

import com.tenxdev.jsinterop.generator.model.MethodArgument;
import com.tenxdev.jsinterop.generator.processing.TypeUtil;
import org.antlr4.webidl.WebIDLBaseVisitor;
import org.antlr4.webidl.WebIDLParser;

public class ArgumentVisitor extends WebIDLBaseVisitor<MethodArgument> {

    @Override
    public MethodArgument visitOptionalArgument(WebIDLParser.OptionalArgumentContext ctx) {
        String name = ctx.argumentName().getText();
        String types[]= TypeUtil.INSTANCE.removeOptionalIndicator(ctx.type().accept(new TypeVisitor()));
        String defaultValue=ctx.default_()==null || ctx.default_().defaultValue()==null?null:
                ctx.default_().defaultValue().getText();
        return new MethodArgument(name, types, false, true, defaultValue);
    }

    @Override
    public MethodArgument visitRequiredArgument(WebIDLParser.RequiredArgumentContext ctx) {
        String name = ctx.argumentName().getText();
        String types[]=ctx.type().accept(new TypeVisitor());
        boolean optional=false;
        if (TypeUtil.INSTANCE.hasOptioanlMarker(types)){
            types=TypeUtil.INSTANCE.removeOptionalIndicator(types);
            optional=true;
        }
        return new MethodArgument(name, types, false, optional, null);
    }

    @Override
    public MethodArgument visitRequiredVarArgArgument(WebIDLParser.RequiredVarArgArgumentContext ctx) {
        String name = ctx.argumentName().getText();
        String types[]=ctx.type().accept(new TypeVisitor());
        boolean optional=false;
        if (TypeUtil.INSTANCE.hasOptioanlMarker(types)){
            types=TypeUtil.INSTANCE.removeOptionalIndicator(types);
            optional=true;
        }
        return new MethodArgument(name, types, true, optional, null);
    }

}
