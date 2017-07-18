package com.tenxdev.jsinterop.generator.parsing.visitors.secondpass;

import com.tenxdev.jsinterop.generator.model.InterfaceMember;
import com.tenxdev.jsinterop.generator.model.types.Type;
import com.tenxdev.jsinterop.generator.parsing.ParsingContext;
import org.antlr4.webidl.WebIDLParser;

class StaticMemberRestVisitor extends ContextWebIDLBaseVisitor<InterfaceMember> {

    StaticMemberRestVisitor(ParsingContext parsingContext) {
        super(parsingContext);
    }

    @Override
    public InterfaceMember visitStaticMemberRest(WebIDLParser.StaticMemberRestContext ctx) {
        if (ctx.attributeRest() != null) {
            boolean readOnly = !ctx.readOnly().isEmpty();
            return ctx.attributeRest().accept(new AttributeRestVisitor(parsingContext, readOnly, true));
        }
        if (ctx.operationRest() != null) {
            Type returnType = ctx.returnType().accept(new TypeVisitor(parsingContext));
            return ctx.operationRest().accept(new OperationRestVisitor(parsingContext, returnType, true));
        }
        parsingContext.getErrorReporter().reportError("Unexpected state in StaticMemberRest");
        return null;
    }
}
