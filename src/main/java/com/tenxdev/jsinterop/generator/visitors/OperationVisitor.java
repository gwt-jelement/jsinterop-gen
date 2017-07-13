package com.tenxdev.jsinterop.generator.visitors;

import com.tenxdev.jsinterop.generator.model.Method;
import com.tenxdev.jsinterop.generator.model.MethodArgument;
import org.antlr4.webidl.WebIDLBaseVisitor;
import org.antlr4.webidl.WebIDLParser;

import java.util.Collections;
import java.util.List;

public class OperationVisitor extends WebIDLBaseVisitor<Method> {

    @Override
    public Method visitOperation(WebIDLParser.OperationContext ctx) {
        if (ctx.specialOperation() != null) {
            String returnType = ctx.specialOperation().returnType().getText();
            String name =
                    ctx.specialOperation().IDENTIFIER_WEBIDL() != null && ctx.specialOperation().IDENTIFIER_WEBIDL().getText() != null ?
                            ctx.specialOperation().IDENTIFIER_WEBIDL().getText() :
                            ctx.specialOperation().special().getText();
            List<MethodArgument> parameters = ctx.specialOperation().argumentList() == null || ctx.specialOperation().argumentList().argument() == null ? Collections.emptyList() :
                    ctx.specialOperation().argumentList().accept(new ArgumentsVisitor());
            return new Method(name, returnType, parameters, false);
        } else if (ctx.operationRest() != null) {
            String returnType = ctx.returnType().getText();
            return ctx.operationRest().accept(new OperationRestVisitor(returnType, false));
        } else {
            System.err.println("Unexpected condition in operation");
            return null;
        }

    }
}


