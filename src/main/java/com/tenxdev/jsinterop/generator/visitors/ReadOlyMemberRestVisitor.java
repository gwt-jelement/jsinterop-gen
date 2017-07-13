package com.tenxdev.jsinterop.generator.visitors;

import com.tenxdev.jsinterop.generator.model.InterfaceMember;
import org.antlr4.webidl.WebIDLBaseVisitor;
import org.antlr4.webidl.WebIDLParser;

public class ReadOlyMemberRestVisitor extends WebIDLBaseVisitor<InterfaceMember> {
    @Override
    public InterfaceMember visitReadonlyMemberRest(WebIDLParser.ReadonlyMemberRestContext ctx) {
        if (ctx.attributeRest() != null) {
            return ctx.attributeRest().accept(new AttributeRestVisitor(true, false));
        }
        if (ctx.readWriteMaplike() != null) {
            return ctx.readWriteMaplike().maplikeRest().accept(new MapLikeRestVisitor(true));
        }
        if (ctx.readWriteSetlike() != null) {
            return ctx.readWriteSetlike().setlikeRest().accept(new SetLikeRestVisitor(true));
        }
        System.err.println("Unexpepcted state in ReadOnlyMemberRest");
        return null;
    }
}
