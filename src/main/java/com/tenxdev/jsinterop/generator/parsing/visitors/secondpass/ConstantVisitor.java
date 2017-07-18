package com.tenxdev.jsinterop.generator.parsing.visitors.secondpass;

import com.tenxdev.jsinterop.generator.model.Constant;
import com.tenxdev.jsinterop.generator.model.types.Type;
import com.tenxdev.jsinterop.generator.parsing.ParsingContext;
import org.antlr4.webidl.WebIDLParser;

public class ConstantVisitor extends ContextWebIDLBaseVisitor<Constant> {

    public ConstantVisitor(ParsingContext parsingContext) {
        super(parsingContext);
    }

    @Override
    public Constant visitConst_(WebIDLParser.Const_Context ctx) {
        String name = ctx.IDENTIFIER_WEBIDL().getText();
        Type type = ctx.constType().accept(new TypeVisitor(parsingContext));
        String value = ctx.constValue().getText();
        return new Constant(name, type, value);
    }
}
