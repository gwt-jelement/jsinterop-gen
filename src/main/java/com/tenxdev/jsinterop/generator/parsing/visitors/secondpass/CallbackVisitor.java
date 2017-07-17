package com.tenxdev.jsinterop.generator.parsing.visitors.secondpass;

import com.tenxdev.jsinterop.generator.model.CallbackDefinition;
import com.tenxdev.jsinterop.generator.model.Definition;
import com.tenxdev.jsinterop.generator.model.Method;
import com.tenxdev.jsinterop.generator.model.MethodArgument;
import com.tenxdev.jsinterop.generator.model.types.Type;
import com.tenxdev.jsinterop.generator.parsing.ParsingContext;
import com.tenxdev.jsinterop.generator.processing.TypeUtil;
import org.antlr4.webidl.WebIDLBaseVisitor;
import org.antlr4.webidl.WebIDLParser;

import java.util.Collections;
import java.util.List;

public class CallbackVisitor extends ContextWebIDLBaseVisitor<Definition> {

    private final List<Method> constructors;

    public CallbackVisitor(ParsingContext context,List<Method> constructors) {
        super(context);
        this.constructors = constructors;
    }

    @Override
    public Definition visitCallbackRestOrInterface(WebIDLParser.CallbackRestOrInterfaceContext ctx) {
        if (ctx.interface_() != null) {
            return ctx.interface_().accept(new InterfaceVisitor(parsingContetxt, constructors));
        } else if (ctx.callbackRest() != null) {
            return ctx.callbackRest().accept(this);
        } else {
            System.err.println("unexpected state in CallbackRestOrInterface visitor");
            System.err.println("Content: " + ctx.getText());
            return null;
        }
    }

    @Override
    public CallbackDefinition visitCallbackRest(WebIDLParser.CallbackRestContext ctx) {
        String name = ctx.IDENTIFIER_WEBIDL().getText();
        Type returnType = ctx.returnType().accept(new TypeVisitor(parsingContetxt));
        List<MethodArgument> arguments = ctx.argumentList() != null && ctx.argumentList().arguments() != null ?
                ctx.argumentList().accept(new ArgumentsVisitor(parsingContetxt)) : Collections.emptyList();
        Method method = new Method(null, returnType, arguments, false);
        return new CallbackDefinition(name, method);
    }
}
