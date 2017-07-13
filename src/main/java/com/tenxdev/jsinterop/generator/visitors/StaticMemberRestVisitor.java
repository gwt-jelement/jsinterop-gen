package com.tenxdev.jsinterop.generator.visitors;

import com.tenxdev.jsinterop.generator.model.InterfaceMember;
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
            String returnType = ctx.returnType().getText();
            return ctx.operationRest().accept(new OperationRestVisitor(returnType, true));
        }
        System.err.println("Unexpected state in StaticMemberRest");
        return null;
    }
}
