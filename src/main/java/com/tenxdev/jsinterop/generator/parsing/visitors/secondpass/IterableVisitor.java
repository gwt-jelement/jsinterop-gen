package com.tenxdev.jsinterop.generator.parsing.visitors.secondpass;

import com.tenxdev.jsinterop.generator.model.Feature;
import com.tenxdev.jsinterop.generator.model.types.Type;
import com.tenxdev.jsinterop.generator.parsing.ParsingContext;
import com.tenxdev.jsinterop.generator.processing.TypeUtil;
import org.antlr4.webidl.WebIDLBaseVisitor;
import org.antlr4.webidl.WebIDLParser;

public class IterableVisitor extends ContextWebIDLBaseVisitor<Feature> {

    public IterableVisitor(ParsingContext parsingContext) {
        super(parsingContext);
    }

    //see https://heycam.github.io/webidl/#idl-iterable
    @Override
    public Feature visitIterable(WebIDLParser.IterableContext ctx) {
        Type type= ctx.type().accept(new TypeVisitor(parsingContetxt));
        if (ctx.optionalType() == null || ctx.optionalType().type() == null ){
            return new Feature(Feature.FeatureType.MapIterator, type, false);
        }else{
            Type type2 = ctx.optionalType().type().accept(new TypeVisitor(parsingContetxt));
            return new Feature(Feature.FeatureType.MapIterator, type, type2, false);
        }
    }
}
