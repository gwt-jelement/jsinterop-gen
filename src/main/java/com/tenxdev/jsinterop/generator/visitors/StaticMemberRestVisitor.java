package com.tenxdev.jsinterop.generator.visitors;

import com.tenxdev.jsinterop.generator.model.InterfaceMember;
import com.tenxdev.jsinterop.generator.processing.TypeUtil;
import org.antlr4.webidl.WebIDLBaseVisitor;
import org.antlr4.webidl.WebIDLParser;

public class StaticMemberRestVisitor extends WebIDLBaseVisitor<InterfaceMember>{

    @Override
    public InterfaceMember visitStaticMemberRest(WebIDLParser.StaticMemberRestContext ctx) {
        if (ctx.attributeRest()!=null){
            boolean readOnly = !ctx.readOnly().isEmpty();
            return ctx.attributeRest().accept(new AttributeRestVisitor(readOnly, true));
        }
        if (ctx.operationRest()!=null){
            String[] returnTypes = TypeUtil.INSTANCE.removeOptionalIndicator(ctx.returnType().accept(new TypeVisitor()));
            return ctx.operationRest().accept(new OperationRestVisitor(returnTypes, true));
        }
        System.err.println("Unexpected state in StaticMemberRest");
        return null;
    }
}
