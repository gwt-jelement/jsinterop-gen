package com.tenxdev.jsinterop.generator.parsing.visitors.firstpass;

import com.tenxdev.jsinterop.generator.model.types.ObjectType;
import com.tenxdev.jsinterop.generator.parsing.ParsingContext;
import com.tenxdev.jsinterop.generator.parsing.visitors.secondpass.ContextWebIDLBaseVisitor;
import com.tenxdev.jsinterop.generator.processing.ParserUtil;
import org.antlr4.webidl.WebIDLParser;

class CallbackScanner extends ContextWebIDLBaseVisitor<Void> {

    public CallbackScanner(ParsingContext context) {
        super(context);
    }

    @Override
    public Void visitCallbackRestOrInterface(WebIDLParser.CallbackRestOrInterfaceContext ctx) {
        if (ctx.interface_() != null) {
            return ctx.interface_().accept(new InterfaceScanner(parsingContext));
        } else if (ctx.callbackRest() != null) {
            return ctx.callbackRest().accept(this);
        } else {
            parsingContext.getlogger().reportError("unexpected state in CallbackRestOrInterface visitor");
            parsingContext.getlogger().reportError("Content: " + ParserUtil.getText(ctx));
            return null;
        }
    }

    @Override
    public Void visitCallbackRest(WebIDLParser.CallbackRestContext ctx) {
        String name = ctx.IDENTIFIER_WEBIDL().getText();
        parsingContext.getTypeFactory().registerType(name,
                new ObjectType(name, parsingContext.getPackageSuffix()));
        return null;
    }
}
