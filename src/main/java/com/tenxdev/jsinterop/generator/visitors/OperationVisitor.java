package com.tenxdev.jsinterop.generator.visitors;

import com.tenxdev.jsinterop.generator.model.Method;
import com.tenxdev.jsinterop.generator.model.MethodArgument;
import com.tenxdev.jsinterop.generator.processing.TypeUtil;
import org.antlr4.webidl.WebIDLBaseVisitor;
import org.antlr4.webidl.WebIDLParser;

import java.util.Collections;
import java.util.List;

public class OperationVisitor extends WebIDLBaseVisitor<Method> {

    private final String containingType;

    public OperationVisitor(String containingType) {
        this.containingType = containingType;
    }

    @Override
    public Method visitOperation(WebIDLParser.OperationContext ctx) {
        if (ctx.specialOperation() != null) {
            String returnTypes[] = TypeUtil.INSTANCE.removeOptionalIndicator(ctx.specialOperation().returnType().accept(new TypeVisitor()));
            String name =
                    ctx.specialOperation().IDENTIFIER_WEBIDL() != null && ctx.specialOperation().IDENTIFIER_WEBIDL().getText() != null ?
                            ctx.specialOperation().IDENTIFIER_WEBIDL().getText() :
                            ctx.specialOperation().special().getText();
            List<MethodArgument> parameters = ctx.specialOperation().argumentList() == null || ctx.specialOperation().argumentList().argument() == null ? Collections.emptyList() :
                    ctx.specialOperation().argumentList().accept(new ArgumentsVisitor());
            return new Method(name, returnTypes, parameters, false);
        } else if (ctx.operationRest() != null) {
            String[] returnTypes = TypeUtil.INSTANCE.removeOptionalIndicator(ctx.returnType().accept(new TypeVisitor()));
            return ctx.operationRest().accept(new OperationRestVisitor(returnTypes, false));
        } else {
            System.err.println("Unexpected condition in operation");
            return null;
        }

    }

    private String removeOptionalIndicator(String type) {
        if (type != null && type.endsWith("?")) {
            return type.substring(0, type.length() - 1);
        }
        return type;
    }
}


