package com.tenxdev.jsinterop.generator.parsing.visitors.secondpass;

import com.tenxdev.jsinterop.generator.model.CallbackDefinition;
import com.tenxdev.jsinterop.generator.model.Definition;
import com.tenxdev.jsinterop.generator.model.Method;
import com.tenxdev.jsinterop.generator.model.MethodArgument;
import com.tenxdev.jsinterop.generator.model.types.Type;
import com.tenxdev.jsinterop.generator.parsing.ParsingContext;
import com.tenxdev.jsinterop.generator.processing.ParserUtil;
import org.antlr4.webidl.WebIDLParser;

import java.util.Collections;
import java.util.List;

class CallbackVisitor extends ContextWebIDLBaseVisitor<Definition> {

    private final List<Method> constructors;

    CallbackVisitor(ParsingContext context, List<Method> constructors) {
        super(context);
        this.constructors = constructors;
    }

    @Override
    public Definition visitCallbackRestOrInterface(WebIDLParser.CallbackRestOrInterfaceContext ctx) {
        if (ctx.interface_() != null) {
            return ctx.interface_().accept(new InterfaceVisitor(parsingContext, constructors));
        } else if (ctx.callbackRest() != null) {
            return ctx.callbackRest().accept(this);
        } else {
            parsingContext.getlogger().reportError("unexpected state in CallbackRestOrInterface visitor");
            parsingContext.getlogger().reportError("Content: " + ParserUtil.getText(ctx));
            return null;
        }
    }

    @Override
    public CallbackDefinition visitCallbackRest(WebIDLParser.CallbackRestContext ctx) {
        String name = ctx.IDENTIFIER_WEBIDL().getText();
        Type returnType = ctx.returnType().accept(new TypeVisitor(parsingContext));
        List<MethodArgument> arguments = ctx.argumentList() != null && ctx.argumentList().arguments() != null ?
                ctx.argumentList().accept(new ArgumentsVisitor(parsingContext)) : Collections.emptyList();
        Method method = new Method(null, returnType, arguments, false, false, null);
        return new CallbackDefinition(name, method);
    }
}
