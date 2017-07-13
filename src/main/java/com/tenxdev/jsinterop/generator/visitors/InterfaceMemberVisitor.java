package com.tenxdev.jsinterop.generator.visitors;

import com.tenxdev.jsinterop.generator.model.InterfaceMember;
import com.tenxdev.jsinterop.generator.model.Method;
import org.antlr4.webidl.WebIDLBaseVisitor;
import org.antlr4.webidl.WebIDLParser;

import java.util.Collections;

public class InterfaceMemberVisitor extends WebIDLBaseVisitor<InterfaceMember>{

    @Override
    public InterfaceMember visitInterfaceMember(WebIDLParser.InterfaceMemberContext ctx) {
        if (ctx.operation()!=null){
            return ctx.operation().accept(new OperationVisitor());
        }
        if (ctx.const_()!=null){
            return ctx.const_().accept(new ConstantVisitor());
        }
        if (ctx.serializer()!=null){
            return new Method("toJSON","object", Collections.emptyList(),false);
        }
        if (ctx.stringifier()!=null){
            return ctx.stringifier().stringifierRest().accept(new StringifierRestVisitor());
        }
        if (ctx.staticMember()!=null){
            return ctx.staticMember().staticMemberRest().accept(new StaticMemberRestVisitor());
        }
        if (ctx.readonlyMember()!=null){
            return ctx.readonlyMember().readonlyMemberRest().accept(new ReadOlyMemberRestVisitor());
        }
        if (ctx.readWriteAttribute()!=null){
            boolean readOnly = ctx.readWriteAttribute().readOnly() == null || !ctx.readWriteAttribute().readOnly().isEmpty();
            return ctx.readWriteAttribute().attributeRest().accept(new AttributeRestVisitor(readOnly, false));
        }
        if (ctx.readWriteMaplike()!=null){
            return ctx.readWriteMaplike().maplikeRest().accept(new MapLikeRestVisitor(false));
        }
        if (ctx.readWriteSetlike()!=null){
            return ctx.readWriteSetlike().setlikeRest().accept(new SetLikeRestVisitor(false));
        }
        if (ctx.iterable()!=null){
            return ctx.iterable().accept(new IterableVisitor());
        }
        System.err.println("Unexpected state in InterfaceMmebers");
        return null;
    }
}
