package com.tenxdev.jsinterop.generator.parsing.visitors.secondpass;

import com.tenxdev.jsinterop.generator.model.TypeDefinition;
import com.tenxdev.jsinterop.generator.model.types.Type;
import com.tenxdev.jsinterop.generator.parsing.ParsingContext;
import org.antlr4.webidl.WebIDLParser;

public class TypeDefVisitor extends ContextWebIDLBaseVisitor<TypeDefinition> {

    public TypeDefVisitor(ParsingContext parsingContext) {
        super(parsingContext);
    }

    @Override
    public TypeDefinition visitTypedef(WebIDLParser.TypedefContext ctx) {
        String name = ctx.IDENTIFIER_WEBIDL().getText();
        Type type = ctx.type().accept(new TypeVisitor(parsingContext));
        return new TypeDefinition(name, type);
    }
}
