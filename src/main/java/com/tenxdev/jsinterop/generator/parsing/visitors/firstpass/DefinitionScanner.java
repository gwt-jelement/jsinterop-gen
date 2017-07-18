package com.tenxdev.jsinterop.generator.parsing.visitors.firstpass;

import com.tenxdev.jsinterop.generator.parsing.ParsingContext;
import com.tenxdev.jsinterop.generator.parsing.visitors.secondpass.ContextWebIDLBaseVisitor;
import org.antlr4.webidl.WebIDLParser;

class DefinitionScanner extends ContextWebIDLBaseVisitor<Void> {

    public DefinitionScanner(ParsingContext context) {
        super(context);
    }

    @Override
    public Void visitDefinition(WebIDLParser.DefinitionContext ctx) {
        if (ctx.callbackOrInterface() != null) {
            if (ctx.callbackOrInterface().interface_() != null) {
                return ctx.callbackOrInterface().interface_()
                        .accept(new InterfaceScanner(parsingContext));
            } else if (ctx.callbackOrInterface().callbackRestOrInterface() != null) {
                return ctx.callbackOrInterface().callbackRestOrInterface()
                        .accept(new CallbackScanner(parsingContext));
            }
        } else if (ctx.dictionary() != null) {
            return ctx.dictionary().accept(new DictionaryScanner(parsingContext));
        } else if (ctx.enum_() != null) {
            return ctx.enum_().accept(new EnumScanner(parsingContext));
        } else if (ctx.typedef() != null) {
            return ctx.typedef().accept(new TypeDefScanner(parsingContext));
        }
        return null;
    }
}
