package com.tenxdev.jsinterop.generator.visitors;

import com.tenxdev.jsinterop.generator.model.Attribute;
import com.tenxdev.jsinterop.generator.processing.TypeUtil;
import org.antlr4.webidl.WebIDLBaseVisitor;
import org.antlr4.webidl.WebIDLParser;

import java.lang.reflect.Array;
import java.util.Arrays;

public class AttributeRestVisitor extends WebIDLBaseVisitor<Attribute> {

    private final boolean readOnly;
    private final boolean static_;

    public AttributeRestVisitor(boolean readOnly, boolean static_) {
        this.readOnly = readOnly;
        this.static_ = static_;
    }

    @Override
    public Attribute visitAttributeRest(WebIDLParser.AttributeRestContext ctx) {
        String name = ctx.attributeName().getText();
        String[] types= TypeUtil.INSTANCE.removeOptionalIndicator(ctx.type().accept(new TypeVisitor()));
        return new Attribute(name, types, readOnly, static_);
    }

}
