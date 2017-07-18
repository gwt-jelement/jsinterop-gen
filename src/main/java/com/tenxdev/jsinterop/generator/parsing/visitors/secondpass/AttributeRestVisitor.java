package com.tenxdev.jsinterop.generator.parsing.visitors.secondpass;

import com.tenxdev.jsinterop.generator.model.Attribute;
import com.tenxdev.jsinterop.generator.model.types.Type;
import com.tenxdev.jsinterop.generator.parsing.ParsingContext;
import org.antlr4.webidl.WebIDLParser;

public class AttributeRestVisitor extends ContextWebIDLBaseVisitor<Attribute> {

    private final boolean readOnly;
    private final boolean static_;

    AttributeRestVisitor(ParsingContext context, boolean readOnly, boolean static_) {
        super(context);
        this.readOnly = readOnly;
        this.static_ = static_;
    }

    @Override
    public Attribute visitAttributeRest(WebIDLParser.AttributeRestContext ctx) {
        String name = ctx.attributeName().getText();
        Type type = ctx.type().accept(new TypeVisitor(parsingContext));
        return new Attribute(name, type, readOnly, static_);
    }

}
