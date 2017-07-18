package com.tenxdev.jsinterop.generator.parsing.visitors.secondpass;

import com.tenxdev.jsinterop.generator.model.Attribute;
import com.tenxdev.jsinterop.generator.model.types.Type;
import com.tenxdev.jsinterop.generator.parsing.ParsingContext;
import org.antlr4.webidl.WebIDLParser;

class AttributeRestVisitor extends ContextWebIDLBaseVisitor<Attribute> {

    private final boolean readOnly;
    private final boolean staticAttribute;

    AttributeRestVisitor(ParsingContext context, boolean readOnly, boolean staticAttribute) {
        super(context);
        this.readOnly = readOnly;
        this.staticAttribute = staticAttribute;
    }

    @Override
    public Attribute visitAttributeRest(WebIDLParser.AttributeRestContext ctx) {
        String name = ctx.attributeName().getText();
        Type type = ctx.type().accept(new TypeVisitor(parsingContext));
        return new Attribute(name, type, readOnly, staticAttribute);
    }

}
