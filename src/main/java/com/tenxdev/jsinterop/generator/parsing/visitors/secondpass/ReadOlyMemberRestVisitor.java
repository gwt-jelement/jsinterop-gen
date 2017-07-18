package com.tenxdev.jsinterop.generator.parsing.visitors.secondpass;

import com.tenxdev.jsinterop.generator.model.InterfaceMember;
import com.tenxdev.jsinterop.generator.parsing.ParsingContext;
import org.antlr4.webidl.WebIDLParser;

class ReadOlyMemberRestVisitor extends ContextWebIDLBaseVisitor<InterfaceMember> {
    public ReadOlyMemberRestVisitor(ParsingContext parsingContext) {
        super(parsingContext);
    }

    @Override
    public InterfaceMember visitReadonlyMemberRest(WebIDLParser.ReadonlyMemberRestContext ctx) {
        if (ctx.attributeRest() != null) {
            return ctx.attributeRest().accept(new AttributeRestVisitor(parsingContext, true, false));
        }
        if (ctx.readWriteMaplike() != null) {
            return ctx.readWriteMaplike().maplikeRest().accept(new MapLikeRestVisitor(parsingContext, true));
        }
        if (ctx.readWriteSetlike() != null) {
            return ctx.readWriteSetlike().setlikeRest().accept(new SetLikeRestVisitor(parsingContext, true));
        }
        parsingContext.getErrorReporter().reportError("ReadOlyMemberRestVisitor: Unexpected state in ReadOnlyMemberRest");
        return null;
    }
}
