package com.tenxdev.jsinterop.generator.parsing.visitors.secondpass;

import com.tenxdev.jsinterop.generator.model.Feature;
import com.tenxdev.jsinterop.generator.model.types.Type;
import com.tenxdev.jsinterop.generator.parsing.ParsingContext;
import org.antlr4.webidl.WebIDLParser;

public class MapLikeRestVisitor extends ContextWebIDLBaseVisitor<Feature> {
    private final boolean readOnly;

    MapLikeRestVisitor(ParsingContext context, boolean readOnly) {
        super(context);
        this.readOnly = readOnly;
    }

    @Override
    public Feature visitMaplikeRest(WebIDLParser.MaplikeRestContext ctx) {
        Type keyType = ctx.type(0).accept(new TypeVisitor(parsingContetxt));
        Type valueType = ctx.type(1).accept(new TypeVisitor(parsingContetxt));
        return new Feature(Feature.FeatureType.MapLike, keyType, valueType, readOnly);
    }
}
