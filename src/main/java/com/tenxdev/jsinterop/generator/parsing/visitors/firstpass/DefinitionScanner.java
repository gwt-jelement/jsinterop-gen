package com.tenxdev.jsinterop.generator.parsing.visitors.firstpass;

import com.tenxdev.jsinterop.generator.model.Definition;
import com.tenxdev.jsinterop.generator.model.Method;
import com.tenxdev.jsinterop.generator.parsing.ParsingContext;
import com.tenxdev.jsinterop.generator.parsing.visitors.secondpass.*;
import org.antlr4.webidl.WebIDLParser;

import java.util.List;

public class DefinitionScanner extends ContextWebIDLBaseVisitor<Void> {

    public DefinitionScanner(ParsingContext context) {
        super(context);
    }

    @Override
    public Void visitDefinition(WebIDLParser.DefinitionContext ctx) {
        if (ctx.callbackOrInterface() != null) {
            if (ctx.callbackOrInterface().interface_() != null) {
                return ctx.callbackOrInterface().interface_()
                        .accept(new InterfaceScanner(parsingContetxt));
            } else if (ctx.callbackOrInterface().callbackRestOrInterface() != null) {
                return ctx.callbackOrInterface().callbackRestOrInterface()
                        .accept(new CallbackScanner(parsingContetxt));
            }
        } else if (ctx.dictionary() != null) {
            return ctx.dictionary().accept(new DictionaryScanner(parsingContetxt));
        } else if (ctx.enum_() != null) {
            return ctx.enum_().accept(new EnumScanner(parsingContetxt));
        } else if (ctx.typedef() != null) {
            return ctx.typedef().accept(new TypeDefScanner(parsingContetxt));
        }
        return null;
    }
}
