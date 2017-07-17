package com.tenxdev.jsinterop.generator.parsing.visitors.secondpass;

import com.tenxdev.jsinterop.generator.model.InterfaceMember;
import com.tenxdev.jsinterop.generator.model.types.Type;
import com.tenxdev.jsinterop.generator.parsing.ParsingContext;
import com.tenxdev.jsinterop.generator.processing.TypeUtil;
import org.antlr4.webidl.WebIDLBaseVisitor;
import org.antlr4.webidl.WebIDLParser;

public class StaticMemberRestVisitor extends ContextWebIDLBaseVisitor<InterfaceMember>{

    public StaticMemberRestVisitor(ParsingContext parsingContext) {
        super(parsingContext);
    }

    @Override
    public InterfaceMember visitStaticMemberRest(WebIDLParser.StaticMemberRestContext ctx) {
        if (ctx.attributeRest()!=null){
            boolean readOnly = !ctx.readOnly().isEmpty();
            return ctx.attributeRest().accept(new AttributeRestVisitor(parsingContetxt,readOnly, true));
        }
        if (ctx.operationRest()!=null){
            Type returnType = ctx.returnType().accept(new TypeVisitor(parsingContetxt));
            return ctx.operationRest().accept(new OperationRestVisitor(parsingContetxt,returnType, true));
        }
        System.err.println("Unexpected state in StaticMemberRest");
        return null;
    }
}
