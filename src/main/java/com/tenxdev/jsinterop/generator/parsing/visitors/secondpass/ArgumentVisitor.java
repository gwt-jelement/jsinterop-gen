package com.tenxdev.jsinterop.generator.parsing.visitors.secondpass;

import com.tenxdev.jsinterop.generator.model.MethodArgument;
import com.tenxdev.jsinterop.generator.model.types.Type;
import com.tenxdev.jsinterop.generator.parsing.ParsingContext;
import org.antlr4.webidl.WebIDLParser;

public class ArgumentVisitor extends ContextWebIDLBaseVisitor<MethodArgument> {

    ArgumentVisitor(ParsingContext parsingContext) {
        super(parsingContext);
    }

    @Override
    public MethodArgument visitOptionalArgument(WebIDLParser.OptionalArgumentContext ctx) {
        String name = ctx.argumentName().getText();
        Type type = ctx.type().accept(new TypeVisitor(parsingContetxt));
        String defaultValue = ctx.default_() == null || ctx.default_().defaultValue() == null ? null :
                ctx.default_().defaultValue().getText();
        return new MethodArgument(name, type, false, true, defaultValue);
    }

    @Override
    public MethodArgument visitRequiredArgument(WebIDLParser.RequiredArgumentContext ctx) {
        String name = ctx.argumentName().getText();
        Type type = ctx.type().accept(new TypeVisitor(parsingContetxt));
        return new MethodArgument(name, type, false, false, null);
    }

    @Override
    public MethodArgument visitRequiredVarArgArgument(WebIDLParser.RequiredVarArgArgumentContext ctx) {
        String name = ctx.argumentName().getText();
        Type type = ctx.type().accept(new TypeVisitor(parsingContetxt));
        return new MethodArgument(name, type, true, false, null);
    }

}
