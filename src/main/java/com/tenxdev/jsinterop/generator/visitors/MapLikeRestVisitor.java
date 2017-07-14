package com.tenxdev.jsinterop.generator.visitors;

import com.tenxdev.jsinterop.generator.model.Feature;
import com.tenxdev.jsinterop.generator.processing.TypeUtil;
import org.antlr4.webidl.WebIDLBaseVisitor;
import org.antlr4.webidl.WebIDLParser;

public class MapLikeRestVisitor extends WebIDLBaseVisitor<Feature> {
    private final boolean readOnly;

    public MapLikeRestVisitor(boolean readOnly) {
        this.readOnly = readOnly;
    }

    @Override
    public Feature visitMaplikeRest(WebIDLParser.MaplikeRestContext ctx) {
        String[] keyTypes = TypeUtil.INSTANCE.removeOptionalIndicator(ctx.type(0).accept(new TypeVisitor()));
        String[] valueTypes=TypeUtil.INSTANCE.removeOptionalIndicator(ctx.type(1).accept(new TypeVisitor()));
        return new Feature(Feature.FeatureType.MapLike, keyTypes, valueTypes, readOnly);
    }
}
