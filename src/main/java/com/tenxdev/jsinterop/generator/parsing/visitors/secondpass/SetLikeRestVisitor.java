package com.tenxdev.jsinterop.generator.parsing.visitors.secondpass;

import com.tenxdev.jsinterop.generator.model.Feature;
import com.tenxdev.jsinterop.generator.model.types.Type;
import com.tenxdev.jsinterop.generator.parsing.ParsingContext;
import org.antlr4.webidl.WebIDLParser;

class SetLikeRestVisitor extends ContextWebIDLBaseVisitor<Feature> {
    private final boolean readOnly;

    public SetLikeRestVisitor(ParsingContext context, boolean readOnly) {
        super(context);
        this.readOnly = readOnly;
    }

    @Override
    public Feature visitSetlikeRest(WebIDLParser.SetlikeRestContext ctx) {
        Type type = ctx.type().accept(new TypeVisitor(parsingContext));
        return new Feature(Feature.FeatureType.SET_LIKE, type, readOnly);
    }
}
