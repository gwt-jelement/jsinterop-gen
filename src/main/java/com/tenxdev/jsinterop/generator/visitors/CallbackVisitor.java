package com.tenxdev.jsinterop.generator.visitors;

import com.tenxdev.jsinterop.generator.model.CallbackDefinition;
import com.tenxdev.jsinterop.generator.model.Definition;
import com.tenxdev.jsinterop.generator.model.Method;
import com.tenxdev.jsinterop.generator.model.MethodArgument;
import com.tenxdev.jsinterop.generator.processing.TypeUtil;
import org.antlr4.webidl.WebIDLBaseVisitor;
import org.antlr4.webidl.WebIDLParser;

import java.util.Collections;
import java.util.List;

public class CallbackVisitor extends WebIDLBaseVisitor<Definition> {

    private final List<Method> constructors;

    public CallbackVisitor(List<Method> constructors) {
        this.constructors = constructors;
    }

    @Override
    public Definition visitCallbackRestOrInterface(WebIDLParser.CallbackRestOrInterfaceContext ctx) {
        if (ctx.interface_() != null) {
            return ctx.interface_().accept(new InterfaceVisitor(constructors));
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
        String[] returnTypes = TypeUtil.INSTANCE.removeOptionalIndicator(ctx.returnType().accept(new TypeVisitor()));
        List<MethodArgument> arguments = ctx.argumentList() != null && ctx.argumentList().arguments() != null ?
                ctx.argumentList().accept(new ArgumentsVisitor()) : Collections.emptyList();
        Method method = new Method(null, returnTypes, arguments, false);
        return new CallbackDefinition(name, method);
    }
}
