package com.tenxdev.jsinterop.generator.visitors;

import com.tenxdev.jsinterop.generator.model.Feature;
import com.tenxdev.jsinterop.generator.processing.TypeUtil;
import org.antlr4.webidl.WebIDLBaseVisitor;
import org.antlr4.webidl.WebIDLParser;

public class IterableVisitor extends WebIDLBaseVisitor<Feature> {

    //see https://heycam.github.io/webidl/#idl-iterable
    @Override
    public Feature visitIterable(WebIDLParser.IterableContext ctx) {
        String[] types = TypeUtil.INSTANCE.removeOptionalIndicator(ctx.type().accept(new TypeVisitor()));
        if (ctx.optionalType() == null || ctx.optionalType().type() == null ){
            return new Feature(Feature.FeatureType.MapIterator, types, false);
        }else{
            String[] types2 = TypeUtil.INSTANCE.removeOptionalIndicator(ctx.optionalType().type().accept(new TypeVisitor()));
            return new Feature(Feature.FeatureType.MapIterator, types, types2, false);
        }
    }
}
