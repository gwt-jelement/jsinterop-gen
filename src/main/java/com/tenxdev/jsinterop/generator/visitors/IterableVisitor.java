package com.tenxdev.jsinterop.generator.visitors;

import com.tenxdev.jsinterop.generator.model.Feature;
import org.antlr4.webidl.WebIDLBaseVisitor;
import org.antlr4.webidl.WebIDLParser;

public class IterableVisitor extends WebIDLBaseVisitor<Feature> {

    //see https://heycam.github.io/webidl/#idl-iterable
    @Override
    public Feature visitIterable(WebIDLParser.IterableContext ctx) {
        String type = ctx.type().accept(new TypeVisitor());
        String type2 = ctx.optionalType() == null || ctx.optionalType().type() == null ? null : ctx.optionalType().type().accept(new TypeVisitor());
        return type2 == null ? new Feature(Feature.FeatureType.MapIterator, type, false) :
                new Feature(Feature.FeatureType.MapIterator, type, type2, false);
    }
}
