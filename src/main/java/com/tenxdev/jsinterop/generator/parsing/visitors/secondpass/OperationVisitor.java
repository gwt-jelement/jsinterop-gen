package com.tenxdev.jsinterop.generator.parsing.visitors.secondpass;

import com.tenxdev.jsinterop.generator.model.Method;
import com.tenxdev.jsinterop.generator.model.types.Type;
import com.tenxdev.jsinterop.generator.parsing.ParsingContext;
import org.antlr4.webidl.WebIDLParser;

public class OperationVisitor extends ContextWebIDLBaseVisitor<Method> {

    private final String containingType;

    public OperationVisitor(ParsingContext context, String containingType) {
        super(context);
        this.containingType = containingType;
    }

    @Override
    public Method visitOperation(WebIDLParser.OperationContext ctx) {
        if (ctx.specialOperation() != null) {
            //FIXME, definition below is not correct
//            Type returnType = ctx.specialOperation().returnType().accept(new TypeVisitor(parsingContext));
//            String name =
//                    ctx.specialOperation().IDENTIFIER_WEBIDL() != null &&
//                            ctx.specialOperation().IDENTIFIER_WEBIDL().getText() != null ?
//                            ctx.specialOperation().IDENTIFIER_WEBIDL().getText() :
//                            ctx.specialOperation().special().getText();
//            List<MethodArgument> parameters = ctx.specialOperation().argumentList() == null ||
//                    ctx.specialOperation().argumentList().argument() == null ?
//                    Collections.emptyList() :
//                    ctx.specialOperation().argumentList().accept(new ArgumentsVisitor(parsingContext));
//            return new Method(name, returnType, parameters, false);
            return null;
        } else if (ctx.operationRest() != null) {
            Type returnType = ctx.returnType().accept(new TypeVisitor(parsingContext));
            return ctx.operationRest().accept(new OperationRestVisitor(parsingContext, returnType, false));
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


