package com.tenxdev.jsinterop.generator.parsing.visitors.firstpass;

import com.tenxdev.jsinterop.generator.model.InterfaceDefinition;
import com.tenxdev.jsinterop.generator.model.InterfaceMember;
import com.tenxdev.jsinterop.generator.model.Method;
import com.tenxdev.jsinterop.generator.model.types.ObjectType;
import com.tenxdev.jsinterop.generator.parsing.ParsingContext;
import com.tenxdev.jsinterop.generator.parsing.visitors.secondpass.ContextWebIDLBaseVisitor;
import com.tenxdev.jsinterop.generator.parsing.visitors.secondpass.InterfaceMembersVisitor;
import org.antlr4.webidl.WebIDLParser;

import java.util.List;

public class InterfaceScanner extends ContextWebIDLBaseVisitor<Void> {

    public InterfaceScanner(ParsingContext context) {
        super(context);
    }

    @Override
    public Void visitInterface_(WebIDLParser.Interface_Context ctx) {
        String name = ctx.IDENTIFIER_WEBIDL().getText();
        parsingContetxt.getTypeFactory()
                .registerType(name, new ObjectType(name, parsingContetxt.getPackageSuffix()));
        return null;
    }
}
