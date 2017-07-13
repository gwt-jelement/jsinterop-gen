package com.tenxdev.jsinterop.generator.visitors;

import com.tenxdev.jsinterop.generator.model.InterfaceMember;
import com.tenxdev.jsinterop.generator.model.Method;
import org.antlr4.webidl.WebIDLBaseVisitor;
import org.antlr4.webidl.WebIDLParser;

import java.util.Collections;

public class StringifierRestVisitor extends WebIDLBaseVisitor<InterfaceMember>{

    @Override
    public InterfaceMember visitStringifierRest(WebIDLParser.StringifierRestContext ctx) {
        if (ctx.attributeRest()!=null){
            boolean readOnly=!ctx.readOnly().isEmpty();
            return ctx.attributeRest().accept(new AttributeRestVisitor(readOnly, false));
        }else if (ctx.operationRest()!=null){
            String returnType = ctx.returnType().getText();
            return ctx.operationRest().accept(new OperationRestVisitor(returnType, false));
        }else{
            return new Method("toString","DOMString", Collections.emptyList(),false);
        }
    }
}
