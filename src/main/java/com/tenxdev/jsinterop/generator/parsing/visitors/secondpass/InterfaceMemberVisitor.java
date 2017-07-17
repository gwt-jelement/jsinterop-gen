package com.tenxdev.jsinterop.generator.parsing.visitors.secondpass;

import com.tenxdev.jsinterop.generator.model.InterfaceMember;
import com.tenxdev.jsinterop.generator.model.Method;
import com.tenxdev.jsinterop.generator.parsing.ParsingContext;
import org.antlr4.webidl.WebIDLParser;

import java.util.Collections;

public class InterfaceMemberVisitor extends ContextWebIDLBaseVisitor<InterfaceMember>{

    private final String containingType;

    public InterfaceMemberVisitor(ParsingContext context, String containingType){
        super(context);
        this.containingType=containingType;
    }

    @Override
    public InterfaceMember visitInterfaceMember(WebIDLParser.InterfaceMemberContext ctx) {
        if (ctx.operation()!=null){
            return ctx.operation().accept(new OperationVisitor(parsingContetxt, containingType));
        }
        if (ctx.const_()!=null){
            return ctx.const_().accept(new ConstantVisitor(parsingContetxt));
        }
        if (ctx.serializer()!=null){
            return new Method("toJSON",parsingContetxt.getTypeFactory().getType("any"),
                    Collections.emptyList(),false);
        }
        if (ctx.stringifier()!=null){
            return ctx.stringifier().stringifierRest().accept(new StringifierRestVisitor(parsingContetxt));
        }
        if (ctx.staticMember()!=null){
            return ctx.staticMember().staticMemberRest().accept(new StaticMemberRestVisitor(parsingContetxt));
        }
        if (ctx.readonlyMember()!=null){
            return ctx.readonlyMember().readonlyMemberRest().accept(new ReadOlyMemberRestVisitor(parsingContetxt));
        }
        if (ctx.readWriteAttribute()!=null){
            boolean readOnly = ctx.readWriteAttribute().readOnly() == null || !ctx.readWriteAttribute().readOnly().isEmpty();
            return ctx.readWriteAttribute().attributeRest().accept(new AttributeRestVisitor(parsingContetxt, readOnly, false));
        }
        if (ctx.readWriteMaplike()!=null){
            return ctx.readWriteMaplike().maplikeRest().accept(new MapLikeRestVisitor(parsingContetxt, false));
        }
        if (ctx.readWriteSetlike()!=null){
            return ctx.readWriteSetlike().setlikeRest().accept(new SetLikeRestVisitor(parsingContetxt, false));
        }
        if (ctx.iterable()!=null){
            return ctx.iterable().accept(new IterableVisitor(parsingContetxt));
        }
        parsingContetxt.getErrorReporter().reportError("Unexpected state in InterfaceMmebers");
        return null;
    }
}
