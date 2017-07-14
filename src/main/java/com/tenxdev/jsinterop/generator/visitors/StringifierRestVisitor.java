package com.tenxdev.jsinterop.generator.visitors;

import com.tenxdev.jsinterop.generator.model.InterfaceMember;
import com.tenxdev.jsinterop.generator.model.Method;
import com.tenxdev.jsinterop.generator.processing.TypeUtil;
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
            String[] returnTypes = TypeUtil.INSTANCE.removeOptionalIndicator(ctx.returnType().accept(new TypeVisitor()));
            return ctx.operationRest().accept(new OperationRestVisitor(returnTypes, false));
        }else{
            return new Method("toString",new String[]{"DOMString"}, Collections.emptyList(),false);
        }
    }
}
