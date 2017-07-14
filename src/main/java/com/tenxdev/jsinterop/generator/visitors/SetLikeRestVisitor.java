package com.tenxdev.jsinterop.generator.visitors;

import com.tenxdev.jsinterop.generator.model.Feature;
import com.tenxdev.jsinterop.generator.processing.TypeUtil;
import org.antlr4.webidl.WebIDLBaseVisitor;
import org.antlr4.webidl.WebIDLParser;

public class SetLikeRestVisitor extends WebIDLBaseVisitor<Feature> {
    private final boolean readOnly;

    public SetLikeRestVisitor(boolean readOnly) {
        this.readOnly = readOnly;
    }

    @Override
    public Feature visitSetlikeRest(WebIDLParser.SetlikeRestContext ctx) {
        String[] types = TypeUtil.INSTANCE.removeOptionalIndicator(ctx.type().accept(new TypeVisitor()));
        return new Feature(Feature.FeatureType.SetLike, types, readOnly);
    }
}
