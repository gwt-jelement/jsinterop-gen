package com.tenxdev.jsinterop.generator.parsing.visitors.secondpass;

import com.tenxdev.jsinterop.generator.model.Method;
import com.tenxdev.jsinterop.generator.model.MethodArgument;
import com.tenxdev.jsinterop.generator.model.types.Type;
import com.tenxdev.jsinterop.generator.parsing.ParsingContext;
import org.antlr4.webidl.WebIDLParser;

import java.util.Collections;
import java.util.List;

class OperationRestVisitor extends ContextWebIDLBaseVisitor<Method> {

    private final Type returnType;
    private final boolean staticMethod;

    public OperationRestVisitor(ParsingContext context, Type returnType, boolean staticMethod) {
        super(context);
        this.returnType = returnType;
        this.staticMethod = staticMethod;
    }

    @Override
    public Method visitOperationRest(WebIDLParser.OperationRestContext ctx) {
        String name = ctx.optionalIdentifier().getText();
        List<MethodArgument> parameters = ctx.argumentList() == null || ctx.argumentList().argument() == null ? Collections.emptyList() :
                ctx.argumentList().accept(new ArgumentsVisitor(parsingContext));
        return new Method(name, returnType, parameters, staticMethod, false, null);
    }
}
