package com.tenxdev.jsinterop.generator.visitors;

import com.tenxdev.jsinterop.generator.model.MethodArgument;
import org.antlr4.webidl.WebIDLBaseVisitor;
import org.antlr4.webidl.WebIDLParser;

public class ArgumentVisitor extends WebIDLBaseVisitor<MethodArgument> {

    @Override
    public MethodArgument visitOptionalArgument(WebIDLParser.OptionalArgumentContext ctx) {
        String name = ctx.argumentName().getText();
        String type = ctx.type().accept(new TypeVisitor());
        String defaultValue=ctx.default_()==null || ctx.default_().defaultValue()==null?null:
                ctx.default_().defaultValue().getText();
        return new MethodArgument(name, type, false, true, defaultValue);
    }

    @Override
    public MethodArgument visitRequiredArgument(WebIDLParser.RequiredArgumentContext ctx) {
        String name = ctx.argumentName().getText();
        String type = ctx.type().accept(new TypeVisitor());
        return new MethodArgument(name, type, false, false, null);
    }

    @Override
    public MethodArgument visitRequiredVarArgArgument(WebIDLParser.RequiredVarArgArgumentContext ctx) {
        String name = ctx.argumentName().getText();
        String type = ctx.type().accept(new TypeVisitor());
        return new MethodArgument(name, type, true, false, null);
    }
}
