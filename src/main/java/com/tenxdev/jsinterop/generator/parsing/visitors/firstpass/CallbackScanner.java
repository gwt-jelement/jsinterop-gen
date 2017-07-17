package com.tenxdev.jsinterop.generator.parsing.visitors.firstpass;

import com.tenxdev.jsinterop.generator.model.CallbackDefinition;
import com.tenxdev.jsinterop.generator.model.Definition;
import com.tenxdev.jsinterop.generator.model.Method;
import com.tenxdev.jsinterop.generator.model.MethodArgument;
import com.tenxdev.jsinterop.generator.model.types.ObjectType;
import com.tenxdev.jsinterop.generator.model.types.Type;
import com.tenxdev.jsinterop.generator.parsing.ParsingContext;
import com.tenxdev.jsinterop.generator.parsing.visitors.secondpass.ArgumentsVisitor;
import com.tenxdev.jsinterop.generator.parsing.visitors.secondpass.ContextWebIDLBaseVisitor;
import com.tenxdev.jsinterop.generator.parsing.visitors.secondpass.InterfaceVisitor;
import com.tenxdev.jsinterop.generator.parsing.visitors.secondpass.TypeVisitor;
import com.tenxdev.jsinterop.generator.processing.ParserUtil;
import org.antlr4.webidl.WebIDLParser;

import java.util.Collections;
import java.util.List;

public class CallbackScanner extends ContextWebIDLBaseVisitor<Void> {

    public CallbackScanner(ParsingContext context) {
        super(context);
    }

    @Override
    public Void visitCallbackRestOrInterface(WebIDLParser.CallbackRestOrInterfaceContext ctx) {
        if (ctx.interface_() != null) {
            return ctx.interface_().accept(new InterfaceScanner(parsingContetxt));
        } else if (ctx.callbackRest() != null) {
            return ctx.callbackRest().accept(this);
        } else {
            parsingContetxt.getErrorReporter().reportError("unexpected state in CallbackRestOrInterface visitor");
            parsingContetxt.getErrorReporter().reportError("Content: " + ParserUtil.getText(ctx));
            return null;
        }
    }

    @Override
    public Void visitCallbackRest(WebIDLParser.CallbackRestContext ctx) {
        String name = ctx.IDENTIFIER_WEBIDL().getText();
        parsingContetxt.getTypeFactory().registerType(name,
                new ObjectType(name, parsingContetxt.getPackageSuffix()));
        return null;
    }
}
