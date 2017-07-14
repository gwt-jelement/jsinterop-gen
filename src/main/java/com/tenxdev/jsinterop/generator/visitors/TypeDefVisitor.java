package com.tenxdev.jsinterop.generator.visitors;

import com.tenxdev.jsinterop.generator.model.TypeDefinition;
import org.antlr4.webidl.WebIDLBaseVisitor;
import org.antlr4.webidl.WebIDLParser;

public class TypeDefVisitor extends WebIDLBaseVisitor<TypeDefinition>{

    @Override
    public TypeDefinition visitTypedef(WebIDLParser.TypedefContext ctx) {
        String name=ctx.IDENTIFIER_WEBIDL().getText();
        String[] types=ctx.type().accept(new TypeVisitor());
        return new TypeDefinition(name, types);
    }
}
